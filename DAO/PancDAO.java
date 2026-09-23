import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PancDAO {
    private Connection conexao;

    public PancDAO(Connection conexao) {
        this.conexao = conexao;
    }

    public void cadastrar(PANC panc) throws SQLException {
        String sql = "INSERT INTO panc (src_imagem, nome, desc, origem_nativa) VALUES (?, ?, ?, ?) RETURNING id";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, panc.getSrc_imagem());
            stmt.setString(2, panc.getNome());
            stmt.setString(3, panc.getDesc());
            stmt.setString(4, panc.getOrigem_nativa());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                panc.setId(rs.getInt(1));
            }
        }
        salvarSintomasRelacionados(panc);
    }

    private void salvarSintomasRelacionados(PANC panc) throws SQLException {
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

    public PANC buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM panc WHERE id = ?";
        PANC panc = null;

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                panc = new PANC();
                panc.setId(rs.getInt("id"));
                panc.setSrc_imagem(rs.getString("src_imagem"));
                panc.setNome(rs.getString("nome"));
                panc.setDesc(rs.getString("desc"));
                panc.setOrigem_nativa(rs.getString("origem_nativa"));
                
                panc.setSintomas_relacionados(carregarSintomasDaPanc(id));
            }
        }
        return panc;
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
                s.setDesc(rs.getString("desc"));
                sintomas.add(s);
            }
        }
        return sintomas;
    }

    public void editar(PANC panc) throws SQLException {
        String sql = "UPDATE panc SET src_imagem = ?, nome = ?, desc = ?, origem_nativa = ? WHERE id = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, panc.getSrc_imagem());
            stmt.setString(2, panc.getNome());
            stmt.setString(3, panc.getDesc());
            stmt.setString(4, panc.getOrigem_nativa());
            stmt.setInt(5, panc.getId());
            stmt.executeUpdate();
        }
    }

    public void remover(int id) throws SQLException {
        String sqlRel = "DELETE FROM panc_sintoma WHERE panc_id = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sqlRel)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }

        String sql = "DELETE FROM panc WHERE id = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
