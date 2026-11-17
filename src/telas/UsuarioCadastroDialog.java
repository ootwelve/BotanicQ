package telas;

import negocio.Usuario;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class UsuarioCadastroDialog extends JDialog {
    private boolean confirmado = false;
    private Usuario usuario;

    private JTextField txtNome;
    private JPasswordField txtSenha;
    private JComboBox<String> cbAcesso;

    private static final Color VERDE_TEXTO = new Color(34, 112, 62);
    private static final Color VERDE_BOTAO = new Color(34, 112, 62);
    private static final Color BORDA_CINZA = new Color(210, 215, 210);

    public UsuarioCadastroDialog(JFrame parent) {
        super(parent, "Cadastrar Novo Usuário", true);
        this.usuario = new Usuario();

        setSize(420, 380);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        initComponents();
    }

    private void initComponents() {
        JPanel pnlPrincipal = new JPanel();
        pnlPrincipal.setLayout(new BoxLayout(pnlPrincipal, BoxLayout.Y_AXIS));
        pnlPrincipal.setBackground(Color.WHITE);
        pnlPrincipal.setBorder(new EmptyBorder(25, 30, 25, 30));

        JLabel lblHeader = new JLabel("Novo Utilizador");
        lblHeader.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblHeader.setForeground(VERDE_TEXTO);
        lblHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
        pnlPrincipal.add(lblHeader);
        pnlPrincipal.add(Box.createRigidArea(new Dimension(0, 20)));

        pnlPrincipal.add(criarRotulo("Nome do Utilizador:"));
        pnlPrincipal.add(Box.createRigidArea(new Dimension(0, 5)));
        txtNome = new JTextField();
        estilizarCampo(txtNome, 35);
        pnlPrincipal.add(txtNome);
        pnlPrincipal.add(Box.createRigidArea(new Dimension(0, 12)));

        pnlPrincipal.add(criarRotulo("Senha:"));
        pnlPrincipal.add(Box.createRigidArea(new Dimension(0, 5)));
        txtSenha = new JPasswordField();
        estilizarCampo(txtSenha, 35);
        pnlPrincipal.add(txtSenha);
        pnlPrincipal.add(Box.createRigidArea(new Dimension(0, 12)));

        pnlPrincipal.add(criarRotulo("Nível de Acesso:"));
        pnlPrincipal.add(Box.createRigidArea(new Dimension(0, 5)));
        cbAcesso = new JComboBox<>(new String[]{"Administrador (Acesso Total)", "Consulta (Acesso Leitura)"});
        cbAcesso.setFont(new Font("SansSerif", Font.PLAIN, 13));
        cbAcesso.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        cbAcesso.setAlignmentX(Component.LEFT_ALIGNMENT);
        pnlPrincipal.add(cbAcesso);
        pnlPrincipal.add(Box.createRigidArea(new Dimension(0, 25)));

        JPanel pnlBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlBotoes.setBackground(Color.WHITE);
        pnlBotoes.setAlignmentX(Component.LEFT_ALIGNMENT);
        pnlBotoes.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnCancelar.setPreferredSize(new Dimension(100, 35));
        btnCancelar.setBackground(Color.WHITE);
        btnCancelar.setForeground(new Color(100, 100, 100));
        btnCancelar.setFocusPainted(false);
        btnCancelar.setBorder(new LineBorder(BORDA_CINZA, 1, true));
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancelar.addActionListener(e -> dispose());

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnSalvar.setPreferredSize(new Dimension(110, 35));
        btnSalvar.setBackground(VERDE_BOTAO);
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.setFocusPainted(false);
        btnSalvar.setBorderPainted(false);
        btnSalvar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSalvar.addActionListener(e -> salvar());

        pnlBotoes.add(btnCancelar);
        pnlBotoes.add(btnSalvar);
        pnlPrincipal.add(pnlBotoes);

        add(pnlPrincipal, BorderLayout.CENTER);
    }

    private JLabel criarRotulo(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        lbl.setForeground(new Color(70, 70, 70));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private void estilizarCampo(JComponent campo, int altura) {
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

    private void salvar() {
        String nome = txtNome.getText().trim();
        String senha = new String(txtSenha.getPassword()).trim();

        if (nome.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        usuario.setNome(nome);
        usuario.setSenha(senha);
        usuario.setAcesso(cbAcesso.getSelectedIndex() == 0 ? 1 : 2);

        confirmado = true;
        dispose();
    }

    public boolean isConfirmado() {
        return confirmado;
    }

    public Usuario getUsuario() {
        return usuario;
    }
}