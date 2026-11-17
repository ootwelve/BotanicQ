package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import negocio.Receita;
import negocio.Sintoma;

public class SintomaDAO {
    private Connection conexao;

    // RF19: traz junto o nome do administrador que fez a ultima alteracao
    private static final String SELECT_BASE =
            "SELECT s.*, u.nome AS ultimo_admin_nome FROM sintoma s " +
            "LEFT JOIN usuario u ON u.id = s.ultimo_admin_id ";

    public SintomaDAO(Connection conexao) {
        this.conexao = conexao;
    }

    /** @param adminId id do administrador logado (RF19) */
    public void cadastrar(Sintoma sintoma, int adminId) throws SQLException {
        String sql = "INSERT INTO sintoma (nome, descricao, ultimo_admin_id, ultima_alteracao) " +
                     "VALUES (?, ?, ?, CURRENT_TIMESTAMP) RETURNING id";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, sintoma.getNome());
            stmt.setString(2, sintoma.getDesc());
            stmt.setInt(3, adminId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                sintoma.setId(rs.getInt(1));
            }
        }
    }

    private Sintoma mapear(ResultSet rs) throws SQLException {
        Sintoma s = new Sintoma();
        s.setId(rs.getInt("id"));
        s.setNome(rs.getString("nome"));
        s.setDesc(rs.getString("descricao"));
        s.setUltimo_admin(rs.getString("ultimo_admin_nome"));
        Timestamp ts = rs.getTimestamp("ultima_alteracao");
        if (ts != null) {
            s.setUltima_alteracao(ts.toLocalDateTime());
        }
        return s;
    }

    public List<Sintoma> listar() throws SQLException {
        List<Sintoma> sintomas = new ArrayList<>();
        String sql = SELECT_BASE + "ORDER BY s.id";

        try (PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Sintoma s = mapear(rs);
                s.setReceitas_relacionadas(carregarReceitasDoSintoma(s.getId()));

                sintomas.add(s);
            }
        }
        return sintomas;
    }

    public Sintoma buscarPorId(int id) throws SQLException {
        String sql = SELECT_BASE + "WHERE s.id = ?";
        Sintoma sintoma = null;

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                sintoma = mapear(rs);
                sintoma.setReceitas_relacionadas(carregarReceitasDoSintoma(id));
            }
        }
        return sintoma;
    }

    private List<Receita> carregarReceitasDoSintoma(int sintomaId) throws SQLException {
        List<Receita> receitas = new ArrayList<>();
        String sql = "SELECT r.* FROM receita r " +
                     "JOIN receita_sintoma rs ON r.id = rs.receita_id " +
                     "WHERE rs.sintoma_id = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, sintomaId);
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
    public void editar(Sintoma sintoma, int adminId) throws SQLException {
        String sql = "UPDATE sintoma SET nome = ?, descricao = ?, " +
                     "ultimo_admin_id = ?, ultima_alteracao = CURRENT_TIMESTAMP WHERE id = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, sintoma.getNome());
            stmt.setString(2, sintoma.getDesc());
            stmt.setInt(3, adminId);
            stmt.setInt(4, sintoma.getId());
            stmt.executeUpdate();
        }
    }

    public void remover(int id) throws SQLException {
        String sqlRel = "DELETE FROM receita_sintoma WHERE sintoma_id = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sqlRel)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }

        String sqlRelPanc = "DELETE FROM panc_sintoma WHERE sintoma_id = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sqlRelPanc)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }

        String sql = "DELETE FROM sintoma WHERE id = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
