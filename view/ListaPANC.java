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

    // =========================================================
    // CLASSE PLANTA
    // =========================================================

    static class Planta {

        String id;
        String nomePopular;
        String nomeCientifico;
        String categoria;
        String regiao;
        String beneficios;
        String modoConsumo;
        String tipoUso;
        String dataCadastro;

        Planta(
                String id,
                String nomePopular,
                String nomeCientifico,
                String categoria,
                String regiao,
                String beneficios,
                String modoConsumo,
                String tipoUso,
                String dataCadastro) {

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

    // =========================================================
    // REPOSITÓRIO
    // =========================================================

    static List<Planta> repositorio = new ArrayList<>();

    static {

        repositorio.add(new Planta(
                "001",
                "Ora-pro-nóbis",
                "Pereskia aculeata",
                "Folhosa",
                "Cerrado Brasileiro",
                "Rica em proteínas, ferro e cálcio",
                "Saladas, refogados, farinhas, sucos",
                "Alimentar",
                "01/09/2026"
        ));

        repositorio.add(new Planta(
                "002",
                "Taioba",
                "Xanthosoma taioba",
                "Folhosa",
                "Mata Atlântica",
                "Fonte de fibras, vitaminas A e C",
                "Cozida, refogada, caldos",
                "Alimentar",
                "01/09/2026"
        ));

        repositorio.add(new Planta(
                "003",
                "Caruru",
                "Amaranthus viridis",
                "Folhosa",
                "Norte / Nordeste",
                "Rico em ferro, cálcio e vitamina A",
                "Refogados, sopas, sucos",
                "Alimentar",
                "01/09/2026"
        ));

        repositorio.add(new Planta(
                "004",
                "Peixinho",
                "Stachys byzantina",
                "Folhosa",
                "Regiões Sul",
                "Antiinflamatória, cicatrizante",
                "Chás, compressas, banhos",
                "Medicinal",
                "01/09/2026"
        ));

        repositorio.add(new Planta(
                "005",
                "Bertalha",
                "Basella alba",
                "Folhosa",
                "Norte / Nordeste",
                "Fonte de ômega 3, ferro e cálcio",
                "Refogados, sopas, ovas omeletes",
                "Alimentar",
                "01/09/2026"
        ));

        repositorio.add(new Planta(
                "006",
                "Hibisco",
                "Hibiscus sabdariffa",
                "Flor",
                "Norte / Nordeste",
                "Antioxidante, diurético",
                "Chás, sucos, geleias",
                "Medicinal",
                "01/09/2026"
        ));

        repositorio.add(new Planta(
                "007",
                "Dente-de-leão",
                "Taraxacum officinale",
                "Folhosa",
                "Regiões Sul",
                "Depurativo, Digestivo",
                "Chás, saladas, sucos",
                "Medicinal",
                "01/09/2026"
        ));

        repositorio.add(new Planta(
                "008",
                "Cúrcuma",
                "Curcuma longa",
                "Raiz",
                "Cultivo Geral",
                "Anti-inflamatória, antioxidante",
                "Temperos, chás, cápsulas",
                "Medicinal",
                "01/09/2026"
        ));
    }

    // =========================================================
    // CORES
    // =========================================================

    private static final Color VERDE =
            new Color(30, 110, 60);

    private static final Color VERDE_CLARO =
            new Color(224, 241, 231);

    // =========================================================
    // COMPONENTES
    // =========================================================

    private JTextField campoBusca;

    private JComboBox<String> comboCategoria;
    private JComboBox<String> comboRegiao;
    private JComboBox<String> comboTipoUso;

    private DefaultTableModel modeloTabela;
    private JTable tabela;

    private JLabel labelContador;

    private final String[] colunas = {
            "ID",
            "Nome Popular",
            "Nome Científico",
            "Categoria",
            "Região de Cultivo",
            "Benefícios Principais",
            "Modo de Consumo",
            "Tipo de Uso",
            "Data Cadastro"
    };

    // =========================================================
    // CONSTRUTOR
    // =========================================================

    public RelatorioPancs() {

        setTitle("PANCS - Relatórios");

        setSize(1300, 700);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout(10, 10));

        add(
                criarBarraSuperior(),
                BorderLayout.NORTH
        );

        add(
                criarPainelTabela(),
                BorderLayout.CENTER
        );

        carregarTabela(repositorio);
    }

    // =========================================================
    // BARRA SUPERIOR
    // =========================================================

    private JPanel criarBarraSuperior() {

        JPanel painelExterno =
                new JPanel(new BorderLayout());

        // -----------------------------
        // Barra verde
        // -----------------------------

        JPanel barraVerde =
                new JPanel(new BorderLayout());

        barraVerde.setBackground(VERDE);

        barraVerde.setBorder(
                BorderFactory.createEmptyBorder(
                        14, 20, 14, 20
                )
        );

        JLabel titulo =
                new JLabel("🌿 Relatórios de PANCS");

        titulo.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        22
                )
        );

        titulo.setForeground(Color.WHITE);

        barraVerde.add(
                titulo,
                BorderLayout.WEST
        );

        // -----------------------------
        // Botões superiores
        // -----------------------------

        JPanel botoes =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                8,
                                0
                        )
                );

        botoes.setOpaque(false);

        JButton btnExportar =
                new JButton("Exportar Excel");

        JButton btnImprimir =
                new JButton("Imprimir");

        estilizarBotaoSobreVerde(
                btnExportar
        );

        estilizarBotaoSobreVerde(
                btnImprimir
        );

        btnExportar.addActionListener(
                e -> exportarCsv()
        );

        btnImprimir.addActionListener(
                e -> imprimirTabela()
        );

        botoes.add(btnExportar);
        botoes.add(btnImprimir);

        barraVerde.add(
                botoes,
                BorderLayout.EAST
        );

        // =====================================================
        // FILTROS
        // =====================================================

        JPanel linhaFiltros =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                10,
                                10
                        )
                );

        linhaFiltros.setBackground(
                VERDE_CLARO
        );

        linhaFiltros.setBorder(
                BorderFactory.createEmptyBorder(
                        6, 15, 6, 15
                )
        );

        campoBusca =
                new JTextField(20);

        campoBusca.setToolTipText(
                "Buscar planta..."
        );

        comboCategoria =
                new JComboBox<>(
                        new String[]{
                                "Todas",
                                "Folhosa",
                                "Flor",
                                "Raiz"
                        }
                );

        comboRegiao =
                new JComboBox<>(
                        new String[]{
                                "Todas",
                                "Cerrado Brasileiro",
                                "Mata Atlântica",
                                "Norte / Nordeste",
                                "Regiões Sul",
                                "Cultivo Geral"
                        }
                );

        comboTipoUso =
                new JComboBox<>(
                        new String[]{
                                "Todos",
                                "Alimentar",
                                "Medicinal"
                        }
                );

        JButton btnFiltrar =
                new JButton("Filtrar");

        estilizarBotaoPrimario(
                btnFiltrar
        );

        btnFiltrar.addActionListener(
                e -> aplicarFiltro()
        );

        linhaFiltros.add(
                new JLabel("Buscar:")
        );

        linhaFiltros.add(
                campoBusca
        );

        linhaFiltros.add(
                new JLabel("Categoria:")
        );

        linhaFiltros.add(
                comboCategoria
        );

        linhaFiltros.add(
                new JLabel("Região:")
        );

        linhaFiltros.add(
                comboRegiao
        );

        linhaFiltros.add(
                new JLabel("Tipo de Uso:")
        );

        linhaFiltros.add(
                comboTipoUso
        );

        linhaFiltros.add(
                btnFiltrar
        );

        painelExterno.add(
                barraVerde,
                BorderLayout.NORTH
        );

        painelExterno.add(
                linhaFiltros,
                BorderLayout.SOUTH
        );

        return painelExterno;
    }

    // =========================================================
    // PAINEL DA TABELA
    // =========================================================

    private JPanel criarPainelTabela() {

        JPanel painel =
                new JPanel(new BorderLayout());

        painel.setBorder(
                BorderFactory.createEmptyBorder(
                        0, 15, 10, 15
                )
        );

        modeloTabela =
                new DefaultTableModel(
                        colunas,
                        0
                ) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int col) {

                        return false;
                    }
                };

        tabela =
                new JTable(modeloTabela);

        tabela.setRowHeight(28);

        tabela.getTableHeader().setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );

        tabela.getTableHeader()
                .setBackground(VERDE);

        tabela.getTableHeader()
                .setForeground(Color.WHITE);

        // =====================================================
        // COLUNA ID
        // =====================================================

        DefaultTableCellRenderer rendererId =
                new DefaultTableCellRenderer();

        rendererId.setForeground(VERDE);

        rendererId.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
                )
        );

        rendererId.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        tabela.getColumnModel()
                .getColumn(0)
                .setCellRenderer(rendererId);

        tabela.getColumnModel()
                .getColumn(0)
                .setPreferredWidth(50);

        JScrollPane scroll =
                new JScrollPane(tabela);

        // =====================================================
        // BOTÕES
        // =====================================================

        JPanel painelBotoes =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT
                        )
                );

        JButton btnEditar =
                new JButton("Editar");

        JButton btnRemover =
                new JButton("Remover");

        estilizarBotaoPrimario(
                btnEditar
        );

        estilizarBotaoPrimario(
                btnRemover
        );

        btnEditar.addActionListener(
                e -> editarPanc()
        );

        btnRemover.addActionListener(
                e -> removerPanc()
        );

        painelBotoes.add(btnEditar);
        painelBotoes.add(btnRemover);

        painel.add(
                painelBotoes,
                BorderLayout.NORTH
        );

        painel.add(
                scroll,
                BorderLayout.CENTER
        );

        labelContador =
                new JLabel();

        labelContador.setForeground(
                VERDE
        );

        labelContador.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );

        painel.add(
                labelContador,
                BorderLayout.SOUTH
        );

        return painel;
    }

    // =========================================================
    // EDITAR PANC
    // =========================================================

    private void editarPanc() {

        int linhaSelecionada =
                tabela.getSelectedRow();

        // Nenhuma linha selecionada
        if (linhaSelecionada == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Selecione uma PANC para editar.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        // Obtém o ID da PANC
        String id =
                modeloTabela
                        .getValueAt(
                                linhaSelecionada,
                                0
                        )
                        .toString();

        // Procura a PANC no repositório
        Planta plantaSelecionada = null;

        for (Planta p : repositorio) {

            if (p.id.equals(id)) {
                plantaSelecionada = p;
                break;
            }
        }

        if (plantaSelecionada == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Erro: PANC não encontrada.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        // =====================================================
        // CAMPOS DE EDIÇÃO
        // =====================================================

        JTextField campoNomePopular =
                new JTextField(
                        plantaSelecionada.nomePopular
                );

        JTextField campoNomeCientifico =
                new JTextField(
                        plantaSelecionada.nomeCientifico
                );

        JComboBox<String> campoCategoria =
                new JComboBox<>(
                        new String[]{
                                "Folhosa",
                                "Flor",
                                "Raiz"
                        }
                );

        campoCategoria.setSelectedItem(
                plantaSelecionada.categoria
        );

        JComboBox<String> campoRegiao =
                new JComboBox<>(
                        new String[]{
                                "Cerrado Brasileiro",
                                "Mata Atlântica",
                                "Norte / Nordeste",
                                "Regiões Sul",
                                "Cultivo Geral"
                        }
                );

        campoRegiao.setSelectedItem(
                plantaSelecionada.regiao
        );

        JTextField campoBeneficios =
                new JTextField(
                        plantaSelecionada.beneficios
                );

        JTextField campoModoConsumo =
                new JTextField(
                        plantaSelecionada.modoConsumo
                );

        JComboBox<String> campoTipoUso =
                new JComboBox<>(
                        new String[]{
                                "Alimentar",
                                "Medicinal"
                        }
                );

        campoTipoUso.setSelectedItem(
                plantaSelecionada.tipoUso
        );

        JTextField campoData =
                new JTextField(
                        plantaSelecionada.dataCadastro
                );

        // =====================================================
        // PAINEL DE EDIÇÃO
        // =====================================================

        JPanel painelEdicao =
                new JPanel(
                        new GridLayout(
                                0,
                                2,
                                8,
                                8
                        )
                );

        painelEdicao.add(
                new JLabel("Nome Popular:")
        );

        painelEdicao.add(
                campoNomePopular
        );

        painelEdicao.add(
                new JLabel("Nome Científico:")
        );

        painelEdicao.add(
                campoNomeCientifico
        );

        painelEdicao.add(
                new JLabel("Categoria:")
        );

        painelEdicao.add(
                campoCategoria
        );

        painelEdicao.add(
                new JLabel("Região de Cultivo:")
        );

        painelEdicao.add(
                campoRegiao
        );

        painelEdicao.add(
                new JLabel("Benefícios:")
        );

        painelEdicao.add(
                campoBeneficios
        );

        painelEdicao.add(
                new JLabel("Modo de Consumo:")
        );

        painelEdicao.add(
                campoModoConsumo
        );

        painelEdicao.add(
                new JLabel("Tipo de Uso:")
        );

        painelEdicao.add(
                campoTipoUso
        );

        painelEdicao.add(
                new JLabel("Data de Cadastro:")
        );

        painelEdicao.add(
                campoData
        );

        // =====================================================
        // ABRIR JANELA DE EDIÇÃO
        // =====================================================

        int resultado =
                JOptionPane.showConfirmDialog(
                        this,
                        painelEdicao,
                        "Editar PANC - ID " + id,
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                );

        // Usuário cancelou
        if (resultado != JOptionPane.OK_OPTION) {

            JOptionPane.showMessageDialog(
                    this,
                    "Edição cancelada.",
                    "Edição",
                    JOptionPane.INFORMATION_MESSAGE
            );

            return;
        }

        // =====================================================
        // VALIDAÇÃO
        // =====================================================

        String nomePopular =
                campoNomePopular
                        .getText()
                        .trim();

        String nomeCientifico =
                campoNomeCientifico
                        .getText()
                        .trim();

        String beneficios =
                campoBeneficios
                        .getText()
                        .trim();

        String modoConsumo =
                campoModoConsumo
                        .getText()
                        .trim();

        String data =
                campoData
                        .getText()
                        .trim();

        if (nomePopular.isEmpty()
                || nomeCientifico.isEmpty()
                || beneficios.isEmpty()
                || modoConsumo.isEmpty()
                || data.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Erro: preencha todos os campos.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        // =====================================================
        // CONFIRMAÇÃO DA EDIÇÃO
        // =====================================================

        int confirmar =
                JOptionPane.showConfirmDialog(
                        this,
                        "Deseja salvar as alterações\n"
                                + "da PANC \"" + nomePopular + "\"?",
                        "Confirmar edição",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

        if (confirmar != JOptionPane.YES_OPTION) {

            JOptionPane.showMessageDialog(
                    this,
                    "Alterações não foram salvas.",
                    "Edição",
                    JOptionPane.INFORMATION_MESSAGE
            );

            return;
        }

        // =====================================================
        // SALVAR ALTERAÇÕES
        // =====================================================

        try {

            plantaSelecionada.nomePopular =
                    nomePopular;

            plantaSelecionada.nomeCientifico =
                    nomeCientifico;

            plantaSelecionada.categoria =
                    (String) campoCategoria
                            .getSelectedItem();

            plantaSelecionada.regiao =
                    (String) campoRegiao
                            .getSelectedItem();

            plantaSelecionada.beneficios =
                    beneficios;

            plantaSelecionada.modoConsumo =
                    modoConsumo;

            plantaSelecionada.tipoUso =
                    (String) campoTipoUso
                            .getSelectedItem();

            plantaSelecionada.dataCadastro =
                    data;

            // Atualiza tabela
            aplicarFiltro();

            JOptionPane.showMessageDialog(
                    this,
                    "PANC editada com sucesso!",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao editar a PANC:\n"
                            + ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================
    // REMOVER PANC
    // =========================================================

    private void removerPanc() {

        int linhaSelecionada =
                tabela.getSelectedRow();

        // Nenhuma PANC selecionada
        if (linhaSelecionada == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Selecione uma PANC para remover.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        String id =
                modeloTabela
                        .getValueAt(
                                linhaSelecionada,
                                0
                        )
                        .toString();

        String nome =
                modeloTabela
                        .getValueAt(
                                linhaSelecionada,
                                1
                        )
                        .toString();

        // =====================================================
        // CONFIRMAÇÃO DA REMOÇÃO
        // =====================================================

        int confirmacao =
                JOptionPane.showConfirmDialog(
                        this,
                        "Tem certeza que deseja remover definitivamente\n"
                                + "a PANC \"" + nome + "\"?",
                        "Confirmar remoção",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

        // Usuário escolheu NÃO
        if (confirmacao != JOptionPane.YES_OPTION) {

            JOptionPane.showMessageDialog(
                    this,
                    "Remoção cancelada.",
                    "Remoção",
                    JOptionPane.INFORMATION_MESSAGE
            );

            return;
        }

        // =====================================================
        // REMOVER
        // =====================================================

        try {

            boolean removido =
                    repositorio.removeIf(
                            p -> p.id.equals(id)
                    );

            if (removido) {

                aplicarFiltro();

                JOptionPane.showMessageDialog(
                        this,
                        "PANC removida com sucesso!",
                        "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE
                );

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Erro: não foi possível remover a PANC.",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao remover a PANC:\n"
                            + ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================
    // CARREGAR TABELA
    // =========================================================

    private void carregarTabela(
            List<Planta> lista) {

        modeloTabela.setRowCount(0);

        for (Planta p : lista) {

            modeloTabela.addRow(
                    new Object[]{
                            p.id,
                            p.nomePopular,
                            p.nomeCientifico,
                            p.categoria,
                            p.regiao,
                            p.beneficios,
                            p.modoConsumo,
                            p.tipoUso,
                            p.dataCadastro
                    }
            );
        }

        labelContador.setText(
                "Mostrando "
                        + lista.size()
                        + " de "
                        + repositorio.size()
                        + " registros"
        );
    }

    // =========================================================
    // FILTRO
    // =========================================================

    private void aplicarFiltro() {

        String busca =
                campoBusca
                        .getText()
                        .trim()
                        .toLowerCase();

        String categoria =
                (String) comboCategoria
                        .getSelectedItem();

        String regiao =
                (String) comboRegiao
                        .getSelectedItem();

        String tipoUso =
                (String) comboTipoUso
                        .getSelectedItem();

        List<Planta> filtradas =
                new ArrayList<>();

        for (Planta p : repositorio) {

            boolean bateBusca =
                    busca.isEmpty()
                            || p.nomePopular
                            .toLowerCase()
                            .contains(busca)
                            || p.nomeCientifico
                            .toLowerCase()
                            .contains(busca);

            boolean bateCategoria =
                    categoria.equals("Todas")
                            || p.categoria
                            .equals(categoria);

            boolean bateRegiao =
                    regiao.equals("Todas")
                            || p.regiao
                            .equals(regiao);

            boolean bateTipoUso =
                    tipoUso.equals("Todos")
                            || p.tipoUso
                            .equals(tipoUso);

            if (bateBusca
                    && bateCategoria
                    && bateRegiao
                    && bateTipoUso) {

                filtradas.add(p);
            }
        }

        carregarTabela(filtradas);

        // Mensagem de confirmação da ação
        JOptionPane.showMessageDialog(
                this,
                "Filtro aplicado com sucesso.\n"
                        + filtradas.size()
                        + " registro(s) encontrado(s).",
                "Filtro",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // =========================================================
    // EXPORTAR CSV
    // =========================================================

    private void exportarCsv() {

        JFileChooser chooser =
                new JFileChooser();

        chooser.setSelectedFile(
                new File(
                        "relatorio_pancs.csv"
                )
        );

        int resultado =
                chooser.showSaveDialog(this);

        if (resultado != JFileChooser.APPROVE_OPTION) {

            JOptionPane.showMessageDialog(
                    this,
                    "Exportação cancelada.",
                    "Exportação",
                    JOptionPane.INFORMATION_MESSAGE
            );

            return;
        }

        File arquivo =
                chooser.getSelectedFile();

        try (
                FileWriter writer =
                        new FileWriter(arquivo)
        ) {

            writer.write(
                    String.join(
                            ";",
                            colunas
                    )
                            + "\n"
            );

            for (
                    int i = 0;
                    i < modeloTabela.getRowCount();
                    i++
            ) {

                StringBuilder linha =
                        new StringBuilder();

                for (
                        int j = 0;
                        j < colunas.length;
                        j++
                ) {

                    linha.append(
                            modeloTabela
                                    .getValueAt(
                                            i,
                                            j
                                    )
                    );

                    if (
                            j
                                    < colunas.length
                                    - 1
                    ) {

                        linha.append(";");
                    }
                }

                writer.write(
                        linha.toString()
                                + "\n"
                );
            }

            JOptionPane.showMessageDialog(
                    this,
                    "Arquivo exportado com sucesso!\n"
                            + "Abra no Excel para visualizar.",
                    "Exportação",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (IOException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao exportar:\n"
                            + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================
    // IMPRIMIR
    // =========================================================

    private void imprimirTabela() {

        try {

            boolean impresso =
                    tabela.print();

            if (impresso) {

                JOptionPane.showMessageDialog(
                        this,
                        "Impressão realizada com sucesso!",
                        "Impressão",
                        JOptionPane.INFORMATION_MESSAGE
                );

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Impressão cancelada.",
                        "Impressão",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }

        } catch (PrinterException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao imprimir:\n"
                            + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================
    // ESTILIZAR BOTÃO PRIMÁRIO
    // =========================================================

    private void estilizarBotaoPrimario(
            JButton b) {

        b.setBackground(VERDE);

        b.setForeground(Color.WHITE);

        b.setFocusPainted(false);
    }

    // =========================================================
    // ESTILIZAR BOTÃO SOBRE FUNDO VERDE
    // =========================================================

    private void estilizarBotaoSobreVerde(
            JButton b) {

        b.setBackground(Color.WHITE);

        b.setForeground(VERDE);

        b.setFocusPainted(false);

        b.setBorder(
                BorderFactory.createEmptyBorder(
                        6, 14, 6, 14
                )
        );
    }

    // =========================================================
    // MAIN
    // =========================================================

    public static void main(
            String[] args) {

        SwingUtilities.invokeLater(
                () -> {

                    RelatorioPancs tela =
                            new RelatorioPancs();

                    tela.setVisible(true);
                }
        );
    }
}