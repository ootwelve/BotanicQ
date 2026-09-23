import java.sql.*;

public class UsuarioDAO {
    private Connection conexao;

    public UsuarioDAO(Connection conexao) {
        this.conexao = conexao;
    }

    public void cadastrar(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuario (nome, senha, acesso, data, obs) VALUES (?, ?, ?, ?, ?) RETURNING id";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getSenha());
            stmt.setInt(3, usuario.getAcesso());
            stmt.setDate(4, Date.valueOf(usuario.getData()));
            stmt.setString(5, usuario.getObs());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                usuario.setId(rs.getInt(1));
            }
        }
    }

    public void alterarSenha(int idUsuario, String novaSenha) throws SQLException {
        String sql = "UPDATE usuario SET senha = ? WHERE id = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, novaSenha);
            stmt.setInt(2, idUsuario);
            stmt.executeUpdate();
        }
    }

    public void editar(Usuario usuario) throws SQLException {
        String sql = "UPDATE usuario SET nome = ?, acesso = ?, data = ?, obs = ? WHERE id = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNome());
            stmt.setInt(2, usuario.getAcesso());
            stmt.setDate(3, Date.valueOf(usuario.getData()));
            stmt.setString(4, usuario.getObs());
            stmt.setInt(5, usuario.getId());
            stmt.executeUpdate();
        }
    }
}
