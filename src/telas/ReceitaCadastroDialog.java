package telas;

import DAO.PancDAO;
import DAO.SintomaDAO;
import negocio.PANC;
import negocio.Receita;
import negocio.Sintoma;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReceitaCadastroDialog extends JDialog {

    private boolean confirmado = false;
    private Receita receita;

    private JTextField txtTitulo;
    private JTextField txtCaminhoImagem;
    private JTextArea txtOutrosIngredientes;
    private JTextArea txtModoPreparo;
    
    private JPanel pnlCheckPancs;
    private JPanel pnlCheckSintomas;
    private List<JCheckBox> listaCheckPancs = new ArrayList<>();
    private List<JCheckBox> listaCheckSintomas = new ArrayList<>();

    private static final Color VERDE_TEXTO = new Color(34, 112, 62);
    private static final Color VERDE_BOTAO = new Color(34, 112, 62);
    private static final Color BORDA_CINZA = new Color(210, 215, 210);

    public ReceitaCadastroDialog(JFrame parent, Receita receitaEdicao) {
        super(parent, receitaEdicao == null ? "Cadastrar Nova Receita" : "Editar Receita", true);
        
        this.receita = receitaEdicao != null ? receitaEdicao : new Receita();

        setSize(650, 750);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        initComponents();
        carregarDadosDoBancoParaSelecao();
        carregarDadosNaTela();
    }

    private void initComponents() {
        JPanel pnlPrincipal = new JPanel();
        pnlPrincipal.setLayout(new BoxLayout(pnlPrincipal, BoxLayout.Y_AXIS));
        pnlPrincipal.setBackground(Color.WHITE);
        pnlPrincipal.setBorder(new EmptyBorder(25, 30, 25, 30));

        JLabel lblHeader = new JLabel(receita.getId() == 0 ? "Nova Receita Culinária" : "Editar Receita (ID: " + receita.getId() + ")");
        lblHeader.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblHeader.setForeground(VERDE_TEXTO);
        lblHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
        pnlPrincipal.add(lblHeader);
        pnlPrincipal.add(Box.createRigidArea(new Dimension(0, 15)));

        pnlPrincipal.add(criarRotulo("Título da Receita:"));
        pnlPrincipal.add(Box.createRigidArea(new Dimension(0, 5)));
        txtTitulo = new JTextField();
        estilizarCampoTexto(txtTitulo, 38);
        pnlPrincipal.add(txtTitulo);
        pnlPrincipal.add(Box.createRigidArea(new Dimension(0, 12)));

        pnlPrincipal.add(criarRotulo("Caminho da Imagem (Arquivo Local):"));
        pnlPrincipal.add(Box.createRigidArea(new Dimension(0, 5)));
        
        JPanel pnlImagemBox = new JPanel(new BorderLayout(8, 0));
        pnlImagemBox.setBackground(Color.WHITE);
        pnlImagemBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        pnlImagemBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));

        txtCaminhoImagem = new JTextField();
        estilizarCampoTexto(txtCaminhoImagem, 38);

        JButton btnProcurar = new JButton("Procurar...");
        btnProcurar.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnProcurar.setPreferredSize(new Dimension(110, 38));
        btnProcurar.setBackground(new Color(230, 235, 230));
        btnProcurar.setForeground(new Color(50, 50, 50));
        btnProcurar.setFocusPainted(false);
        btnProcurar.setBorder(new LineBorder(BORDA_CINZA, 1, true));
        btnProcurar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnProcurar.addActionListener(e -> escolherImagem());

        pnlImagemBox.add(txtCaminhoImagem, BorderLayout.CENTER);
        pnlImagemBox.add(btnProcurar, BorderLayout.EAST);
        pnlPrincipal.add(pnlImagemBox);
        pnlPrincipal.add(Box.createRigidArea(new Dimension(0, 12)));

        pnlPrincipal.add(criarRotulo("PANCs Utilizadas na Receita:"));
        pnlPrincipal.add(Box.createRigidArea(new Dimension(0, 5)));
        pnlCheckPancs = new JPanel();
        pnlCheckPancs.setLayout(new BoxLayout(pnlCheckPancs, BoxLayout.Y_AXIS));
        pnlCheckPancs.setBackground(Color.WHITE);
        JScrollPane scrollPancs = new JScrollPane(pnlCheckPancs);
        scrollPancs.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPancs.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        scrollPancs.setBorder(new LineBorder(BORDA_CINZA, 1, true));
        pnlPrincipal.add(scrollPancs);
        pnlPrincipal.add(Box.createRigidArea(new Dimension(0, 12)));

        pnlPrincipal.add(criarRotulo("Sintomas Tratados / Aliviados:"));
        pnlPrincipal.add(Box.createRigidArea(new Dimension(0, 5)));
        pnlCheckSintomas = new JPanel();
        pnlCheckSintomas.setLayout(new BoxLayout(pnlCheckSintomas, BoxLayout.Y_AXIS));
        pnlCheckSintomas.setBackground(Color.WHITE);
        JScrollPane scrollSintomas = new JScrollPane(pnlCheckSintomas);
        scrollSintomas.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollSintomas.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        scrollSintomas.setBorder(new LineBorder(BORDA_CINZA, 1, true));
        pnlPrincipal.add(scrollSintomas);
        pnlPrincipal.add(Box.createRigidArea(new Dimension(0, 12)));

        pnlPrincipal.add(criarRotulo("Outros Ingredientes (um por linha):"));
        pnlPrincipal.add(Box.createRigidArea(new Dimension(0, 5)));
        txtOutrosIngredientes = new JTextArea(3, 20);
        estilizarTextArea(txtOutrosIngredientes);
        JScrollPane scrollIngredientes = new JScrollPane(txtOutrosIngredientes);
        scrollIngredientes.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollIngredientes.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        pnlPrincipal.add(scrollIngredientes);
        pnlPrincipal.add(Box.createRigidArea(new Dimension(0, 12)));

        pnlPrincipal.add(criarRotulo("Modo de Preparo:"));
        pnlPrincipal.add(Box.createRigidArea(new Dimension(0, 5)));
        txtModoPreparo = new JTextArea(4, 20);
        estilizarTextArea(txtModoPreparo);
        JScrollPane scrollPreparo = new JScrollPane(txtModoPreparo);
        scrollPreparo.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPreparo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        pnlPrincipal.add(scrollPreparo);
        pnlPrincipal.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel pnlBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlBotoes.setBackground(Color.WHITE);
        pnlBotoes.setAlignmentX(Component.LEFT_ALIGNMENT);
        pnlBotoes.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnCancelar.setPreferredSize(new Dimension(110, 38));
        btnCancelar.setBackground(Color.WHITE);
        btnCancelar.setForeground(new Color(100, 100, 100));
        btnCancelar.setFocusPainted(false);
        btnCancelar.setBorder(new LineBorder(BORDA_CINZA, 1, true));
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancelar.addActionListener(e -> dispose());

        JButton btnSalvar = new JButton("Salvar Receita");
        btnSalvar.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnSalvar.setPreferredSize(new Dimension(140, 38));
        btnSalvar.setBackground(VERDE_BOTAO);
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.setFocusPainted(false);
        btnSalvar.setBorderPainted(false);
        btnSalvar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSalvar.addActionListener(e -> salvarReceita());

        pnlBotoes.add(btnCancelar);
        pnlBotoes.add(btnSalvar);
        pnlPrincipal.add(pnlBotoes);

        JScrollPane scrollPrincipal = new JScrollPane(pnlPrincipal);
        scrollPrincipal.setBorder(BorderFactory.createEmptyBorder());
        scrollPrincipal.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPrincipal, BorderLayout.CENTER);
    }

    private JLabel criarRotulo(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 13));
        lbl.setForeground(new Color(70, 70, 70));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private void estilizarCampoTexto(JTextField campo, int altura) {
        campo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        campo.setAlignmentX(Component.LEFT_ALIGNMENT);
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, altura));
        campo.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDA_CINZA, 1, true),
                new EmptyBorder(5, 8, 5, 8)
        ));
    }

    private void estilizarTextArea(JTextArea area) {
        area.setFont(new Font("SansSerif", Font.PLAIN, 13));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setAlignmentX(Component.LEFT_ALIGNMENT);
        area.setBorder(new EmptyBorder(6, 6, 6, 6));
    }

    private void escolherImagem() {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Imagens (JPG, PNG)", "jpg", "jpeg", "png");
        chooser.setFileFilter(filter);

        int resultado = chooser.showOpenDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File arquivo = chooser.getSelectedFile();
            txtCaminhoImagem.setText(arquivo.getAbsolutePath());
        }
    }

    private void carregarDadosDoBancoParaSelecao() {
        try (Connection conn = DAO.Conexao.getConexao()) {
            PancDAO pancDAO = new PancDAO(conn);
            List<PANC> todasPancs = pancDAO.listarTodas();
            for (PANC p : todasPancs) {
                JCheckBox chk = new JCheckBox(p.getNome());
                chk.setBackground(Color.WHITE);
                chk.putClientProperty("pancObj", p);
                
                if (receita.getPanc_ingredientes() != null) {
                    boolean selecionada = receita.getPanc_ingredientes().stream().anyMatch(item -> item.getId() == p.getId());
                    chk.setSelected(selecionada);
                }
                
                listaCheckPancs.add(chk);
                pnlCheckPancs.add(chk);
            }

            SintomaDAO sintomaDAO = new SintomaDAO(conn);
            List<Sintoma> todosSintomas = sintomaDAO.listar();
            for (Sintoma s : todosSintomas) {
                JCheckBox chk = new JCheckBox(s.getNome());
                chk.setBackground(Color.WHITE);
                chk.putClientProperty("sintomaObj", s);
                
                if (receita.getSintoma_associados() != null) {
                    boolean selecionado = receita.getSintoma_associados().stream().anyMatch(item -> item.getId() == s.getId());
                    chk.setSelected(selecionado);
                }
                
                listaCheckSintomas.add(chk);
                pnlCheckSintomas.add(chk);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados de seleção do banco: " + e.getMessage(), 
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarDadosNaTela() {
        if (receita.getTitulo() != null) {
            txtTitulo.setText(receita.getTitulo());
        }
        if (receita.getSrc_imagem() != null) {
            txtCaminhoImagem.setText(receita.getSrc_imagem());
        }
        if (receita.getOutros_ingredientes() != null && !receita.getOutros_ingredientes().isEmpty()) {
            txtOutrosIngredientes.setText(String.join("\n", receita.getOutros_ingredientes()));
        }
        if (receita.getPreparo() != null) {
            txtModoPreparo.setText(receita.getPreparo());
        }
    }

    private void salvarReceita() {
        String titulo = txtTitulo.getText().trim();
        if (titulo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "O título da receita não pode estar vazio.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        receita.setTitulo(titulo);
        receita.setSrc_imagem(txtCaminhoImagem.getText().trim());
        receita.setPreparo(txtModoPreparo.getText().trim());

        String[] linhasIngredientes = txtOutrosIngredientes.getText().split("\n");
        ArrayList<String> listaIngs = new ArrayList<>();
        for (String linha : linhasIngredientes) {
            if (!linha.trim().isEmpty()) {
                listaIngs.add(linha.trim());
            }
        }
        receita.setOutros_ingredientes(listaIngs);

        List<PANC> pancsSelecionadas = new ArrayList<>();
        for (JCheckBox chk : listaCheckPancs) {
            if (chk.isSelected()) {
                pancsSelecionadas.add((PANC) chk.getClientProperty("pancObj"));
            }
        }
        receita.setPanc_ingredientes(pancsSelecionadas);

        List<Sintoma> sintomasSelecionados = new ArrayList<>();
        for (JCheckBox chk : listaCheckSintomas) {
            if (chk.isSelected()) {
                sintomasSelecionados.add((Sintoma) chk.getClientProperty("sintomaObj"));
            }
        }
        receita.setSintoma_associados(sintomasSelecionados);

        this.confirmado = true;
        dispose();
    }

    public boolean isConfirmado() {
        return confirmado;
    }

    public Receita getReceita() {
        return receita;
    }
}