package telas;

import negocio.LogAlteracoes;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * RF19: conteudo da janela que exibe, dentro do programa, o arquivo logs/alteracoes.txt.
 * (A janela em si e a LogAlteracoesDialog.)
 */
public class LogAlteracoesPanel extends JPanel {

    private static final Color CINZA_BG = new Color(248, 249, 250);
    private static final Color BORDA_CINZA = new Color(220, 224, 230);
    private static final Color COR_CADASTRO = MainFrame.VERDE_ESCURO;
    private static final Color COR_EDICAO = new Color(200, 120, 20);
    private static final Color COR_EXCLUSAO = new Color(200, 50, 50);

    private JTable tabela;
    private DefaultTableModel tableModel;
    private JTextField txtFiltro;
    private JLabel lblContagem;

    private final Runnable onFechar;

    private List<LogAlteracoes.Registro> todosRegistros = new ArrayList<>();

    public LogAlteracoesPanel(Runnable onFechar) {
        this.onFechar = onFechar;

        setLayout(new BorderLayout());

        add(criarCabecalho(), BorderLayout.NORTH);

        JPanel pnlCentro = new JPanel(new BorderLayout(0, 12));
        pnlCentro.setBackground(Color.WHITE);
        pnlCentro.setBorder(new EmptyBorder(18, 22, 14, 22));
        pnlCentro.add(criarBarraFiltro(), BorderLayout.NORTH);
        pnlCentro.add(criarTabela(), BorderLayout.CENTER);
        add(pnlCentro, BorderLayout.CENTER);

        add(criarRodape(), BorderLayout.SOUTH);

        carregarLog();
    }

    private JPanel criarCabecalho() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        p.setBackground(MainFrame.VERDE_ESCURO);

        JLabel lbl = new JLabel("Log de Alterações do Sistema");
        lbl.setFont(new Font("SansSerif", Font.BOLD, 18));
        lbl.setForeground(Color.WHITE);

