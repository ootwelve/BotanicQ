package telas;

import negocio.Usuario;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class UsuarioAlterarSenhaDialog extends JDialog {
    private boolean confirmado = false;
    private String novaSenhaDigitada;

    private JPasswordField txtNovaSenha;
    private JPasswordField txtConfirmarSenha;

    private static final Color VERDE_TEXTO = new Color(34, 112, 62);
    private static final Color VERDE_BOTAO = new Color(34, 112, 62);
    private static final Color BORDA_CINZA = new Color(210, 215, 210);

    public UsuarioAlterarSenhaDialog(JFrame parent, Usuario usuarioAlvo) {
        super(parent, "Alterar Senha - " + usuarioAlvo.getNome(), true);

        setSize(400, 320);
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

        JLabel lblHeader = new JLabel("Nova Senha");
        lblHeader.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblHeader.setForeground(VERDE_TEXTO);
        lblHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
        pnlPrincipal.add(lblHeader);
        pnlPrincipal.add(Box.createRigidArea(new Dimension(0, 20)));

        pnlPrincipal.add(criarRotulo("Digite a Nova Senha:"));
        pnlPrincipal.add(Box.createRigidArea(new Dimension(0, 5)));
        txtNovaSenha = new JPasswordField();
        estilizarCampo(txtNovaSenha, 35);
        pnlPrincipal.add(txtNovaSenha);
        pnlPrincipal.add(Box.createRigidArea(new Dimension(0, 12)));

        pnlPrincipal.add(criarRotulo("Confirme a Nova Senha:"));
        pnlPrincipal.add(Box.createRigidArea(new Dimension(0, 5)));
        txtConfirmarSenha = new JPasswordField();
        estilizarCampo(txtConfirmarSenha, 35);
        pnlPrincipal.add(txtConfirmarSenha);
        pnlPrincipal.add(Box.createRigidArea(new Dimension(0, 25)));

        // Botões
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

        JButton btnSalvar = new JButton("Alterar Senha");
        btnSalvar.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnSalvar.setPreferredSize(new Dimension(130, 35));
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
        String senha = new String(txtNovaSenha.getPassword()).trim();
        String confirmar = new String(txtConfirmarSenha.getPassword()).trim();

        if (senha.isEmpty() || confirmar.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos de senha.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!senha.equals(confirmar)) {
            JOptionPane.showMessageDialog(this, "As senhas não coincidem. Tente novamente.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return;
        }

        novaSenhaDigitada = senha;
        confirmado = true;
        dispose();
    }

    public boolean isConfirmado() {
        return confirmado;
    }

    public String getNovaSenha() {
        return novaSenhaDigitada;
    }
}