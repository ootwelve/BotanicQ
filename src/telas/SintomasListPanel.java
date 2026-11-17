package telas;

import DAO.Conexao;
import DAO.SintomaDAO;
import negocio.LogAlteracoes;
import negocio.Sintoma;
import negocio.Usuario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SintomasListPanel extends JPanel {

    private MainFrame parentFrame;
    private Usuario usuarioLogado;

    private JTable tabela;
    private DefaultTableModel tableModel;
    private JTextField txtBusca;

    private List<Sintoma> listaSintomasCache = new ArrayList<>();
    private List<Sintoma> listaSintomasExibidos = new ArrayList<>();

    private static final Color VERDE_TEXTO = new Color(34, 112, 62);

    public SintomasListPanel(MainFrame parentFrame, Usuario usuario) {
        this.parentFrame = parentFrame;
        this.usuarioLogado = usuario;

        setLayout(new BorderLayout(20, 20));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(25, 25, 25, 25));

        add(criarTopo(), BorderLayout.NORTH);
        add(criarTabela(), BorderLayout.CENTER);

        carregarDadosDoBanco();
    }

    private JPanel criarTopo() {
        JPanel topoContainer = new JPanel();
        topoContainer.setLayout(new BoxLayout(topoContainer, BoxLayout.Y_AXIS));
        topoContainer.setOpaque(false);

        JPanel pnlTitulo = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlTitulo.setOpaque(false);

        JLabel lblTitulo = new JLabel("Sintomas de Saúde");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblTitulo.setForeground(VERDE_TEXTO);
        pnlTitulo.add(lblTitulo);

        JPanel pnlAcoes = new JPanel(new BorderLayout(10, 0));
        pnlAcoes.setOpaque(false);
        pnlAcoes.setBorder(BorderFactory.createEmptyBorder(15, 0, 5, 0));

        JPanel pnlEsquerda = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        pnlEsquerda.setOpaque(false);

        JLabel lblFiltrar = new JLabel("Filtrar:");
        lblFiltrar.setFont(new Font("SansSerif", Font.BOLD, 13));

        txtBusca = new JTextField(15);
        txtBusca.setPreferredSize(new Dimension(180, 36));
        txtBusca.addActionListener(e -> filtrarSintomas());

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setPreferredSize(new Dimension(100, 36));
        btnBuscar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBuscar.addActionListener(e -> filtrarSintomas());

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

        pnlEsquerda.add(lblFiltrar);
        pnlEsquerda.add(txtBusca);
        pnlEsquerda.add(btnBuscar);
        pnlEsquerda.add(Box.createHorizontalStrut(10));
        pnlEsquerda.add(btnDetalhes);

        JPanel pnlBotoesAdmin = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        pnlBotoesAdmin.setOpaque(false);
        pnlBotoesAdmin.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

        if (usuarioLogado != null && usuarioLogado.getAcesso() == 1) {

            JButton btnNovo = new JButton("+ Cadastrar Sintoma");
            btnNovo.setFont(new Font("SansSerif", Font.BOLD, 13));
            btnNovo.setPreferredSize(new Dimension(180, 36));
            btnNovo.setBackground(MainFrame.VERDE_ESCURO);
            btnNovo.setForeground(Color.WHITE);
            btnNovo.setFocusPainted(false);
            btnNovo.setBorderPainted(false);
            btnNovo.setOpaque(true);
            btnNovo.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnNovo.addActionListener(e -> cadastrarNovoSintoma());

            JButton btnEditar = new JButton("✏️ Editar");
            btnEditar.setFont(new Font("SansSerif", Font.BOLD, 13));
            btnEditar.setPreferredSize(new Dimension(100, 36));
            btnEditar.setBackground(new Color(220, 140, 40));
            btnEditar.setForeground(Color.WHITE);
            btnEditar.setFocusPainted(false);
            btnEditar.setBorderPainted(false);
            btnEditar.setOpaque(true);
            btnEditar.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnEditar.addActionListener(e -> editarSintomaSelecionado());

            JButton btnExcluir = new JButton("🗑️ Excluir");
            btnExcluir.setFont(new Font("SansSerif", Font.BOLD, 13));
            btnExcluir.setPreferredSize(new Dimension(100, 36));
            btnExcluir.setBackground(new Color(200, 50, 50));
            btnExcluir.setForeground(Color.WHITE);
            btnExcluir.setFocusPainted(false);
            btnExcluir.setBorderPainted(false);
            btnExcluir.setOpaque(true);
            btnExcluir.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnExcluir.addActionListener(e -> excluirSintomaSelecionado());

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
        String[] colunas = {"ID", "Sintoma / Problema de Saúde", "Descrição"};

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

        for (int i = 0; i < tabela.getColumnCount(); i++) {
            tabela.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tabela.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);

        tabela.getColumnModel().getColumn(0).setPreferredWidth(60);
        tabela.getColumnModel().getColumn(0).setMaxWidth(80);
        tabela.getColumnModel().getColumn(1).setPreferredWidth(250);
        tabela.getColumnModel().getColumn(2).setPreferredWidth(500);

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
            Sintoma sintomaSelecionado = listaSintomasExibidos.get(linha);
            parentFrame.exibirDetalhesSintoma(sintomaSelecionado);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Selecione um sintoma para ver os detalhes.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void cadastrarNovoSintoma() {
        SintomaCadastroDialog dialog = new SintomaCadastroDialog(parentFrame);
        dialog.setVisible(true);

        if (dialog.isConfirmado()) {
            try (Connection conn = Conexao.getConexao()) {
                SintomaDAO dao = new SintomaDAO(conn);
                dao.cadastrar(dialog.getSintoma(), usuarioLogado.getId());
                LogAlteracoes.registrar(usuarioLogado, LogAlteracoes.CADASTRO, LogAlteracoes.TIPO_SINTOMA,
                        dialog.getSintoma().getId(), dialog.getSintoma().getNome());

                carregarDadosDoBanco();
                JOptionPane.showMessageDialog(this, "Sintoma cadastrado com sucesso!");
            } catch (SQLException e) {
                if (Conexao.isNomeDuplicado(e)) {
                    JOptionPane.showMessageDialog(this,
                            "Já existe um sintoma cadastrado com o nome '" + dialog.getSintoma().getNome() + "'.",
                            "Nome duplicado",
                            JOptionPane.WARNING_MESSAGE);
                } else {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this,
                            "Erro ao cadastrar sintoma no banco: " + e.getMessage(),
                            "Erro BD",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void editarSintomaSelecionado() {
        int linhaSelecionada = tabela.getSelectedRow();

        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um sintoma na tabela para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Sintoma sintomaSelecionado = listaSintomasExibidos.get(linhaSelecionada);
        SintomaCadastroDialog dialog = new SintomaCadastroDialog(parentFrame, sintomaSelecionado);
        dialog.setVisible(true);

        if (dialog.isConfirmado()) {
            try (Connection conn = Conexao.getConexao()) {
                SintomaDAO dao = new SintomaDAO(conn);
                dao.editar(dialog.getSintoma(), usuarioLogado.getId());
                LogAlteracoes.registrar(usuarioLogado, LogAlteracoes.EDICAO, LogAlteracoes.TIPO_SINTOMA,
                        dialog.getSintoma().getId(), dialog.getSintoma().getNome());

                carregarDadosDoBanco();
                JOptionPane.showMessageDialog(this, "Sintoma atualizado com sucesso!");
            } catch (SQLException e) {
                if (Conexao.isNomeDuplicado(e)) {
                    JOptionPane.showMessageDialog(this,
                            "Já existe outro sintoma cadastrado com o nome '" + dialog.getSintoma().getNome() + "'.",
                            "Nome duplicado",
                            JOptionPane.WARNING_MESSAGE);
                } else {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this,
                            "Erro ao atualizar sintoma no banco: " + e.getMessage(),
                            "Erro BD",
                            JOptionPane.ERROR_MESSAGE);
                }
                // o dialog altera o objeto em memoria; recarrega para voltar ao que esta no banco
                carregarDadosDoBanco();
            }
        }
    }

    private void excluirSintomaSelecionado() {
        int linhaSelecionada = tabela.getSelectedRow();

        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecione um sintoma na tabela para excluir.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Sintoma sintomaSelecionado = listaSintomasExibidos.get(linhaSelecionada);

        int opcao = JOptionPane.showConfirmDialog(
                this,
                "Tem certeza que deseja excluir o sintoma '" + sintomaSelecionado.getNome() + "'?",
                "Confirmar Exclusão",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (opcao == JOptionPane.YES_OPTION) {
            try (Connection conn = Conexao.getConexao()) {
                SintomaDAO dao = new SintomaDAO(conn);
                dao.remover(sintomaSelecionado.getId());
                LogAlteracoes.registrar(usuarioLogado, LogAlteracoes.EXCLUSAO, LogAlteracoes.TIPO_SINTOMA,
                        sintomaSelecionado.getId(), sintomaSelecionado.getNome());

                carregarDadosDoBanco();
                JOptionPane.showMessageDialog(this,
                        "Sintoma excluído com sucesso!",
                        "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Erro ao excluir sintoma no banco: " + e.getMessage(),
                        "Erro BD",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void carregarDadosDoBanco() {
        try (Connection conn = Conexao.getConexao()) {
            SintomaDAO dao = new SintomaDAO(conn);
            listaSintomasCache = dao.listar();
            
            // Se houver algo digitado no filtro, reaplica; caso contrário exibe tudo
            filtrarSintomas();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar sintomas do banco: " + e.getMessage(),
                    "Erro BD",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void preencherTabela(List<Sintoma> lista) {
        listaSintomasExibidos = lista;
        tableModel.setRowCount(0);
        for (Sintoma s : lista) {
            tableModel.addRow(new Object[]{
                    s.getId(),
                    s.getNome(),
                    s.getDesc()
            });
        }
    }

    private void filtrarSintomas() {
        String termo = txtBusca.getText().toLowerCase().trim();
        if (termo.isEmpty()) {
            preencherTabela(listaSintomasCache);
            return;
        }

        List<Sintoma> filtrados = new ArrayList<>();
        for (Sintoma s : listaSintomasCache) {
            if (s.getNome().toLowerCase().contains(termo) ||
                (s.getDesc() != null && s.getDesc().toLowerCase().contains(termo))) {
                filtrados.add(s);
            }
        }
        preencherTabela(filtrados);
    }
}