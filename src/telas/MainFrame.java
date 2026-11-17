package telas;

import negocio.PANC;
import negocio.Receita;
import negocio.Sintoma;
import negocio.Usuario;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainFrame extends JFrame {
    private JPanel painelConteudo;
    private CardLayout cardLayout;
    private Usuario usuarioLogado;

    public static final Color VERDE_ESCURO = new Color(34, 112, 62);
    public static final Color VERDE_MEDIO = new Color(46, 139, 87);
    public static final Color VERDE_SELECAO = new Color(225, 245, 230);
    public static final Color BG_MENU = new Color(242, 244, 243);

    public MainFrame(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("O MainFrame exige um usuário autenticado.");
        }
        this.usuarioLogado = usuario;

        String perfilStr = (this.usuarioLogado.getAcesso() == 1) ? "Administrador" : "Consulta";
        setTitle("BotanicQ - Conta: " + this.usuarioLogado.getNome() + " (" + perfilStr + ")");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 720);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        add(criarMenuLateral(), BorderLayout.WEST);

        cardLayout = new CardLayout();
        painelConteudo = new JPanel(cardLayout);

        add(painelConteudo, BorderLayout.CENTER);
        
        inicializarPaineis();
    }

    private JPanel criarMenuLateral() {
        JPanel menu = new JPanel();
        menu.setLayout(new BorderLayout());
        menu.setBackground(BG_MENU);
        menu.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(210, 215, 210)),
            BorderFactory.createEmptyBorder(25, 15, 20, 15)
        ));
        menu.setPreferredSize(new Dimension(240, 0));

        JPanel pnlTopo = new JPanel();
        pnlTopo.setLayout(new BoxLayout(pnlTopo, BoxLayout.Y_AXIS));
        pnlTopo.setBackground(BG_MENU);

        JLabel lblLogo = new JLabel("❀🌿 BotanicQ");
        lblLogo.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblLogo.setForeground(VERDE_ESCURO);
        lblLogo.setAlignmentX(Component.LEFT_ALIGNMENT);
        pnlTopo.add(lblLogo);

        pnlTopo.add(Box.createRigidArea(new Dimension(0, 35)));

        pnlTopo.add(criarBotaoMenu("Receitas", "RECEITAS"));
        pnlTopo.add(Box.createRigidArea(new Dimension(0, 12)));
        pnlTopo.add(criarBotaoMenu("PANCs", "PANCS"));
        pnlTopo.add(Box.createRigidArea(new Dimension(0, 12)));
        pnlTopo.add(criarBotaoMenu("Sintomas", "SINTOMAS"));

        if (usuarioLogado.getAcesso() == 1) {
            pnlTopo.add(Box.createRigidArea(new Dimension(0, 12)));
            pnlTopo.add(criarBotaoMenu("Gerenciar Usuários", "USUARIOS"));
        }

        menu.add(pnlTopo, BorderLayout.NORTH);

        JPanel pnlRodape = new JPanel();
        pnlRodape.setLayout(new BoxLayout(pnlRodape, BoxLayout.Y_AXIS));
        pnlRodape.setBackground(BG_MENU);

        JButton btnLogout = new JButton("Encerrar Sessão");
        btnLogout.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnLogout.setForeground(new Color(180, 40, 40));
        btnLogout.setBackground(Color.WHITE);
        btnLogout.setFocusPainted(false);
        btnLogout.setHorizontalAlignment(SwingConstants.LEFT);
        btnLogout.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnLogout.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnLogout.setPreferredSize(new Dimension(210, 40));
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnLogout.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 180, 180), 1),
            BorderFactory.createEmptyBorder(0, 15, 0, 10)
        ));

        btnLogout.addActionListener(e -> realizarLogout());
        pnlRodape.add(btnLogout);

        menu.add(pnlRodape, BorderLayout.SOUTH);

        return menu;
    }

    private JButton criarBotaoMenu(String texto, String cardName) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setForeground(new Color(60, 60, 60));
        btn.setBackground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(true);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btn.setPreferredSize(new Dimension(210, 45));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 205, 200), 1),
            BorderFactory.createEmptyBorder(0, 15, 0, 10)
        ));

        btn.addActionListener(e -> cardLayout.show(painelConteudo, cardName));
        return btn;
    }
    
    private void inicializarPaineis() {
        painelConteudo.setLayout(cardLayout);

        painelConteudo.add(new ReceitasListPanel(this, usuarioLogado), "RECEITAS");
        painelConteudo.add(new PancsListPanel(this, usuarioLogado), "PANCS");
        painelConteudo.add(new SintomasListPanel(this, usuarioLogado), "SINTOMAS");
        
        if (usuarioLogado.getAcesso() == 1) {
            painelConteudo.add(new UsuariosListPanel(this, usuarioLogado), "USUARIOS");
        }

        cardLayout.show(painelConteudo, "RECEITAS");
    }

    private void realizarLogout() {
        int resposta = JOptionPane.showConfirmDialog(
            this, 
            "Deseja realmente encerrar a sessão e voltar ao login?", 
            "Confirmação de Saída", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.QUESTION_MESSAGE
        );

        if (resposta == JOptionPane.YES_OPTION) {
            dispose();
            new LoginFrame().setVisible(true);
        }
    }
    
    public void exibirDetalhesPanc(PANC panc) {
        PancDetailPanel detalhePanel = new PancDetailPanel(this, panc, () -> {
            cardLayout.show(painelConteudo, "PANCS");
        });

        painelConteudo.add(detalhePanel, "PANC_DETALHES");
        cardLayout.show(painelConteudo, "PANC_DETALHES");
    }
    
    public void exibirDetalhesSintoma(Sintoma sintoma) {
        SintomaDetailPanel detalhePanel = new SintomaDetailPanel(this, sintoma, () -> {
            cardLayout.show(painelConteudo, "SINTOMAS");
        });

        painelConteudo.add(detalhePanel, "SINTOMA_DETALHES");
        cardLayout.show(painelConteudo, "SINTOMA_DETALHES");
    }
    
    public void exibirDetalhesReceita(Receita receita) {
        ReceitaDetailPanel detalhePanel = new ReceitaDetailPanel(this, receita, () -> {
            cardLayout.show(painelConteudo, "RECEITAS");
        });

        painelConteudo.add(detalhePanel, "RECEITA_DETALHES");
        cardLayout.show(painelConteudo, "RECEITA_DETALHES");
    }

    public void exibirPainel(JPanel novoPainel) {
        painelConteudo.removeAll();
        painelConteudo.add(novoPainel, BorderLayout.CENTER);
        painelConteudo.revalidate();
        painelConteudo.repaint();
    }
}