package DAO;

import java.sql.*;

import model.Sintoma;

public class SintomaDAO {
    private Connection conexao;

    public SintomaDAO(Connection conexao) {
        this.conexao = conexao;
    }

    public void cadastrar(Sintoma sintoma) throws SQLException {
        String sql = "INSERT INTO sintoma (nome, desc) VALUES (?, ?) RETURNING id";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, sintoma.getNome());
            stmt.setString(2, sintoma.getDesc());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                sintoma.setId(rs.getInt(1));
            }
        }
    }

    public Sintoma buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM sintoma WHERE id = ?";
        Sintoma sintoma = null;

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                sintoma = new Sintoma();
                sintoma.setId(rs.getInt("id"));
                sintoma.setNome(rs.getString("nome"));
                sintoma.setDesc(rs.getString("desc"));
            }
        }
        return sintoma;
    }

    public void editar(Sintoma sintoma) throws SQLException {
        String sql = "UPDATE sintoma SET nome = ?, desc = ? WHERE id = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, sintoma.getNome());
            stmt.setString(2, sintoma.getDesc());
            stmt.setInt(3, sintoma.getId());
            stmt.executeUpdate();
        }
    }

    public void remover(int id) throws SQLException {
        String sql = "DELETE FROM sintoma WHERE id = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}