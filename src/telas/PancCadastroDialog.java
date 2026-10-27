package view;

import model.PANC;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PancCadastroDialog extends JDialog {

    private JTextField txtNome;
    private JTextField txtOrigemNativa;
    private JTextField txtSrcImagem;
    private JTextArea txtDesc;

    private PANC pancEdicao;
    private boolean confirmado = false;

    public PancCadastroDialog(Frame parent) {
        this(parent, null);
    }

    public PancCadastroDialog(Frame parent, PANC panc) {
        super(parent, panc == null ? "Cadastrar PANC" : "Editar PANC", true);
        this.pancEdicao = panc;

        setSize(480, 480);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        add(criarCabecalho(), BorderLayout.NORTH);
        add(criarFormulario(), BorderLayout.CENTER);
        add(criarBotoes(), BorderLayout.SOUTH);

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

        // Nome
        form.add(new JLabel("Nome:"), gbc);
        txtNome = new JTextField();
        txtNome.setPreferredSize(new Dimension(0, 32));
        form.add(txtNome, gbc);

        // Origem Nativa
        form.add(new JLabel("Origem Nativa:"), gbc);
        txtOrigemNativa = new JTextField();
        txtOrigemNativa.setPreferredSize(new Dimension(0, 32));
        form.add(txtOrigemNativa, gbc);

        // Caminho/URL da Imagem
        form.add(new JLabel("Caminho/URL da Imagem:"), gbc);
        txtSrcImagem = new JTextField();
        txtSrcImagem.setPreferredSize(new Dimension(0, 32));
        form.add(txtSrcImagem, gbc);

        // Descrição
        form.add(new JLabel("Descrição:"), gbc);
        txtDesc = new JTextArea(4, 20);
        txtDesc.setLineWrap(true);
        txtDesc.setWrapStyleWord(true);
        JScrollPane scrollDesc = new JScrollPane(txtDesc);
        form.add(scrollDesc, gbc);

        return form;
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

        btnSalvar.addActionListener(e -> salvarMock());

        p.add(btnCancelar);
        p.add(btnSalvar);
        return p;
    }

    private void preencherCampos(PANC p) {
        txtNome.setText(p.getNome());
        txtOrigemNativa.setText(p.getOrigem_nativa());
        txtSrcImagem.setText(p.getSrc_imagem());
        txtDesc.setText(p.getDesc());
    }

    // SIMULAÇÃO
    private void salvarMock() {
        String nome = txtNome.getText().trim();

        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha o nome da PANC.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (pancEdicao == null) {
            JOptionPane.showMessageDialog(this, "Modo de Teste: PANC '" + nome + "' criada com sucesso!");
        } else {
            JOptionPane.showMessageDialog(this, "Modo de Teste: PANC '" + nome + "' atualizada com sucesso!");
        }

        this.confirmado = true;
        dispose();
    }
    
    public boolean isConfirmado() {
        return confirmado;
    }
    
    public PANC getPanc() {
        return pancEdicao;
    }

    /*

	// PARA ATUALIZAR DEPOIS

    private void salvarBD() {
        String nome = txtNome.getText().trim();

        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha o nome da PANC.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            java.sql.Connection conexao = DAO.Conexao.getConexao();
            DAO.PancDAO dao = new DAO.PancDAO(conexao);

            if (pancEdicao == null) {
                PANC novaPanc = new PANC();
                novaPanc.setNome(nome);
                novaPanc.setOrigem_nativa(txtOrigemNativa.getText().trim());
                novaPanc.setSrc_imagem(txtSrcImagem.getText().trim());
                novaPanc.setDesc(txtDesc.getText().trim());
                novaPanc.setSintomas_relacionados(new ArrayList<>());

                dao.cadastrar(novaPanc);
                JOptionPane.showMessageDialog(this, "PANC cadastrada com sucesso!");
            } else {
                pancEdicao.setNome(nome);
                pancEdicao.setOrigem_nativa(txtOrigemNativa.getText().trim());
                pancEdicao.setSrc_imagem(txtSrcImagem.getText().trim());
                pancEdicao.setDesc(txtDesc.getText().trim());

                dao.editar(pancEdicao);
                JOptionPane.showMessageDialog(this, "PANC atualizada com sucesso!");
            }

            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar no banco: " + e.getMessage(), "Erro BD", JOptionPane.ERROR_MESSAGE);
        }
    }
    */
}