package view;

import model.PANC;
import model.Receita;
import model.Sintoma;
import model.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PancsListPanel extends JPanel {

    private JTable tabela;
    private DefaultTableModel model;
    private JTextField txtBuscaSintoma;
    private Usuario usuarioLogado;
    private MainFrame parentFrame;

    private List<PANC> listaPancsMock = new ArrayList<>();

    public PancsListPanel(MainFrame parentFrame, Usuario usuario) {
        this.parentFrame = parentFrame;
        this.usuarioLogado = usuario;

        setLayout(new BorderLayout(20, 20));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        add(criarTopo(), BorderLayout.NORTH);
        add(criarTabela(), BorderLayout.CENTER);

        carregarDadosMock();
    }

    private JPanel criarTopo() {
        JPanel topoContainer = new JPanel();
        topoContainer.setLayout(new BoxLayout(topoContainer, BoxLayout.Y_AXIS));
        topoContainer.setOpaque(false);

        JPanel pnlTitulo = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlTitulo.setOpaque(false);

        JLabel lblTitulo = new JLabel("Consulta de PANCs");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblTitulo.setForeground(MainFrame.VERDE_ESCURO);
        pnlTitulo.add(lblTitulo);

        JPanel pnlAcoes = new JPanel(new BorderLayout(10, 0));
        pnlAcoes.setOpaque(false);
        pnlAcoes.setBorder(BorderFactory.createEmptyBorder(15, 0, 5, 0));

        JPanel pnlEsquerda = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        pnlEsquerda.setOpaque(false);

        JLabel lblSintoma = new JLabel("Sintoma:");
        lblSintoma.setFont(new Font("SansSerif", Font.BOLD, 13));

        txtBuscaSintoma = new JTextField();
        txtBuscaSintoma.setPreferredSize(new Dimension(160, 36));
        txtBuscaSintoma.setToolTipText("Digite um sintoma para buscar");

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setPreferredSize(new Dimension(100, 36));
        btnBuscar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnBuscar.addActionListener(e -> buscarPorSintomaMock());
        txtBuscaSintoma.addActionListener(e -> buscarPorSintomaMock());

        JButton btnDetalhes = new JButton("Detalhes");
        btnDetalhes.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnDetalhes.setPreferredSize(new Dimension(130, 36));
        btnDetalhes.setBackground(new Color(60, 130, 180));
        btnDetalhes.setForeground(Color.WHITE);
        btnDetalhes.setFocusPainted(false);
        btnDetalhes.setBorderPainted(false);
        btnDetalhes.setOpaque(true);
        btnDetalhes.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnDetalhes.addActionListener(e -> abrirDetalhesSelecionado());

        pnlEsquerda.add(lblSintoma);
        pnlEsquerda.add(txtBuscaSintoma);
        pnlEsquerda.add(btnBuscar);
        pnlEsquerda.add(Box.createHorizontalStrut(10)); // Espaçamento discreto
        pnlEsquerda.add(btnDetalhes);

        JPanel pnlBotoesAdmin = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        pnlBotoesAdmin.setOpaque(false);
        pnlBotoesAdmin.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

        if (usuarioLogado != null && usuarioLogado.getAcesso() == 1) {
            
            JButton btnNovo = new JButton("+ Cadastrar PANC");
            btnNovo.setFont(new Font("SansSerif", Font.BOLD, 13));
            btnNovo.setPreferredSize(new Dimension(160, 36));
            btnNovo.setBackground(MainFrame.VERDE_ESCURO);
            btnNovo.setForeground(Color.WHITE);
            btnNovo.setFocusPainted(false);
            btnNovo.setBorderPainted(false);
            btnNovo.setOpaque(true);
            btnNovo.setCursor(new Cursor(Cursor.HAND_CURSOR));

            btnNovo.addActionListener(e -> {
                PancCadastroDialog dialog = new PancCadastroDialog(parentFrame);
                dialog.setVisible(true);

                if (dialog.isConfirmado()) {
                    PANC novaPanc = dialog.getPanc();
                    novaPanc.setId(listaPancsMock.size() + 1);
                    listaPancsMock.add(novaPanc);
                    preencherTabela(listaPancsMock);
                    JOptionPane.showMessageDialog(this, "PANC cadastrada com sucesso!");
                }
            });

            JButton btnEditar = new JButton("✏️ Editar");
            btnEditar.setFont(new Font("SansSerif", Font.BOLD, 13));
            btnEditar.setPreferredSize(new Dimension(100, 36));
            btnEditar.setBackground(new Color(220, 140, 40));
            btnEditar.setForeground(Color.WHITE);
            btnEditar.setFocusPainted(false);
            btnEditar.setBorderPainted(false);
            btnEditar.setOpaque(true);
            btnEditar.setCursor(new Cursor(Cursor.HAND_CURSOR));

            btnEditar.addActionListener(e -> editarPancSelecionada());

            JButton btnExcluir = new JButton("🗑️ Excluir");
            btnExcluir.setFont(new Font("SansSerif", Font.BOLD, 13));
            btnExcluir.setPreferredSize(new Dimension(100, 36));
            btnExcluir.setBackground(new Color(200, 50, 50));
            btnExcluir.setForeground(Color.WHITE);
            btnExcluir.setFocusPainted(false);
            btnExcluir.setBorderPainted(false);
            btnExcluir.setOpaque(true);
            btnExcluir.setCursor(new Cursor(Cursor.HAND_CURSOR));

            btnExcluir.addActionListener(e -> excluirPancSelecionada());

            pnlBotoesAdmin.add(btnNovo);
            pnlBotoesAdmin.add(btnEditar);
            pnlBotoesAdmin.add(btnExcluir);
        }

        pnlAcoes.add(pnlEsquerda, BorderLayout.WEST);
        pnlAcoes.add(pnlBotoesAdmin, BorderLayout.EAST);

        topoContainer.add(pnlTitulo);
        topoContainer.add(pnlAcoes);

        return topoContainer;
    }

    private JComponent criarTabela() {
        String[] colunas = {"ID", "Nome", "Origem Nativa", "Descrição", "Sintomas Tratados"};

        model = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabela = new JTable(model);
        tabela.setRowHeight(34);
        tabela.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tabela.setSelectionBackground(MainFrame.VERDE_SELECAO);
        tabela.setSelectionForeground(Color.BLACK);
        tabela.setShowVerticalLines(false);
        tabela.setGridColor(new Color(230, 230, 230));

        JTableHeader header = tabela.getTableHeader();
        header.setPreferredSize(new Dimension(0, 38));
        header.setReorderingAllowed(false);

        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                lbl.setBackground(MainFrame.VERDE_ESCURO);
                lbl.setForeground(Color.WHITE);
                lbl.setFont(new Font("SansSerif", Font.BOLD, 13));
                lbl.setHorizontalAlignment(SwingConstants.LEFT);
                lbl.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
                return lbl;
            }
        };

        for (int i = 0; i < tabela.getColumnCount(); i++) {
            tabela.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tabela.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);

        tabela.getColumnModel().getColumn(0).setPreferredWidth(60);
        tabela.getColumnModel().getColumn(0).setMaxWidth(80);
        tabela.getColumnModel().getColumn(1).setPreferredWidth(30);
        tabela.getColumnModel().getColumn(2).setPreferredWidth(30);

        tabela.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    abrirDetalhesSelecionado();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tabela);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(210, 215, 210), 1));

        return scrollPane;
    }
    
    private void abrirDetalhesSelecionado() {
        int linha = tabela.getSelectedRow();
        if (linha != -1) {
            PANC PancSelecionada = listaPancsMock.get(linha);
            parentFrame.exibirDetalhesPanc(PancSelecionada);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Selecione um sintoma para ver os detalhes.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void editarPancSelecionada() {
        int linhaSelecionada = tabela.getSelectedRow();

        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma PANC na tabela para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        PANC pancSelecionada = listaPancsMock.get(linhaSelecionada);
        PancCadastroDialog dialog = new PancCadastroDialog(parentFrame, pancSelecionada);
        dialog.setVisible(true);
    }
    
    private void excluirPancSelecionada() {
        int linhaSelecionada = tabela.getSelectedRow();

        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, 
                    "Selecione uma PANC na tabela para excluir.", 
                    "Aviso", 
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        PANC pancSelecionada = listaPancsMock.get(linhaSelecionada);

        int opcao = JOptionPane.showConfirmDialog(
                this,
                "Tem certeza que deseja excluir a PANC '" + pancSelecionada.getNome() + "'?",
                "Confirmar Exclusão",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (opcao == JOptionPane.YES_OPTION) {
            listaPancsMock.remove(linhaSelecionada);
            preencherTabela(listaPancsMock);

            JOptionPane.showMessageDialog(this, 
                    "PANC excluída com sucesso!", 
                    "Sucesso", 
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void carregarDadosMock() {
        listaPancsMock.clear();

        // PANC 1
        PANC p1 = new PANC();
        p1.setId(1);
        p1.setNome("Ora-pro-nóbis");
        p1.setOrigem_nativa("Cerrado Brasileiro");
        p1.setDesc("Folhosa rica em proteínas e fibras.");
        p1.setSrc_imagem("C:/Users/henri/Downloads/carro.jpg");
        
        List<Sintoma> s1 = new ArrayList<>();
        Sintoma sint1 = new Sintoma(); sint1.setNome("Anemia"); s1.add(sint1);
        Sintoma sint2 = new Sintoma(); sint2.setNome("Inflamação"); s1.add(sint2);
        p1.setSintomas_relacionados(s1);
        
        List<Receita> r1 = new ArrayList<>();
        Receita rec1 = new Receita();
        rec1.setTitulo("Refogado de Ora-pro-nóbis");
        rec1.setPreparo("Refogue as folhas rasgadas com alho e azeite por 2 minutos.");
        r1.add(rec1);
        p1.setReceitas_relacionadas(r1);

        // PANC 2
        PANC p2 = new PANC();
        p2.setId(2);
        p2.setNome("Peixinho-da-horta");
        p2.setOrigem_nativa("Região Sul");
        p2.setDesc("Planta empanada muito saborosa.");
        p2.setSrc_imagem("peixinho.jpg");
        
        List<Sintoma> s2 = new ArrayList<>();
        Sintoma sint3 = new Sintoma(); sint3.setNome("Tosse"); s2.add(sint3);
        Sintoma sint4 = new Sintoma(); sint4.setNome("Irritação na Garganta"); s2.add(sint4);
        p2.setSintomas_relacionados(s2);
        
        List<Receita> r2 = new ArrayList<>();
        Receita rec2 = new Receita();
        rec2.setTitulo("Refogado Tradicional de Peixinho-da-horta");
        rec2.setPreparo("Lave bem as folhas, refogue com alho, azeite e uma pitada de sal por 3 minutos.");
        r2.add(rec2);

        Receita rec3 = new Receita();
        rec3.setTitulo("Suco Verde Energético com Peixinho-da-horta");
        rec3.setPreparo("Bata as folhas no liquidificador com suco de 2 limões, água bem gelada e adoce a gosto.");
        r2.add(rec3);

        p2.setReceitas_relacionadas(r2);

        listaPancsMock.add(p1);
        listaPancsMock.add(p2);

        preencherTabela(listaPancsMock);
    }

    private void buscarPorSintomaMock() {
        String termo = txtBuscaSintoma.getText().trim();
        if (termo.isEmpty()) {
            preencherTabela(listaPancsMock);
            return;
        }

        List<PANC> filtradas = new ArrayList<>();
        for (PANC p : listaPancsMock) {
            if (p.getSintomas_relacionados() != null) {
                for (Sintoma s : p.getSintomas_relacionados()) {
                    if (s.getNome().toLowerCase().contains(termo.toLowerCase())) {
                        filtradas.add(p);
                        break;
                    }
                }
            }
        }

        preencherTabela(filtradas);
        if (filtradas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhuma PANC encontrada para o sintoma: " + termo);
        }
    }

    private void preencherTabela(List<PANC> lista) {
        model.setRowCount(0);
        for (PANC p : lista) {
            StringBuilder sintomasStr = new StringBuilder();
            if (p.getSintomas_relacionados() != null) {
                for (int i = 0; i < p.getSintomas_relacionados().size(); i++) {
                    Sintoma s = p.getSintomas_relacionados().get(i);
                    sintomasStr.append(s.getNome());
                    if (i < p.getSintomas_relacionados().size() - 1) {
                        sintomasStr.append(", ");
                    }
                }
            }

            model.addRow(new Object[]{
                p.getId(),
                p.getNome(),
                p.getOrigem_nativa(),
                p.getDesc(),
                sintomasStr.toString()
            });
        }
    }
}