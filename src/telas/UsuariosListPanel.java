package telas;

import DAO.Conexao;
import DAO.UsuarioDAO;
import negocio.LogAlteracoes;
import negocio.Usuario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class UsuariosListPanel extends JPanel {
    private MainFrame parentFrame;
    private Usuario usuarioLogado;
    
    private JTable tabela;
    private DefaultTableModel tableModel;
    private List<Usuario> listaUsuarios;

    private static final Color VERDE_TEXTO = new Color(34, 112, 62);
    private static final Color VERDE_CABECALHO = new Color(34, 112, 62);

    public UsuariosListPanel(MainFrame parentFrame, Usuario usuario) {
        this.parentFrame = parentFrame;
        this.usuarioLogado = usuario;

        setLayout(new BorderLayout(20, 20));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(25, 25, 25, 25));

        add(criarTopo(), BorderLayout.NORTH);
        add(criarTabela(), BorderLayout.CENTER);
        add(criarRodapeAviso(), BorderLayout.SOUTH);

        carregarDados();
    }

    private JPanel criarTopo() {
        JPanel topoContainer = new JPanel();
        topoContainer.setLayout(new BoxLayout(topoContainer, BoxLayout.Y_AXIS));
        topoContainer.setOpaque(false);

        JPanel pnlTitulo = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlTitulo.setOpaque(false);

        JLabel lblTitulo = new JLabel("Gerenciamento de Utilizadores");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblTitulo.setForeground(VERDE_TEXTO);
        pnlTitulo.add(lblTitulo);

        JPanel pnlAcoes = new JPanel(new BorderLayout(10, 0));
        pnlAcoes.setOpaque(false);
        pnlAcoes.setBorder(BorderFactory.createEmptyBorder(15, 0, 5, 0));

        JPanel pnlEsquerda = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        pnlEsquerda.setOpaque(false);

        JPanel pnlBotoesAdmin = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        pnlBotoesAdmin.setOpaque(false);

        if (usuarioLogado != null && usuarioLogado.getAcesso() == 1) {
            JButton btnNovo = new JButton("+ Novo Utilizador");
            btnNovo.setFont(new Font("SansSerif", Font.BOLD, 13));
            btnNovo.setPreferredSize(new Dimension(160, 36));
            btnNovo.setBackground(MainFrame.VERDE_ESCURO);
            btnNovo.setForeground(Color.WHITE);
            btnNovo.setFocusPainted(false);
            btnNovo.setBorderPainted(false);
            btnNovo.setOpaque(true);
            btnNovo.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnNovo.addActionListener(e -> abrirDialogCadastro());

            JButton btnAlterarSenha = new JButton("🔑 Alterar Senha");
            btnAlterarSenha.setFont(new Font("SansSerif", Font.BOLD, 13));
            btnAlterarSenha.setPreferredSize(new Dimension(150, 36));
            btnAlterarSenha.setBackground(new Color(60, 130, 180));
            btnAlterarSenha.setForeground(Color.WHITE);
            btnAlterarSenha.setFocusPainted(false);
            btnAlterarSenha.setBorderPainted(false);
            btnAlterarSenha.setOpaque(true);
            btnAlterarSenha.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnAlterarSenha.addActionListener(e -> abrirDialogAlterarSenha());

            pnlBotoesAdmin.add(btnNovo);
            pnlBotoesAdmin.add(btnAlterarSenha);

            // RF19: abre o log de alteracoes (botao "vazado" para nao competir com as acoes)
            JButton btnLog = new JButton("Log de Alterações");
            btnLog.setFont(new Font("SansSerif", Font.BOLD, 13));
            btnLog.setPreferredSize(new Dimension(170, 36));
            btnLog.setBackground(Color.WHITE);
            btnLog.setForeground(MainFrame.VERDE_ESCURO);
            btnLog.setBorder(BorderFactory.createLineBorder(MainFrame.VERDE_ESCURO, 1));
            btnLog.setFocusPainted(false);
            btnLog.setOpaque(true);
            btnLog.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnLog.addActionListener(e -> new LogAlteracoesDialog(parentFrame).setVisible(true));

            pnlEsquerda.add(btnLog);
        }

        pnlAcoes.add(pnlEsquerda, BorderLayout.WEST);
        pnlAcoes.add(pnlBotoesAdmin, BorderLayout.EAST);

        topoContainer.add(pnlTitulo);
        topoContainer.add(pnlAcoes);

        return topoContainer;
    }

    private JScrollPane criarTabela() {
        tableModel = new DefaultTableModel(new String[]{"ID", "Nome do Utilizador", "Nível de Acesso"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabela = new JTable(tableModel);
        tabela.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tabela.setRowHeight(32);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.setGridColor(new Color(230, 235, 230));

        tabela.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        tabela.getTableHeader().setBackground(VERDE_CABECALHO);
        tabela.getTableHeader().setForeground(Color.WHITE);
        tabela.getTableHeader().setOpaque(false);

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        tabela.setDefaultRenderer(Object.class, renderer);

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setBorder(new LineBorder(new Color(220, 225, 220), 1, true));
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        return scroll;
    }

    private JPanel criarRodapeAviso() {
        JPanel pnlRodape = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        pnlRodape.setOpaque(false);

        JLabel lblAviso = new JLabel("<html><i>* Exclusão de usuários só podem ser feitas diretamente no banco de dados.</i></html>");
        lblAviso.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblAviso.setForeground(Color.GRAY);

        pnlRodape.add(lblAviso);
        return pnlRodape;
    }

    private void carregarDados() {
        tableModel.setRowCount(0);
        try (Connection conn = Conexao.getConexao()) {
            UsuarioDAO dao = new UsuarioDAO(conn);
            listaUsuarios = dao.listar();

            for (Usuario u : listaUsuarios) {
                String perfil = (u.getAcesso() == 1) ? "Administrador" : "Consulta";
                tableModel.addRow(new Object[]{u.getId(), u.getNome(), perfil});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar utilizadores: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirDialogCadastro() {
        UsuarioCadastroDialog dialog = new UsuarioCadastroDialog(parentFrame);
        dialog.setVisible(true);

        if (dialog.isConfirmado()) {
            try (Connection conn = Conexao.getConexao()) {
                UsuarioDAO dao = new UsuarioDAO(conn);
                dao.cadastrar(dialog.getUsuario());
                LogAlteracoes.registrar(usuarioLogado, LogAlteracoes.CADASTRO, LogAlteracoes.TIPO_USUARIO,
                        dialog.getUsuario().getId(), dialog.getUsuario().getNome());
                
                carregarDados();
                JOptionPane.showMessageDialog(this, "Utilizador cadastrado com sucesso!");
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao salvar utilizador: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void abrirDialogAlterarSenha() {
        int linhaSelecionada = tabela.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um utilizador na tabela para alterar a senha.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Usuario usuarioSelecionado = listaUsuarios.get(linhaSelecionada);

        UsuarioAlterarSenhaDialog dialog = new UsuarioAlterarSenhaDialog(parentFrame, usuarioSelecionado);
        dialog.setVisible(true);

        if (dialog.isConfirmado()) {
            try (Connection conn = Conexao.getConexao()) {
                UsuarioDAO dao = new UsuarioDAO(conn);
                dao.alterarSenha(usuarioSelecionado.getId(), dialog.getNovaSenha());
                LogAlteracoes.registrar(usuarioLogado, LogAlteracoes.ALTERACAO_SENHA, LogAlteracoes.TIPO_USUARIO,
                        usuarioSelecionado.getId(), usuarioSelecionado.getNome());
                
                JOptionPane.showMessageDialog(this, "Senha alterada com sucesso para o utilizador: " + usuarioSelecionado.getNome());
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao alterar senha: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}