package DAO;

import negocio.PANC;
import negocio.Receita;
import negocio.Sintoma;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PancDAO {
    private Connection conexao;

    // RF19: traz junto o nome do administrador que fez a ultima alteracao
    private static final String SELECT_BASE =
            "SELECT p.*, u.nome AS ultimo_admin_nome FROM panc p " +
            "LEFT JOIN usuario u ON u.id = p.ultimo_admin_id ";

    public PancDAO(Connection conexao) {
        this.conexao = conexao;
    }

    /** @param adminId id do administrador logado (RF19) */
    public void cadastrar(PANC panc, int adminId) throws SQLException {
        String sql = "INSERT INTO panc (src_imagem, nome, descricao, origem_nativa, ultimo_admin_id, ultima_alteracao) " +
                     "VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP) RETURNING id";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, panc.getSrc_imagem());
            stmt.setString(2, panc.getNome());
            stmt.setString(3, panc.getDesc());
            stmt.setString(4, panc.getOrigem_nativa());
            stmt.setInt(5, adminId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                panc.setId(rs.getInt(1));
            }
        }
        salvarSintomasRelacionados(panc);
    }

    private void salvarSintomasRelacionados(PANC panc) throws SQLException {
        if (panc.getSintomas_relacionados() == null || panc.getSintomas_relacionados().isEmpty()) {
            return;
        }

        String sql = "INSERT INTO panc_sintoma (panc_id, sintoma_id) VALUES (?, ?)";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            for (Sintoma sintoma : panc.getSintomas_relacionados()) {
                stmt.setInt(1, panc.getId());
                stmt.setInt(2, sintoma.getId());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    public void removerSintomaEspecifico(int pancId, int sintomaId) throws SQLException {
        String sql = "DELETE FROM panc_sintoma WHERE panc_id = ? AND sintoma_id = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, pancId);
            stmt.setInt(2, sintomaId);
            stmt.executeUpdate();
        }
    }

    private void removerTodosSintomasRelacionados(int pancId) throws SQLException {
        String sql = "DELETE FROM panc_sintoma WHERE panc_id = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, pancId);
            stmt.executeUpdate();
        }
    }

    private PANC mapear(ResultSet rs) throws SQLException {
        PANC panc = new PANC();
        panc.setId(rs.getInt("id"));
        panc.setSrc_imagem(rs.getString("src_imagem"));
        panc.setNome(rs.getString("nome"));
        panc.setDesc(rs.getString("descricao"));
        panc.setOrigem_nativa(rs.getString("origem_nativa"));
        panc.setUltimo_admin(rs.getString("ultimo_admin_nome"));
        Timestamp ts = rs.getTimestamp("ultima_alteracao");
        if (ts != null) {
            panc.setUltima_alteracao(ts.toLocalDateTime());
        }
        return panc;
    }

    public PANC buscarPorId(int id) throws SQLException {
        String sql = SELECT_BASE + "WHERE p.id = ?";
        PANC panc = null;

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                panc = mapear(rs);
                panc.setSintomas_relacionados(carregarSintomasDaPanc(id));
                panc.setReceitas_relacionadas(carregarReceitasDaPanc(id));
            }
        }
        return panc;
    }

    public List<PANC> listarTodas() throws SQLException {
        List<PANC> lista = new ArrayList<>();
        String sql = SELECT_BASE + "ORDER BY p.id";

        try (PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                PANC panc = mapear(rs);
                panc.setSintomas_relacionados(carregarSintomasDaPanc(panc.getId()));
                panc.setReceitas_relacionadas(carregarReceitasDaPanc(panc.getId()));

                lista.add(panc);
            }
        }
        return lista;
    }

    /** RF8: busca PANCs pelo nome (ou descricao) de um sintoma relacionado. */
    public List<PANC> buscarPorSintoma(String termoSintoma) throws SQLException {
        List<PANC> lista = new ArrayList<>();
        String sql = "SELECT DISTINCT p.*, u.nome AS ultimo_admin_nome FROM panc p " +
                     "JOIN panc_sintoma ps ON p.id = ps.panc_id " +
                     "JOIN sintoma s ON s.id = ps.sintoma_id " +
                     "LEFT JOIN usuario u ON u.id = p.ultimo_admin_id " +
                     "WHERE LOWER(s.nome) LIKE LOWER(?) OR LOWER(s.descricao) LIKE LOWER(?) " +
                     "ORDER BY p.id";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            String termoComCuringa = "%" + termoSintoma + "%";
            stmt.setString(1, termoComCuringa);
            stmt.setString(2, termoComCuringa);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                PANC panc = mapear(rs);
                panc.setSintomas_relacionados(carregarSintomasDaPanc(panc.getId()));
                // antes faltava carregar as receitas: a tela de detalhes
                // aparecia sem receitas quando se chegava nela por esta busca
                panc.setReceitas_relacionadas(carregarReceitasDaPanc(panc.getId()));

                lista.add(panc);
            }
        }
        return lista;
    }

    private List<Sintoma> carregarSintomasDaPanc(int pancId) throws SQLException {
        List<Sintoma> sintomas = new ArrayList<>();
        String sql = "SELECT s.* FROM sintoma s " +
                     "JOIN panc_sintoma ps ON s.id = ps.sintoma_id " +
                     "WHERE ps.panc_id = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, pancId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Sintoma s = new Sintoma();
                s.setId(rs.getInt("id"));
                s.setNome(rs.getString("nome"));
                s.setDesc(rs.getString("descricao"));
                sintomas.add(s);
            }
        }
        return sintomas;
    }

    private List<Receita> carregarReceitasDaPanc(int pancId) throws SQLException {
        List<Receita> receitas = new ArrayList<>();
        String sql = "SELECT r.* FROM receita r " +
                     "JOIN receita_panc rp ON r.id = rp.receita_id " +
                     "WHERE rp.panc_id = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, pancId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Receita r = new Receita();
                r.setId(rs.getInt("id"));
                r.setTitulo(rs.getString("titulo"));
                r.setSrc_imagem(rs.getString("src_imagem"));
                r.setDesc(rs.getString("descricao"));
                r.setPreparo(rs.getString("preparo"));
                r.setUso(rs.getString("uso"));
                receitas.add(r);
            }
        }
        return receitas;
    }

    /** @param adminId id do administrador logado (RF19) */
    public void editar(PANC panc, int adminId) throws SQLException {
        String sql = "UPDATE panc SET src_imagem = ?, nome = ?, descricao = ?, origem_nativa = ?, " +
                     "ultimo_admin_id = ?, ultima_alteracao = CURRENT_TIMESTAMP WHERE id = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, panc.getSrc_imagem());
            stmt.setString(2, panc.getNome());
            stmt.setString(3, panc.getDesc());
            stmt.setString(4, panc.getOrigem_nativa());
            stmt.setInt(5, adminId);
            stmt.setInt(6, panc.getId());
            stmt.executeUpdate();
        }

        removerTodosSintomasRelacionados(panc.getId());

        salvarSintomasRelacionados(panc);
    }

    public void remover(int id) throws SQLException {
        removerTodosSintomasRelacionados(id);

        String sql = "DELETE FROM panc WHERE id = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
