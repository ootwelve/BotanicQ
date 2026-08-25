package sla;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RelatorioPancs extends JFrame {

    // ===== Classe que representa uma planta (PANC) cadastrada =====
    static class Planta {
        String id, nomePopular, nomeCientifico, categoria, regiao,
               beneficios, modoConsumo, tipoUso, dataCadastro;

        Planta(String id, String nomePopular, String nomeCientifico, String categoria,
               String regiao, String beneficios, String modoConsumo, String tipoUso, String dataCadastro) {
            this.id = id;
            this.nomePopular = nomePopular;
            this.nomeCientifico = nomeCientifico;
            this.categoria = categoria;
            this.regiao = regiao;
            this.beneficios = beneficios;
            this.modoConsumo = modoConsumo;
            this.tipoUso = tipoUso;
            this.dataCadastro = dataCadastro;
        }
    }

    // ===== "Banco de dados" em memória =====
    // Depois que o colega terminar a tela de Cadastro, os itens cadastrados
    // por ele deveriam entrar nessa mesma lista (ou em uma classe compartilhada).
    static List<Planta> repositorio = new ArrayList<>();
    static {
        repositorio.add(new Planta("001","Ora-pro-nóbis","Pereskia aculeata","Folhosa","Cerrado Brasileiro","Rica em proteínas, ferro e cálcio","Saladas, refogados, farinhas, sucos","Alimentar","20/05/2024"));
        repositorio.add(new Planta("002","Taioba","Xanthosoma taioba","Folhosa","Mata Atlântica","Fonte de fibras, vitaminas A e C","Cozida, refogada, caldos","Alimentar","18/05/2024"));
        repositorio.add(new Planta("003","Caruru","Amaranthus viridis","Folhosa","Norte / Nordeste","Rico em ferro, cálcio e vitamina A","Refogados, sopas, sucos","Alimentar","15/05/2024"));
        repositorio.add(new Planta("004","Peixinho","Stachys byzantina","Folhosa","Regiões Sul","Antiinflamatória, cicatrizante","Chás, compressas, banhos","Medicinal","10/05/2024"));
        repositorio.add(new Planta("005","Bertalha","Basella alba","Folhosa","Norte / Nordeste","Fonte de ômega 3, ferro e cálcio","Refogados, sopas, ovas omeletes","Alimentar","08/05/2024"));
        repositorio.add(new Planta("006","Hibisco","Hibiscus sabdariffa","Flor","Norte / Nordeste","Antioxidante, diurético","Chás, sucos, geleias","Medicinal","05/05/2024"));
        repositorio.add(new Planta("007","Dente-de-leão","Taraxacum officinale","Folhosa","Regiões Sul","Depurativo, Digestivo","Chás, saladas, sucos","Medicinal","02/05/2024"));
        repositorio.add(new Planta("008","Cúrcuma","Curcuma longa","Raiz","Cultivo Geral","Anti-inflamatória, antioxidante","Temperos, chás, cápsulas","Medicinal","28/04/2024"));
    }

    // Cor verde principal usada em toda a tela (barra, botões, IDs)
    private static final Color VERDE = new Color(30,110,60);
    private static final Color VERDE_CLARO = new Color(224,241,231);

    private JTextField campoBusca;
    private JComboBox<String> comboCategoria, comboRegiao, comboTipoUso;
    private DefaultTableModel modeloTabela;
    private JTable tabela;
    private JLabel labelContador;

    private final String[] colunas = {"ID","Nome Popular","Nome Científico","Categoria",
            "Região de Cultivo","Benefícios Principais","Modo de Consumo","Tipo de Uso","Data Cadastro"};

    public RelatorioPancs() {
        setTitle("PANCS - Relatórios");
        setSize(1300, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10,10));

        add(criarBarraSuperior(), BorderLayout.NORTH);
        add(criarPainelTabela(), BorderLayout.CENTER);

        carregarTabela(repositorio);
    }

    // ===== Barra superior verde com título e filtros =====
    private JPanel criarBarraSuperior() {
        JPanel painelExterno = new JPanel(new BorderLayout());

        // Barra verde de destaque no topo
        JPanel barraVerde = new JPanel(new BorderLayout());
        barraVerde.setBackground(VERDE);
        barraVerde.setBorder(BorderFactory.createEmptyBorder(14,20,14,20));

        JLabel titulo = new JLabel("🌿 Relatórios de PANCS");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        titulo.setForeground(Color.WHITE);
        barraVerde.add(titulo, BorderLayout.WEST);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        botoes.setOpaque(false);
        JButton btnExportar = new JButton("Exportar Excel");
        JButton btnImprimir = new JButton("Imprimir");
        estilizarBotaoSobreVerde(btnExportar);
        estilizarBotaoSobreVerde(btnImprimir);
        btnExportar.addActionListener(e -> exportarCsv());
        btnImprimir.addActionListener(e -> imprimirTabela());
        botoes.add(btnExportar);
        botoes.add(btnImprimir);
        barraVerde.add(botoes, BorderLayout.EAST);

        // Linha de filtros abaixo da barra
        JPanel linhaFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        linhaFiltros.setBackground(VERDE_CLARO);
        linhaFiltros.setBorder(BorderFactory.createEmptyBorder(6,15,6,15));

        campoBusca = new JTextField(20);
        campoBusca.setToolTipText("Buscar planta...");

        comboCategoria = new JComboBox<>(new String[]{"Todas","Folhosa","Flor","Raiz"});
        comboRegiao = new JComboBox<>(new String[]{"Todas","Cerrado Brasileiro","Mata Atlântica","Norte / Nordeste","Regiões Sul","Cultivo Geral"});
        comboTipoUso = new JComboBox<>(new String[]{"Todos","Alimentar","Medicinal"});

        JButton btnFiltrar = new JButton("Filtrar");
        estilizarBotaoPrimario(btnFiltrar);
        btnFiltrar.addActionListener(e -> aplicarFiltro());

        linhaFiltros.add(new JLabel("Buscar:"));
        linhaFiltros.add(campoBusca);
        linhaFiltros.add(new JLabel("Categoria:"));
        linhaFiltros.add(comboCategoria);
        linhaFiltros.add(new JLabel("Região:"));
        linhaFiltros.add(comboRegiao);
        linhaFiltros.add(new JLabel("Tipo de Uso:"));
        linhaFiltros.add(comboTipoUso);
        linhaFiltros.add(btnFiltrar);

        painelExterno.add(barraVerde, BorderLayout.NORTH);
        painelExterno.add(linhaFiltros, BorderLayout.SOUTH);
        return painelExterno;
    }

    private JPanel criarPainelTabela() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBorder(BorderFactory.createEmptyBorder(0,15,10,15));

        modeloTabela = new DefaultTableModel(colunas, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tabela = new JTable(modeloTabela);
        tabela.setRowHeight(28);
        tabela.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        tabela.getTableHeader().setBackground(VERDE);
        tabela.getTableHeader().setForeground(Color.WHITE);

        // Renderizador que deixa a coluna ID em verde e negrito
        DefaultTableCellRenderer rendererId = new DefaultTableCellRenderer();
        rendererId.setForeground(VERDE);
        rendererId.setFont(new Font("SansSerif", Font.BOLD, 13));
        rendererId.setHorizontalAlignment(SwingConstants.CENTER);
        tabela.getColumnModel().getColumn(0).setCellRenderer(rendererId);
        tabela.getColumnModel().getColumn(0).setPreferredWidth(50);

        JScrollPane scroll = new JScrollPane(tabela);
        painel.add(scroll, BorderLayout.CENTER);

        labelContador = new JLabel();
        labelContador.setForeground(VERDE);
        labelContador.setFont(new Font("SansSerif", Font.BOLD, 12));
        painel.add(labelContador, BorderLayout.SOUTH);
        return painel;
    }

    private void carregarTabela(List<Planta> lista) {
        modeloTabela.setRowCount(0);
        for (Planta p : lista) {
            modeloTabela.addRow(new Object[]{p.id, p.nomePopular, p.nomeCientifico, p.categoria,
                    p.regiao, p.beneficios, p.modoConsumo, p.tipoUso, p.dataCadastro});
        }
        labelContador.setText("Mostrando " + lista.size() + " de " + repositorio.size() + " registros");
    }

    private void aplicarFiltro() {
        String busca = campoBusca.getText().trim().toLowerCase();
        String categoria = (String) comboCategoria.getSelectedItem();
        String regiao = (String) comboRegiao.getSelectedItem();
        String tipoUso = (String) comboTipoUso.getSelectedItem();

        List<Planta> filtradas = new ArrayList<>();
        for (Planta p : repositorio) {
            boolean bateBusca = busca.isEmpty()
                    || p.nomePopular.toLowerCase().contains(busca)
                    || p.nomeCientifico.toLowerCase().contains(busca);
            boolean bateCategoria = categoria.equals("Todas") || p.categoria.equals(categoria);
            boolean bateRegiao = regiao.equals("Todas") || p.regiao.equals(regiao);
            boolean bateTipoUso = tipoUso.equals("Todos") || p.tipoUso.equals(tipoUso);

            if (bateBusca && bateCategoria && bateRegiao && bateTipoUso) {
                filtradas.add(p);
            }
        }
        carregarTabela(filtradas);
    }

    private void exportarCsv() {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("relatorio_pancs.csv"));
        int resultado = chooser.showSaveDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File arquivo = chooser.getSelectedFile();
            try (FileWriter writer = new FileWriter(arquivo)) {
                writer.write(String.join(";", colunas) + "\n");
                for (int i = 0; i < modeloTabela.getRowCount(); i++) {
                    StringBuilder linha = new StringBuilder();
                    for (int j = 0; j < colunas.length; j++) {
                        linha.append(modeloTabela.getValueAt(i,j)).append(j < colunas.length-1 ? ";" : "");
                    }
                    writer.write(linha.toString() + "\n");
                }
                JOptionPane.showMessageDialog(this, "Arquivo exportado com sucesso!\nAbra no Excel para visualizar.");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Erro ao exportar: " + e.getMessage());
            }
        }
    }

    private void imprimirTabela() {
        try {
            tabela.print();
        } catch (PrinterException e) {
            JOptionPane.showMessageDialog(this, "Erro ao imprimir: " + e.getMessage());
        }
    }

    private void estilizarBotaoPrimario(JButton b) {
        b.setBackground(VERDE);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
    }

    // Botões que ficam em cima do fundo verde da barra superior
    private void estilizarBotaoSobreVerde(JButton b) {
        b.setBackground(Color.WHITE);
        b.setForeground(VERDE);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(6,14,6,14));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RelatorioPancs tela = new RelatorioPancs();
            tela.setVisible(true);
        });
    }
}
