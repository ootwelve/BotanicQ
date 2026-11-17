package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import negocio.PANC;
import negocio.Receita;
import negocio.Sintoma;

public class ReceitaDAO {
    private Connection conexao;

    // RF19: traz junto o nome do administrador que fez a ultima alteracao
    private static final String SELECT_BASE =
            "SELECT r.*, u.nome AS ultimo_admin_nome FROM receita r " +
            "LEFT JOIN usuario u ON u.id = r.ultimo_admin_id ";

    public ReceitaDAO(Connection conexao) {
        this.conexao = conexao;
    }

    /** @param adminId id do administrador logado (RF19) */
    public void cadastrar(Receita receita, int adminId) throws SQLException {
        String sql = "INSERT INTO receita (titulo, src_imagem, descricao, outros_ingredientes, preparo, uso, " +
                     "ultimo_admin_id, ultima_alteracao) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP) RETURNING id";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, receita.getTitulo());
            stmt.setString(2, receita.getSrc_imagem());
            stmt.setString(3, receita.getDesc());
            
            Array arrayIngredientes = conexao.createArrayOf("text", receita.getOutros_ingredientes().toArray());
            stmt.setArray(4, arrayIngredientes);
            
            stmt.setString(5, receita.getPreparo());
            stmt.setString(6, receita.getUso());
            stmt.setInt(7, adminId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                receita.setId(rs.getInt(1));
            }
        }

        salvarIngredientesPanc(receita);
        salvarSintomasReceita(receita);
    }

    private void salvarIngredientesPanc(Receita receita) throws SQLException {
        if (receita.getPanc_ingredientes() == null || receita.getPanc_ingredientes().isEmpty()) {
            return;
        }
        String sql = "INSERT INTO receita_panc (receita_id, panc_id) VALUES (?, ?)";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            for (PANC panc : receita.getPanc_ingredientes()) {
                stmt.setInt(1, receita.getId());
                stmt.setInt(2, panc.getId());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }
    
    private void salvarSintomasReceita(Receita receita) throws SQLException {
        if (receita.getSintoma_associados() == null || receita.getSintoma_associados().isEmpty()) {
            return;
        }
        
        String sql = "INSERT INTO receita_sintoma (receita_id, sintoma_id) VALUES (?, ?)";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            for (Sintoma sintoma : receita.getSintoma_associados()) {
                stmt.setInt(1, receita.getId());
                stmt.setInt(2, sintoma.getId());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }
    
    private List<Sintoma> carregarSintomasReceita(int receitaId) throws SQLException {
        List<Sintoma> sintomas = new ArrayList<>();
        String sql = "SELECT s.* FROM sintoma s " +
                     "JOIN receita_sintoma rs ON s.id = rs.sintoma_id " +
                     "WHERE rs.receita_id = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, receitaId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Sintoma sintoma = new Sintoma();
                sintoma.setId(rs.getInt("id"));
                sintoma.setNome(rs.getString("nome"));
                sintoma.setDesc(rs.getString("descricao"));
                sintomas.add(sintoma);
            }
        }
        return sintomas;
    }

    private Receita mapear(ResultSet rs) throws SQLException {
        Receita receita = new Receita();
        receita.setId(rs.getInt("id"));
        receita.setTitulo(rs.getString("titulo"));
        receita.setSrc_imagem(rs.getString("src_imagem"));
        receita.setDesc(rs.getString("descricao"));

        Array arraySql = rs.getArray("outros_ingredientes");
        if (arraySql != null) {
            String[] arr = (String[]) arraySql.getArray();
            receita.setOutros_ingredientes(new ArrayList<>(List.of(arr)));
        }

        receita.setPreparo(rs.getString("preparo"));
        receita.setUso(rs.getString("uso"));

        receita.setUltimo_admin(rs.getString("ultimo_admin_nome"));
        Timestamp ts = rs.getTimestamp("ultima_alteracao");
        if (ts != null) {
            receita.setUltima_alteracao(ts.toLocalDateTime());
        }
        return receita;
    }

    private void carregarRelacionamentos(Receita receita) throws SQLException {
        receita.setPanc_ingredientes(carregarPancsDaReceita(receita.getId()));
        receita.setSintoma_associados(carregarSintomasReceita(receita.getId()));
    }

    public List<Receita> listar() throws SQLException {
        List<Receita> receitas = new ArrayList<>();
        String sql = SELECT_BASE + "ORDER BY r.id";

        try (PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Receita receita = mapear(rs);
                carregarRelacionamentos(receita);
                receitas.add(receita);
            }
        }
        return receitas;
    }

    /** RF15: receitas indicadas para um sintoma de saude. */
    public List<Receita> listarPorSintoma(int sintomaId) throws SQLException {
        List<Receita> receitas = new ArrayList<>();
        String sql = "SELECT r.*, u.nome AS ultimo_admin_nome FROM receita r " +
                     "JOIN receita_sintoma rs ON rs.receita_id = r.id " +
                     "LEFT JOIN usuario u ON u.id = r.ultimo_admin_id " +
                     "WHERE rs.sintoma_id = ? ORDER BY r.id";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, sintomaId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Receita receita = mapear(rs);
                carregarRelacionamentos(receita);
                receitas.add(receita);
            }
        }
        return receitas;
    }

    public Receita buscarPorId(int id) throws SQLException {
        String sql = SELECT_BASE + "WHERE r.id = ?";
        Receita receita = null;

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                receita = mapear(rs);
                carregarRelacionamentos(receita);
            }
        }
        return receita;
    }

    private List<PANC> carregarPancsDaReceita(int receitaId) throws SQLException {
        List<PANC> pancs = new ArrayList<>();
        String sql = "SELECT p.* FROM panc p " +
                     "JOIN receita_panc rp ON p.id = rp.panc_id " +
                     "WHERE rp.receita_id = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, receitaId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                PANC panc = new PANC();
                panc.setId(rs.getInt("id"));
                panc.setNome(rs.getString("nome"));
                panc.setSrc_imagem(rs.getString("src_imagem"));
                panc.setDesc(rs.getString("descricao")); 
                panc.setOrigem_nativa(rs.getString("origem_nativa"));
                pancs.add(panc);
            }
        }
        return pancs;
    }

    /** @param adminId id do administrador logado (RF19) */
    public void editar(Receita receita, int adminId) throws SQLException {
        String sql = "UPDATE receita SET titulo = ?, src_imagem = ?, descricao = ?, " +
                     "outros_ingredientes = ?, preparo = ?, uso = ?, " +
                     "ultimo_admin_id = ?, ultima_alteracao = CURRENT_TIMESTAMP WHERE id = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, receita.getTitulo());
            stmt.setString(2, receita.getSrc_imagem());
            stmt.setString(3, receita.getDesc());
            
            Array arrayIngredientes = conexao.createArrayOf("text", receita.getOutros_ingredientes().toArray());
            stmt.setArray(4, arrayIngredientes);
            
            stmt.setString(5, receita.getPreparo());
            stmt.setString(6, receita.getUso());
            stmt.setInt(7, adminId);
            stmt.setInt(8, receita.getId());
            stmt.executeUpdate();
        }

        String sqlDelete = "DELETE FROM receita_panc WHERE receita_id = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sqlDelete)) {
            stmt.setInt(1, receita.getId());
            stmt.executeUpdate();
        }
        
        String sqlDeleteSintomas = "DELETE FROM receita_sintoma WHERE receita_id = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sqlDeleteSintomas)) {
            stmt.setInt(1, receita.getId());
            stmt.executeUpdate();
        }

        salvarSintomasReceita(receita);
        salvarIngredientesPanc(receita);
    }

    public void remover(int id) throws SQLException {
        String sqlRel = "DELETE FROM receita_panc WHERE receita_id = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sqlRel)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
        
        String sqlRelSintoma = "DELETE FROM receita_sintoma WHERE receita_id = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sqlRelSintoma)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }

        String sql = "DELETE FROM receita WHERE id = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}