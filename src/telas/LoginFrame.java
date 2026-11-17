package telas;

import negocio.Usuario;
import DAO.UsuarioDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;

public class LoginFrame extends JFrame {

    public static final Color VERDE_ESCURO = new Color(34, 112, 62);
    private static final Color CINZA_BG = new Color(248, 249, 250);
    private static final Color BORDA_CINZA = new Color(210, 215, 210);

    private JTextField txtUsuario;
    private JPasswordField txtSenha;

    public LoginFrame() {
        setTitle("BotanicQ - Acesso ao Sistema");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(780, 460);
        setLocationRelativeTo(null);
        setResizable(false);

        setLayout(new BorderLayout());

        add(criarPainelLateralEsquerdo(), BorderLayout.WEST);
        add(criarPainelFormularioDireito(), BorderLayout.CENTER);
    }

    private JPanel criarPainelLateralEsquerdo() {
        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBackground(VERDE_ESCURO);
        painel.setPreferredSize(new Dimension(280, 0));
        painel.setBorder(new EmptyBorder(40, 25, 40, 25));

        JLabel lblLogo = new JLabel("🌿 BotanicQ");
        lblLogo.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblLogo.setForeground(Color.WHITE);
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSubtitulo = new JLabel("<html><center>Plataforma para valorização das plantas alimentícias não convencionais.</center></html>");
        lblSubtitulo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblSubtitulo.setForeground(new Color(220, 240, 225));
        lblSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        painel.add(lblLogo);
        painel.add(Box.createRigidArea(new Dimension(0, 30)));
        painel.add(lblSubtitulo);

        return painel;
    }

    private JPanel criarPainelFormularioDireito() {
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(Color.WHITE);
        form.setBorder(new EmptyBorder(35, 40, 35, 40));

        JLabel lblTitulo = new JLabel("Bem-vindo de volta");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblTitulo.setForeground(VERDE_TEXTO_CORRIGIDO());
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(lblTitulo);

        JLabel lblInstrucao = new JLabel("Entre com suas credenciais para acessar o sistema.");
        lblInstrucao.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblInstrucao.setForeground(Color.GRAY);
        lblInstrucao.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(lblInstrucao);
        form.add(Box.createRigidArea(new Dimension(0, 20)));

        form.add(criarRotulo("Nome do Utilizador:"));
        form.add(Box.createRigidArea(new Dimension(0, 5)));
        txtUsuario = new JTextField();
        estilizarCampoTexto(txtUsuario, 36);
        form.add(txtUsuario);
        form.add(Box.createRigidArea(new Dimension(0, 15)));

        form.add(criarRotulo("Senha:"));
        form.add(Box.createRigidArea(new Dimension(0, 5)));
        txtSenha = new JPasswordField();
        estilizarCampoTexto(txtSenha, 36);
        form.add(txtSenha);
        form.add(Box.createRigidArea(new Dimension(0, 25)));

        JButton btnEntrar = new JButton("Entrar no Sistema");
        btnEntrar.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnEntrar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnEntrar.setBackground(VERDE_ESCURO);
        btnEntrar.setForeground(Color.WHITE);
        btnEntrar.setFocusPainted(false);
        btnEntrar.setBorderPainted(false);
        btnEntrar.setOpaque(true);
        btnEntrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEntrar.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        btnEntrar.addActionListener(e -> autenticarBD());
        form.add(btnEntrar);

        return form;
    }

    private JLabel criarRotulo(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        lbl.setForeground(new Color(70, 70, 70));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private void estilizarCampoTexto(JComponent campo, int altura) {
        campo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, altura));
        campo.setAlignmentX(Component.LEFT_ALIGNMENT);
        if (campo instanceof JTextField) {
            ((JTextField) campo).setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(BORDA_CINZA, 1, true),
                    new EmptyBorder(5, 8, 5, 8)
            ));
        }
    }

    private Color VERDE_TEXTO_CORRIGIDO() {
        return new Color(34, 112, 62);
    }

    private void autenticarBD() {
        String nomeInput = txtUsuario.getText().trim();
        String senhaInput = new String(txtSenha.getPassword());

        if (nomeInput.isEmpty() || senhaInput.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha o nome e a senha.", "Campos Obrigatórios", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (Connection conexao = DAO.Conexao.getConexao()) { 
            UsuarioDAO dao = new UsuarioDAO(conexao);
            Usuario usuarioLogado = dao.autenticar(nomeInput, senhaInput);

            if (usuarioLogado != null) {
                MainFrame main = new MainFrame(usuarioLogado);
                main.setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Nome de utilizador ou senha incorretos.", "Acesso Negado", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao ligar à base de dados: " + e.getMessage(), "Erro BD", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}