        p.add(lbl);
        return p;
    }

    private JPanel criarBarraFiltro() {
        JPanel barra = new JPanel(new BorderLayout(10, 0));
        barra.setOpaque(false);

        JPanel pnlEsquerda = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        pnlEsquerda.setOpaque(false);

        JLabel lblFiltrar = new JLabel("Filtrar:");
        lblFiltrar.setFont(new Font("SansSerif", Font.BOLD, 13));

        txtFiltro = new JTextField();
        txtFiltro.setPreferredSize(new Dimension(260, 36));
        txtFiltro.setToolTipText("Digite parte de um nome, ação, tipo ou data");
        txtFiltro.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { aplicarFiltro(); }
            @Override public void removeUpdate(DocumentEvent e) { aplicarFiltro(); }
            @Override public void changedUpdate(DocumentEvent e) { aplicarFiltro(); }
        });

        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.setPreferredSize(new Dimension(100, 36));
        btnAtualizar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAtualizar.addActionListener(e -> carregarLog());

        pnlEsquerda.add(lblFiltrar);
        pnlEsquerda.add(txtFiltro);
        pnlEsquerda.add(btnAtualizar);

        lblContagem = new JLabel(" ");
        lblContagem.setFont(new Font("SansSerif", Font.ITALIC, 12));
        lblContagem.setForeground(Color.GRAY);

        barra.add(pnlEsquerda, BorderLayout.WEST);
        barra.add(lblContagem, BorderLayout.EAST);
        return barra;
    }

    private JComponent criarTabela() {
        String[] colunas = {"Data / Hora", "Administrador", "Ação", "Tipo", "Registro"};

        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabela = new JTable(tableModel);
        tabela.setRowHeight(34);
        tabela.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tabela.setSelectionBackground(MainFrame.VERDE_SELECAO);
        tabela.setSelectionForeground(Color.BLACK);
        tabela.setShowVerticalLines(false);
        tabela.setGridColor(new Color(230, 230, 230));
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

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

        // celulas com o mesmo recuo do cabecalho
        DefaultTableCellRenderer celulaRenderer = new DefaultTableCellRenderer();
        celulaRenderer.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));

        // coluna "Ação" colorida como os botoes: verde = cadastro, laranja = edicao, vermelho = exclusao
        DefaultTableCellRenderer acaoRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                lbl.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
                lbl.setFont(new Font("SansSerif", Font.BOLD, 13));
                String acao = value == null ? "" : value.toString();
                if (acao.equals(LogAlteracoes.CADASTRO)) {
                    lbl.setForeground(COR_CADASTRO);
                } else if (acao.equals(LogAlteracoes.EXCLUSAO)) {
                    lbl.setForeground(COR_EXCLUSAO);
                } else {
                    lbl.setForeground(COR_EDICAO);
                }
                return lbl;
            }
        };

        for (int i = 0; i < tabela.getColumnCount(); i++) {
            tabela.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
            tabela.getColumnModel().getColumn(i).setCellRenderer(i == 2 ? acaoRenderer : celulaRenderer);
        }

        tabela.getColumnModel().getColumn(0).setPreferredWidth(150);
        tabela.getColumnModel().getColumn(0).setMaxWidth(190);
        tabela.getColumnModel().getColumn(1).setPreferredWidth(140);
        tabela.getColumnModel().getColumn(2).setPreferredWidth(150);
        tabela.getColumnModel().getColumn(3).setPreferredWidth(90);
        tabela.getColumnModel().getColumn(4).setPreferredWidth(360);

        JScrollPane scrollPane = new JScrollPane(tabela);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(210, 215, 210), 1));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        return scrollPane;
    }

    private JPanel criarRodape() {
        JPanel pnl = new JPanel(new BorderLayout(15, 0));
        pnl.setBackground(CINZA_BG);
        pnl.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, BORDA_CINZA),
                new EmptyBorder(12, 22, 12, 22)
        ));

        JLabel lblArquivo = new JLabel("Arquivo: " + LogAlteracoes.getArquivo());
        lblArquivo.setFont(new Font("SansSerif", Font.ITALIC, 12));
        lblArquivo.setForeground(Color.GRAY);
        lblArquivo.setToolTipText(LogAlteracoes.getArquivo().toString());

        JButton btnFechar = new JButton("Fechar");
        btnFechar.setPreferredSize(new Dimension(100, 35));
        btnFechar.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btnFechar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnFechar.addActionListener(e -> {
            if (onFechar != null) {
                onFechar.run();
            }
        });

        pnl.add(lblArquivo, BorderLayout.CENTER);
        pnl.add(btnFechar, BorderLayout.EAST);
        return pnl;
    }

    private void carregarLog() {
        try {
            todosRegistros = LogAlteracoes.ler();
        } catch (IOException e) {
            e.printStackTrace();
            todosRegistros = new ArrayList<>();
            JOptionPane.showMessageDialog(this, "Não foi possível ler o arquivo de log: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
        aplicarFiltro();
    }

    private void aplicarFiltro() {
        String termo = txtFiltro.getText().toLowerCase().trim();

        tableModel.setRowCount(0);
        int exibidos = 0;
        for (LogAlteracoes.Registro r : todosRegistros) {
            if (!termo.isEmpty() && !contem(r, termo)) {
                continue;
            }
            tableModel.addRow(new Object[]{r.dataHora, r.administrador, r.acao, r.tipo, r.registro});
            exibidos++;
        }

        if (todosRegistros.isEmpty()) {
            lblContagem.setText("Nenhuma alteração registrada ainda.");
        } else if (termo.isEmpty()) {
            lblContagem.setText(exibidos + (exibidos == 1 ? " registro" : " registros"));
        } else {
            lblContagem.setText(exibidos + " de " + todosRegistros.size() + " registros");
        }
    }

    private boolean contem(LogAlteracoes.Registro r, String termo) {
        return r.dataHora.toLowerCase().contains(termo)
                || r.administrador.toLowerCase().contains(termo)
                || r.acao.toLowerCase().contains(termo)
                || r.tipo.toLowerCase().contains(termo)
                || r.registro.toLowerCase().contains(termo);
    }
}
