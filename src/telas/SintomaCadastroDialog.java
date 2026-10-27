package view;

import model.Sintoma;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class SintomaCadastroDialog extends JDialog {

    private JTextField txtNome;
    private JTextArea txtDescricao;

    private Sintoma sintomaEdicao;
    private boolean confirmado = false;

    private static final Color VERDE_ESCURO = new Color(34, 112, 62);
    private static final Color CINZA_BG = new Color(248, 249, 250);

    public SintomaCadastroDialog(Frame parent) {
        this(parent, null);
    }

    public SintomaCadastroDialog(Frame parent, Sintoma sintomaParaEditar) {
        super(parent, sintomaParaEditar == null ? "Cadastrar Sintoma" : "Editar Sintoma", true);
        this.sintomaEdicao = sintomaParaEditar;

        setSize(500, 380);
        setLocationRelativeTo(parent);
        setResizable(false);
        setLayout(new BorderLayout());

        JPanel pnlConteudo = new JPanel(new GridBagLayout());
        pnlConteudo.setBackground(Color.WHITE);
        pnlConteudo.setBorder(new EmptyBorder(20, 25, 20, 25));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 0, 8, 0);

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 1.0;
        pnlConteudo.add(criarLabelCampo("Nome do Sintoma / Condição:"), gbc);

        gbc.gridy = 1;
        txtNome = new JTextField();
        txtNome.setPreferredSize(new Dimension(0, 36));
        txtNome.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtNome.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 205, 200), 1),
                new EmptyBorder(0, 8, 0, 8)
        ));
        pnlConteudo.add(txtNome, gbc);

        gbc.gridy = 2;
        pnlConteudo.add(criarLabelCampo("Descrição / Diagnóstico Objetivo:"), gbc);

        gbc.gridy = 3; gbc.weighty = 1.0; gbc.fill = GridBagConstraints.BOTH;
        txtDescricao = new JTextArea(5, 20);
        txtDescricao.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtDescricao.setLineWrap(true);
        txtDescricao.setWrapStyleWord(true);
        txtDescricao.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 205, 200), 1),
                new EmptyBorder(5, 8, 5, 8)
        ));

        JScrollPane scrollDesc = new JScrollPane(txtDescricao);
        pnlConteudo.add(scrollDesc, gbc);

        add(pnlConteudo, BorderLayout.CENTER);
        add(criarPainelBotoes(), BorderLayout.SOUTH);

        if (sintomaEdicao != null) {
            preencherCampos();
        }
    }

    private JLabel criarLabelCampo(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        lbl.setForeground(new Color(60, 60, 60));
        return lbl;
    }

    private JPanel criarPainelBotoes() {
        JPanel pnlBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 15));
        pnlBotoes.setBackground(CINZA_BG);
        pnlBotoes.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 224, 230)));

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setPreferredSize(new Dimension(100, 35));
        btnCancelar.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btnCancelar.addActionListener(e -> dispose());

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.setPreferredSize(new Dimension(110, 35));
        btnSalvar.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnSalvar.setBackground(VERDE_ESCURO);
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.setFocusPainted(false);
        btnSalvar.setOpaque(true);
        btnSalvar.setBorderPainted(false);
        btnSalvar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnSalvar.addActionListener(e -> salvar());

        pnlBotoes.add(btnCancelar);
        pnlBotoes.add(btnSalvar);

        return pnlBotoes;
    }

    private void preencherCampos() {
        txtNome.setText(sintomaEdicao.getNome());
        txtDescricao.setText(sintomaEdicao.getDesc());
    }

    private void salvar() {
        if (txtNome.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe o nome do sintoma.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (sintomaEdicao == null) {
            sintomaEdicao = new Sintoma();
        }

        sintomaEdicao.setNome(txtNome.getText().trim());
        sintomaEdicao.setDesc(txtDescricao.getText().trim());

        confirmado = true;
        dispose();
    }

    public boolean isConfirmado() {
        return confirmado;
    }

    public Sintoma getSintoma() {
        return sintomaEdicao;
    }
}