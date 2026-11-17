package telas;

import DAO.Conexao;
import DAO.PancDAO;
import negocio.LogAlteracoes;
import negocio.PANC;
import negocio.Sintoma;
import negocio.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PancsListPanel extends JPanel {

    private JTable tabela;
    private DefaultTableModel model;
    private JTextField txtBuscaSintoma;
    private Usuario usuarioLogado;
    private MainFrame parentFrame;

    private List<PANC> listaPancsAtual = new ArrayList<>();

    public PancsListPanel(MainFrame parentFrame, Usuario usuario) {
        this.parentFrame = parentFrame;
        this.usuarioLogado = usuario;

        setLayout(new BorderLayout(20, 20));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

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
        btnBuscar.addActionListener(e -> buscarPorSintomaBD());
        txtBuscaSintoma.addActionListener(e -> buscarPorSintomaBD());

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
        pnlEsquerda.add(Box.createHorizontalStrut(10));
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
            btnNovo.addActionListener(e -> cadastrarNovaPanc());

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
        tabela.getColumnModel().getColumn(1).setPreferredWidth(150);
        tabela.getColumnModel().getColumn(2).setPreferredWidth(130);

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
            PANC pancSelecionada = listaPancsAtual.get(linha);
            parentFrame.exibirDetalhesPanc(pancSelecionada);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Selecione uma PANC para ver os detalhes.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void cadastrarNovaPanc() {
        PancCadastroDialog dialog = new PancCadastroDialog(parentFrame, null);
        dialog.setVisible(true);

        if (dialog.isConfirmado()) {
            try (Connection conn = Conexao.getConexao()) {
                PancDAO dao = new PancDAO(conn);
                dao.cadastrar(dialog.getPanc(), usuarioLogado.getId());
                LogAlteracoes.registrar(usuarioLogado, LogAlteracoes.CADASTRO, LogAlteracoes.TIPO_PANC,
                        dialog.getPanc().getId(), dialog.getPanc().getNome());
                JOptionPane.showMessageDialog(this, "PANC cadastrada com sucesso!");
                carregarDadosDoBanco();
            } catch (SQLException e) {
                if (Conexao.isNomeDuplicado(e)) {
                    JOptionPane.showMessageDialog(this, "Já existe uma PANC cadastrada com o nome '" + dialog.getPanc().getNome() + "'.", "Nome duplicado", JOptionPane.WARNING_MESSAGE);
                } else {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Erro ao cadastrar PANC: " + e.getMessage(), "Erro BD", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void editarPancSelecionada() {
        int linhaSelecionada = tabela.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma PANC na tabela para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        PANC pancSelecionada = listaPancsAtual.get(linhaSelecionada);
        PancCadastroDialog dialog = new PancCadastroDialog(parentFrame, pancSelecionada);
        dialog.setVisible(true);

        if (dialog.isConfirmado()) {
            try (Connection conn = Conexao.getConexao()) {
                PancDAO dao = new PancDAO(conn);
                dao.editar(dialog.getPanc(), usuarioLogado.getId());
                LogAlteracoes.registrar(usuarioLogado, LogAlteracoes.EDICAO, LogAlteracoes.TIPO_PANC,
                        dialog.getPanc().getId(), dialog.getPanc().getNome());
                JOptionPane.showMessageDialog(this, "PANC atualizada com sucesso!");
                carregarDadosDoBanco();
            } catch (SQLException e) {
                if (Conexao.isNomeDuplicado(e)) {
                    JOptionPane.showMessageDialog(this, "Já existe outra PANC cadastrada com o nome '" + dialog.getPanc().getNome() + "'.", "Nome duplicado", JOptionPane.WARNING_MESSAGE);
                } else {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Erro ao atualizar PANC: " + e.getMessage(), "Erro BD", JOptionPane.ERROR_MESSAGE);
                }
                // o dialog altera o objeto em memoria; recarrega para voltar ao que esta no banco
                carregarDadosDoBanco();
            }
        }
    }

    private void excluirPancSelecionada() {
        int linhaSelecionada = tabela.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma PANC na tabela para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        PANC pancSelecionada = listaPancsAtual.get(linhaSelecionada);

        int opcao = JOptionPane.showConfirmDialog(
                this,
                "Tem certeza que deseja excluir a PANC '" + pancSelecionada.getNome() + "'?",
                "Confirmar Exclusão",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (opcao == JOptionPane.YES_OPTION) {
            try (Connection conn = Conexao.getConexao()) {
                PancDAO dao = new PancDAO(conn);
                dao.remover(pancSelecionada.getId());
                LogAlteracoes.registrar(usuarioLogado, LogAlteracoes.EXCLUSAO, LogAlteracoes.TIPO_PANC,
                        pancSelecionada.getId(), pancSelecionada.getNome());
                JOptionPane.showMessageDialog(this, "PANC excluída com sucesso!");
                carregarDadosDoBanco();
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao excluir PANC: " + e.getMessage(), "Erro BD", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void carregarDadosDoBanco() {
        try (Connection conn = Conexao.getConexao()) {
            PancDAO dao = new PancDAO(conn);
            listaPancsAtual = dao.listarTodas();
            preencherTabela(listaPancsAtual);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar PANCs do banco: " + e.getMessage(), "Erro BD", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarPorSintomaBD() {
        String termo = txtBuscaSintoma.getText().trim();
        if (termo.isEmpty()) {
            carregarDadosDoBanco();
            return;
        }

        try (Connection conn = Conexao.getConexao()) {
            PancDAO dao = new PancDAO(conn);
            listaPancsAtual = dao.buscarPorSintoma(termo);
            preencherTabela(listaPancsAtual);

            if (listaPancsAtual.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nenhuma PANC encontrada para o sintoma: " + termo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao buscar PANCs: " + e.getMessage(), "Erro BD", JOptionPane.ERROR_MESSAGE);
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