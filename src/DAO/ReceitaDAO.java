package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.PANC;
import model.Receita;

public class ReceitaDAO {
    private Connection conexao;

    public ReceitaDAO(Connection conexao) {
        this.conexao = conexao;
    }

    public void cadastrar(Receita receita) throws SQLException {
        String sql = "INSERT INTO receita (titulo, src_imagem, desc, outros_ingredientes, preparo, uso) " +
                     "VALUES (?, ?, ?, ?, ?, ?) RETURNING id";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, receita.getTitulo());
            stmt.setString(2, receita.getSrc_imagem());
            stmt.setString(3, receita.getDesc());
            
            // Converte o ArrayList<String> para o tipo ARRAY nativo do PostgreSQL
            Array arrayIngredientes = conexao.createArrayOf("text", receita.getOutros_ingredientes().toArray());
            stmt.setArray(4, arrayIngredientes);
            
            stmt.setString(5, receita.getPreparo());
            stmt.setString(6, receita.getUso());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                receita.setId(rs.getInt(1));
            }
        }

      salvarIngredientesPanc(receita);
    }

    private void salvarIngredientesPanc(Receita receita) throws SQLException {
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

    public Receita buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM receita WHERE id = ?";
        Receita receita = null;

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                receita = new Receita();
                receita.setId(rs.getInt("id"));
                receita.setTitulo(rs.getString("titulo"));
                receita.setSrc_imagem(rs.getString("src_imagem"));
                receita.setDesc(rs.getString("desc"));
                
                Array arraySql = rs.getArray("outros_ingredientes");
                if (arraySql != null) {
                    String[] arr = (String[]) arraySql.getArray();
                    receita.setOutros_ingredientes(new ArrayList<>(List.of(arr)));
                }

                receita.setPreparo(rs.getString("preparo"));
                receita.setUso(rs.getString("uso"));
                
                receita.setPanc_ingredientes(carregarPancsDaReceita(id));
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
                panc.setDesc(rs.getString("desc"));
                panc.setOrigem_nativa(rs.getString("origem_nativa"));
                pancs.add(panc);
            }
        }
        return pancs;
    }

    public void editar(Receita receita) throws SQLException {
        String sql = "UPDATE receita SET titulo = ?, src_imagem = ?, desc = ?, " +
                     "outros_ingredientes = ?, preparo = ?, uso = ? WHERE id = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, receita.getTitulo());
            stmt.setString(2, receita.getSrc_imagem());
            stmt.setString(3, receita.getDesc());
            
            Array arrayIngredientes = conexao.createArrayOf("text", receita.getOutros_ingredientes().toArray());
            stmt.setArray(4, arrayIngredientes);
            
            stmt.setString(5, receita.getPreparo());
            stmt.setString(6, receita.getUso());
            stmt.setInt(7, receita.getId());
            stmt.executeUpdate();
        }

        String sqlDelete = "DELETE FROM receita_panc WHERE receita_id = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sqlDelete)) {
            stmt.setInt(1, receita.getId());
            stmt.executeUpdate();
        }
        salvarIngredientesPanc(receita);
    }

    public void remover(int id) throws SQLException {
        String sqlRel = "DELETE FROM receita_panc WHERE receita_id = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sqlRel)) {
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