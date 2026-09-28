package view;

import model.Usuario;
import DAO.UsuarioDAO; // Descomente quando integrar com a base de dados
// import util.Conexao; // Descomente e ajuste o pacote da sua classe de Conexão

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

public class LoginFrame extends JFrame {

    public static final Color VERDE_ESCURO = new Color(34, 112, 62);

    private JTextField txtUsuario;
    private JPasswordField txtSenha;

    public LoginFrame() {
        setTitle("BotanicQ - Acesso");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 450);
        setLocationRelativeTo(null);
        setResizable(false);

        setLayout(new BorderLayout());

        add(criarCabecalho(), BorderLayout.NORTH);
        add(criarFormulario(), BorderLayout.CENTER);
        add(criarRodape(), BorderLayout.SOUTH);
    }

    private JPanel criarCabecalho() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(VERDE_ESCURO);
        panel.setPreferredSize(new Dimension(0, 90));

        JLabel lblLogo = new JLabel("❀🌿 BotanicQ");
        lblLogo.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblLogo.setForeground(Color.WHITE);

        panel.add(lblLogo);
        return panel;
    }

    private JPanel criarFormulario() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 0, 5, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        JLabel lblUser = new JLabel("Nome do Utilizador:");
        lblUser.setFont(new Font("SansSerif", Font.BOLD, 12));
        form.add(lblUser, gbc);

        txtUsuario = new JTextField("admin");
        txtUsuario.setPreferredSize(new Dimension(0, 35));
        form.add(txtUsuario, gbc);

        JLabel lblSenha = new JLabel("Senha:");
        lblSenha.setFont(new Font("SansSerif", Font.BOLD, 12));
        form.add(lblSenha, gbc);

        txtSenha = new JPasswordField("1234");
        txtSenha.setPreferredSize(new Dimension(0, 35));
        form.add(txtSenha, gbc);

        JLabel lblDica = new JLabel("<html><center><i>Modo de Teste Visual:<br>Digite <b>admin</b> = acesso Total | Qualquer outro = Leitura</i></center></html>");
        lblDica.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblDica.setForeground(Color.GRAY);
        lblDica.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.insets = new Insets(15, 0, 0, 0);
        form.add(lblDica, gbc);

        return form;
    }

    private JPanel criarRodape() {
        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        rodape.setBackground(Color.WHITE);

        JButton btnEntrar = new JButton("Entrar no Sistema");
        btnEntrar.setPreferredSize(new Dimension(320, 40));
        btnEntrar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnEntrar.setBackground(VERDE_ESCURO);
        btnEntrar.setForeground(Color.WHITE);
        btnEntrar.setFocusPainted(false);
        btnEntrar.setBorderPainted(false);
        btnEntrar.setOpaque(true);
        btnEntrar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnEntrar.addActionListener(e -> autenticarMock());

        rodape.add(btnEntrar);
        return rodape;
    }

    private void autenticarMock() {
        String nomeInput = txtUsuario.getText().trim();
        if (nomeInput.isEmpty()) {
            nomeInput = "Visitante";
        }

        Usuario usuarioSimulado = new Usuario();
        usuarioSimulado.setNome(nomeInput);
        
        // Se escrever "admin", atribui nível de acesso 1 (Administrador)
        if ("admin".equalsIgnoreCase(nomeInput)) {
            usuarioSimulado.setAcesso(1);
        } else {
            usuarioSimulado.setAcesso(2);
        }

        MainFrame main = new MainFrame(usuarioSimulado);
        main.setVisible(true);
        dispose();
    }

    /*
     
    PARA USAR DEPOIS COM O BANCO DE DADOS
     
    private void autenticarBD() {
        String nomeInput = txtUsuario.getText().trim();
        String senhaInput = new String(txtSenha.getPassword());

        if (nomeInput.isEmpty() || senhaInput.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha o nome e a senha.", "Campos Obrigatórios", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Connection conexao = Conexao.getConexao(); 

            UsuarioDAO dao = new UsuarioDAO(conexao);
            Usuario usuarioLogado = dao.autenticar(nomeInput, senhaInput);

            if (usuarioLogado != null) {
                MainFrame main = new MainFrame(usuarioLogado);
                main.setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Nome de utilizador ou senha incorretos.", "Acesso Negado", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao ligar à base de dados: " + e.getMessage(), "Erro BD", JOptionPane.ERROR_MESSAGE);
        }
    }
    */

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}