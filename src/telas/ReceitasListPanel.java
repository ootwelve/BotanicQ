package view;

import model.Receita;
import model.Usuario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class ReceitasListPanel extends JPanel {

    private MainFrame parentFrame;
    private Usuario usuarioLogado;

    private JTextField txtBusca;
    private JPanel pnlGridCards;
    private List<Receita> listaReceitasMock = new ArrayList<>();
    
    private JPanel cardSelecionado = null;
    private Receita receitaSelecionada = null;

    private static final Color VERDE_TEXTO = new Color(34, 112, 62);
    private static final Color COR_SELECAO = new Color(200, 230, 201);

    public ReceitasListPanel(MainFrame parentFrame, Usuario usuario) {
        this.parentFrame = parentFrame;
        this.usuarioLogado = usuario;

        setLayout(new BorderLayout(20, 20));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(25, 25, 25, 25));

        add(criarTopo(), BorderLayout.NORTH);

        add(criarContainerGrid(), BorderLayout.CENTER);

        carregarDadosMock();
    }

    private JPanel criarTopo() {
        JPanel topoContainer = new JPanel();
        topoContainer.setLayout(new BoxLayout(topoContainer, BoxLayout.Y_AXIS));
        topoContainer.setOpaque(false);

        JPanel pnlTitulo = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlTitulo.setOpaque(false);

        JLabel lblTitulo = new JLabel("Receitas Culinárias com PANCs");
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
        txtBusca.addActionListener(e -> filtrarReceitas());

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setPreferredSize(new Dimension(100, 36));
        btnBuscar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBuscar.addActionListener(e -> filtrarReceitas());

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

            JButton btnNovo = new JButton("+ Cadastrar Receita");
            btnNovo.setFont(new Font("SansSerif", Font.BOLD, 13));
            btnNovo.setPreferredSize(new Dimension(180, 36));
            btnNovo.setBackground(MainFrame.VERDE_ESCURO);
            btnNovo.setForeground(Color.WHITE);
            btnNovo.setFocusPainted(false);
            btnNovo.setBorderPainted(false);
            btnNovo.setOpaque(true);
            btnNovo.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnNovo.addActionListener(e -> cadastrarNovaReceita());

            JButton btnEditar = new JButton("✏️ Editar");
            btnEditar.setFont(new Font("SansSerif", Font.BOLD, 13));
            btnEditar.setPreferredSize(new Dimension(100, 36));
            btnEditar.setBackground(new Color(220, 140, 40));
            btnEditar.setForeground(Color.WHITE);
            btnEditar.setFocusPainted(false);
            btnEditar.setBorderPainted(false);
            btnEditar.setOpaque(true);
            btnEditar.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnEditar.addActionListener(e -> editarReceitaSelecionada());

            JButton btnExcluir = new JButton("🗑️ Excluir");
            btnExcluir.setFont(new Font("SansSerif", Font.BOLD, 13));
            btnExcluir.setPreferredSize(new Dimension(100, 36));
            btnExcluir.setBackground(new Color(200, 50, 50));
            btnExcluir.setForeground(Color.WHITE);
            btnExcluir.setFocusPainted(false);
            btnExcluir.setBorderPainted(false);
            btnExcluir.setOpaque(true);
            btnExcluir.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnExcluir.addActionListener(e -> excluirReceitaSelecionada());

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

    private JComponent criarContainerGrid() {
        pnlGridCards = new JPanel(new GridLayout(0, 4, 18, 18));
        pnlGridCards.setBackground(Color.WHITE);

        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setBackground(Color.WHITE);
        wrapperPanel.add(pnlGridCards, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(wrapperPanel);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        return scrollPane;
    }

    private void renderizarGrid(List<Receita> receitas) {
        pnlGridCards.removeAll();
        cardSelecionado = null;
        receitaSelecionada = null;

        for (Receita r : receitas) {
            JPanel card = criarCardReceita(r);
            pnlGridCards.add(card);
        }

        pnlGridCards.revalidate();
        pnlGridCards.repaint();
    }

    private JPanel criarCardReceita(Receita receita) {
        JPanel card = new JPanel(new BorderLayout());
        card.setPreferredSize(new Dimension(180, 200));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 225, 220), 1, true),
                new EmptyBorder(8, 8, 8, 8)
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel lblImagem = new JLabel();
        lblImagem.setHorizontalAlignment(SwingConstants.CENTER);
        lblImagem.setOpaque(true);
        lblImagem.setBackground(new Color(240, 243, 240));

        if (receita.getSrc_imagem() != null && !receita.getSrc_imagem().isEmpty()) {
            ImageIcon icon = new ImageIcon(receita.getSrc_imagem());
            Image img = icon.getImage().getScaledInstance(180, 130, Image.SCALE_SMOOTH);
            lblImagem.setIcon(new ImageIcon(img));
        } else {
            lblImagem.setText("📷 Sem Imagem");
            lblImagem.setFont(new Font("SansSerif", Font.PLAIN, 12));
            lblImagem.setForeground(Color.GRAY);
        }

        JLabel lblTitulo = new JLabel("<html><center>" + receita.getTitulo() + "</center></html>");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblTitulo.setForeground(new Color(40, 40, 40));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setPreferredSize(new Dimension(180, 45));

        card.add(lblImagem, BorderLayout.CENTER);
        card.add(lblTitulo, BorderLayout.SOUTH);

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selecionarCard(card, receita);

                if (e.getClickCount() == 2) {
                    abrirDetalhesSelecionado();
                }
            }
        });

        return card;
    }

    private void selecionarCard(JPanel card, Receita receita) {
        if (cardSelecionado != null) {
            cardSelecionado.setBackground(Color.WHITE);
            cardSelecionado.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(new Color(220, 225, 220), 1, true),
                    new EmptyBorder(8, 8, 8, 8)
            ));
        }

        cardSelecionado = card;
        receitaSelecionada = receita;
        card.setBackground(COR_SELECAO);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(VERDE_TEXTO, 2, true),
                new EmptyBorder(7, 7, 7, 7)
        ));
    }

    private void abrirDetalhesSelecionado() {
        if (receitaSelecionada != null) {
            parentFrame.exibirDetalhesReceita(receitaSelecionada);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Selecione uma receita na grade para ver os detalhes.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void cadastrarNovaReceita() {
    	// TEMPORÁRIO
        JOptionPane.showMessageDialog(this, "Abre a tela de cadastro de receita.");
    }

    private void editarReceitaSelecionada() {
        if (receitaSelecionada == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma receita para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // TEMPORÁRIO
        JOptionPane.showMessageDialog(this, "Abre a tela de edição para: " + receitaSelecionada.getTitulo());
    }

    private void excluirReceitaSelecionada() {
        if (receitaSelecionada == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma receita para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int opcao = JOptionPane.showConfirmDialog(
                this,
                "Tem certeza que deseja excluir a receita '" + receitaSelecionada.getTitulo() + "'?",
                "Confirmar Exclusão",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (opcao == JOptionPane.YES_OPTION) {
            listaReceitasMock.remove(receitaSelecionada);
            renderizarGrid(listaReceitasMock);
            JOptionPane.showMessageDialog(this, "Receita excluída com sucesso!");
        }
    }

    private void carregarDadosMock() {
        listaReceitasMock.clear();

        Receita r1 = new Receita();
        r1.setId(1);
        r1.setTitulo("Refogado de Ora-pro-nóbis");
        r1.setSrc_imagem("C:/Users/henri/Downloads/carro.jpg");
        
        Receita r2 = new Receita();
        r2.setId(2);
        r2.setTitulo("Peixinho Frito Empanado");

        Receita r3 = new Receita();
        r3.setId(3);
        r3.setTitulo("Geleia de Amora Silvestre");

        Receita r4 = new Receita();
        r4.setId(4);
        r4.setTitulo("Salada de Serralha e Tomate");

        listaReceitasMock.add(r1);
        listaReceitasMock.add(r2);
        listaReceitasMock.add(r3);
        listaReceitasMock.add(r4);

        renderizarGrid(listaReceitasMock);
    }

    private void filtrarReceitas() {
        String termo = txtBusca.getText().toLowerCase().trim();
        if (termo.isEmpty()) {
            renderizarGrid(listaReceitasMock);
            return;
        }

        List<Receita> filtradas = new ArrayList<>();
        for (Receita r : listaReceitasMock) {
            if (r.getTitulo().toLowerCase().contains(termo)) {
                filtradas.add(r);
            }
        }
        renderizarGrid(filtradas);
    }
}