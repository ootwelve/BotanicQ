package telas;

import DAO.Conexao;
import DAO.SintomaDAO;
import negocio.PANC;
import negocio.Sintoma;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class PancCadastroDialog extends JDialog {

    private JTextField txtNome;
    private JTextField txtOrigemNativa;
    private JTextField txtSrcImagem;
    private JTextArea txtDesc;

    private JPanel pnlSintomasCheckboxes;
    private List<JCheckBox> listaCheckboxesSintomas = new ArrayList<>();

    private PANC pancEdicao;
    private boolean confirmado = false;

    public PancCadastroDialog(Frame parent) {
        this(parent, null);
    }

    public PancCadastroDialog(Frame parent, PANC panc) {
        super(parent, panc == null ? "Cadastrar PANC" : "Editar PANC", true);
        this.pancEdicao = panc;

        setSize(520, 620);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        add(criarCabecalho(), BorderLayout.NORTH);

        JScrollPane scrollPrincipal = new JScrollPane(criarFormulario());
        scrollPrincipal.setBorder(null);
        scrollPrincipal.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPrincipal, BorderLayout.CENTER);

        add(criarBotoes(), BorderLayout.SOUTH);

        carregarSintomasDisponiveis();

        if (pancEdicao != null) {
            preencherCampos(pancEdicao);
        }
    }

    private JPanel criarCabecalho() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        p.setBackground(MainFrame.VERDE_ESCURO);

        JLabel lbl = new JLabel(pancEdicao == null ? "🌿 Cadastrar Nova PANC" : "✏️ Editar PANC");
        lbl.setFont(new Font("SansSerif", Font.BOLD, 18));
        lbl.setForeground(Color.WHITE);

        p.add(lbl);
        return p;
    }

    private JPanel criarFormulario() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 0, 6, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        form.add(new JLabel("Nome:"), gbc);
        txtNome = new JTextField();
        txtNome.setPreferredSize(new Dimension(0, 32));
        form.add(txtNome, gbc);

        form.add(new JLabel("Origem Nativa:"), gbc);
        txtOrigemNativa = new JTextField();
        txtOrigemNativa.setPreferredSize(new Dimension(0, 32));
        form.add(txtOrigemNativa, gbc);

        form.add(new JLabel("Caminho da Imagem (Arquivo Local):"), gbc);
        JPanel pnlImagemBox = new JPanel(new BorderLayout(8, 0));
        pnlImagemBox.setBackground(Color.WHITE);

        txtSrcImagem = new JTextField();
        txtSrcImagem.setPreferredSize(new Dimension(0, 32));

        JButton btnProcurar = new JButton("Procurar...");
        btnProcurar.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnProcurar.setPreferredSize(new Dimension(110, 32));
        btnProcurar.setBackground(new Color(230, 235, 230));
        btnProcurar.setForeground(new Color(50, 50, 50));
        btnProcurar.setFocusPainted(false);
        btnProcurar.setBorder(new LineBorder(new Color(210, 215, 210), 1, true));
        btnProcurar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnProcurar.addActionListener(e -> escolherImagem());

        pnlImagemBox.add(txtSrcImagem, BorderLayout.CENTER);
        pnlImagemBox.add(btnProcurar, BorderLayout.EAST);
        form.add(pnlImagemBox, gbc);

        form.add(new JLabel("Descrição:"), gbc);
        txtDesc = new JTextArea(4, 20);
        txtDesc.setLineWrap(true);
        txtDesc.setWrapStyleWord(true);
        JScrollPane scrollDesc = new JScrollPane(txtDesc);
        form.add(scrollDesc, gbc);

        form.add(Box.createRigidArea(new Dimension(0, 10)), gbc);
        JLabel lblSintomasTitulo = new JLabel("Sintomas / Usos Medicinais Associados:");
        lblSintomasTitulo.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblSintomasTitulo.setForeground(MainFrame.VERDE_ESCURO);
        form.add(lblSintomasTitulo, gbc);

        pnlSintomasCheckboxes = new JPanel();
        pnlSintomasCheckboxes.setLayout(new BoxLayout(pnlSintomasCheckboxes, BoxLayout.Y_AXIS));
        pnlSintomasCheckboxes.setBackground(Color.WHITE);
        pnlSintomasCheckboxes.setBorder(new LineBorder(new Color(220, 224, 230), 1, true));

        JScrollPane scrollSintomas = new JScrollPane(pnlSintomasCheckboxes);
        scrollSintomas.setPreferredSize(new Dimension(0, 130));
        scrollSintomas.getViewport().setBackground(Color.WHITE);
        form.add(scrollSintomas, gbc);

        return form;
    }

    private void escolherImagem() {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Imagens (JPG, PNG)", "jpg", "jpeg", "png");
        chooser.setFileFilter(filter);

        int resultado = chooser.showOpenDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File arquivo = chooser.getSelectedFile();
            txtSrcImagem.setText(arquivo.getAbsolutePath());
        }
    }

    private void carregarSintomasDisponiveis() {
        pnlSintomasCheckboxes.removeAll();
        listaCheckboxesSintomas.clear();

        try (Connection conn = Conexao.getConexao()) {
            SintomaDAO sintomaDAO = new SintomaDAO(conn);
            List<Sintoma> todosSintomas = sintomaDAO.listar();

            for (Sintoma s : todosSintomas) {
                JCheckBox chk = new JCheckBox(s.getNome());
                chk.setBackground(Color.WHITE);
                chk.setFont(new Font("SansSerif", Font.PLAIN, 12));
                chk.putClientProperty("sintoma", s);

                listaCheckboxesSintomas.add(chk);
                pnlSintomasCheckboxes.add(chk);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar sintomas para associação: " + e.getMessage(), "Erro BD", JOptionPane.ERROR_MESSAGE);
        }

        pnlSintomasCheckboxes.revalidate();
        pnlSintomasCheckboxes.repaint();
    }

    private JPanel criarBotoes() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        p.setBackground(Color.WHITE);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.setBackground(MainFrame.VERDE_ESCURO);
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.setFocusPainted(false);
        btnSalvar.setBorderPainted(false);
        btnSalvar.setOpaque(true);
        btnSalvar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSalvar.addActionListener(e -> salvar());

        p.add(btnCancelar);
        p.add(btnSalvar);
        return p;
    }

    private void preencherCampos(PANC p) {
        txtNome.setText(p.getNome());
        txtOrigemNativa.setText(p.getOrigem_nativa());
        txtSrcImagem.setText(p.getSrc_imagem());
        txtDesc.setText(p.getDesc());

        if (p.getSintomas_relacionados() != null) {
            List<Integer> idsVinculados = new ArrayList<>();
            for (Sintoma sVinculado : p.getSintomas_relacionados()) {
                idsVinculados.add(sVinculado.getId());
            }

            for (JCheckBox chk : listaCheckboxesSintomas) {
                Sintoma sCheck = (Sintoma) chk.getClientProperty("sintoma");
                if (sCheck != null && idsVinculados.contains(sCheck.getId())) {
                    chk.setSelected(true);
                }
            }
        }
    }

    private void salvar() {
        String nome = txtNome.getText().trim();

        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha o nome da PANC.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (pancEdicao == null) {
            pancEdicao = new PANC();
        }

        pancEdicao.setNome(nome);
        pancEdicao.setOrigem_nativa(txtOrigemNativa.getText().trim());
        pancEdicao.setSrc_imagem(txtSrcImagem.getText().trim());
        pancEdicao.setDesc(txtDesc.getText().trim());

        List<Sintoma> sintomasSelecionados = new ArrayList<>();
        for (JCheckBox chk : listaCheckboxesSintomas) {
            if (chk.isSelected()) {
                Sintoma s = (Sintoma) chk.getClientProperty("sintoma");
                if (s != null) {
                    sintomasSelecionados.add(s);
                }
            }
        }
        pancEdicao.setSintomas_relacionados(sintomasSelecionados);

        this.confirmado = true;
        dispose();
    }

    public boolean isConfirmado() {
        return confirmado;
    }

    public PANC getPanc() {
        return pancEdicao;
    }
}