package view;

import model.PANC;
import model.Receita;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.List;

public class PancDetailPanel extends JPanel {

    private static final Color VERDE_TEXTO = new Color(34, 112, 62);
    private static final Color CINZA_BG = new Color(248, 249, 250);
    private static final Color BORDA_CINZA = new Color(220, 224, 230);

    public PancDetailPanel(JFrame parentFrame, PANC panc, Runnable onVoltarCallback) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 25, 20, 25));

        add(criarPainelTopo(panc, onVoltarCallback), BorderLayout.NORTH);

        JPanel pnlCorpo = new JPanel();
        pnlCorpo.setLayout(new BoxLayout(pnlCorpo, BoxLayout.Y_AXIS));
        pnlCorpo.setBackground(Color.WHITE);

        JPanel cardInfo = criarCardInformacoes(panc);
        cardInfo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel secaoReceitas = criarSecaoReceitas(panc);
        secaoReceitas.setAlignmentX(Component.LEFT_ALIGNMENT);

        pnlCorpo.add(cardInfo);
        pnlCorpo.add(Box.createVerticalStrut(25));
        pnlCorpo.add(secaoReceitas);

        JScrollPane scroll = new JScrollPane(pnlCorpo);
        scroll.setBorder(null);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(12);

        add(scroll, BorderLayout.CENTER);
    }

    private JPanel criarPainelTopo(PANC panc, Runnable onVoltarCallback) {
        JPanel pnlTopo = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        pnlTopo.setBackground(Color.WHITE);
        pnlTopo.setBorder(new EmptyBorder(0, 0, 20, 0));

        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnVoltar.setFocusPainted(false);
        btnVoltar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVoltar.addActionListener(e -> {
            if (onVoltarCallback != null) onVoltarCallback.run();
        });

        JLabel lblNome = new JLabel(panc.getNome());
        lblNome.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblNome.setForeground(VERDE_TEXTO);

        pnlTopo.add(btnVoltar);
        pnlTopo.add(lblNome);

        return pnlTopo;
    }

    private JPanel criarCardInformacoes(PANC panc) {
        JPanel card = new JPanel(new BorderLayout(0, 15));
        card.setBackground(CINZA_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDA_CINZA, 1, true),
                new EmptyBorder(15, 15, 15, 15)
        ));

        JPanel pnlFoto = new JPanel(new BorderLayout());
        pnlFoto.setPreferredSize(new Dimension(500, 320));
        pnlFoto.setBackground(Color.WHITE);
        pnlFoto.setBorder(new LineBorder(BORDA_CINZA, 1));

        JLabel lblFoto = new JLabel();
        lblFoto.setHorizontalAlignment(SwingConstants.CENTER);
        lblFoto.setVerticalAlignment(SwingConstants.CENTER);

        if (panc.getSrc_imagem() != null && !panc.getSrc_imagem().trim().isEmpty()) {
            java.io.File fileImg = new java.io.File(panc.getSrc_imagem());
            if (fileImg.exists()) {
                ImageIcon icon = new ImageIcon(panc.getSrc_imagem());
                Image img = icon.getImage().getScaledInstance(500, 320, Image.SCALE_SMOOTH);
                lblFoto.setIcon(new ImageIcon(img));
            } else {
                lblFoto.setText("📷 Imagem não encontrada: " + fileImg.getName());
                lblFoto.setFont(new Font("SansSerif", Font.PLAIN, 14));
                lblFoto.setForeground(Color.GRAY);
            }
        } else {
            lblFoto.setText("📷 Sem Imagem");
            lblFoto.setFont(new Font("SansSerif", Font.BOLD, 16));
            lblFoto.setForeground(Color.GRAY);
        }

        pnlFoto.add(lblFoto, BorderLayout.CENTER);

        JPanel pnlFotoWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        pnlFotoWrapper.setBackground(CINZA_BG);
        pnlFotoWrapper.add(pnlFoto);

        card.add(pnlFotoWrapper, BorderLayout.NORTH);

        JPanel pnlDados = new JPanel(new GridBagLayout());
        pnlDados.setBackground(CINZA_BG);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(8, 10, 8, 10);

        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 0.3; gbc.weighty = 0.0;
        pnlDados.add(criarBlocoInformacao("Origem Nativa", panc.getOrigem_nativa()), gbc);

        StringBuilder sintomasStr = new StringBuilder();
        if (panc.getSintomas_relacionados() != null && !panc.getSintomas_relacionados().isEmpty()) {
            for (int i = 0; i < panc.getSintomas_relacionados().size(); i++) {
                sintomasStr.append("• ").append(panc.getSintomas_relacionados().get(i).getNome());
                if (i < panc.getSintomas_relacionados().size() - 1) {
                    sintomasStr.append("\n");
                }
            }
        } else {
            sintomasStr.append("Nenhum sintoma ou uso vinculado.");
        }

        gbc.gridx = 1; gbc.gridy = 0;
        gbc.weightx = 0.7; gbc.weighty = 1.0;
        pnlDados.add(criarBlocoInformacao("Sintomas / Usos Medicinais", sintomasStr.toString()), gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0; gbc.weighty = 0.0;
        pnlDados.add(criarBlocoInformacao("Descrição", panc.getDesc()), gbc);

        card.add(pnlDados, BorderLayout.CENTER);

        return card;
    }

    private JPanel criarBlocoInformacao(String titulo, String conteudo) {
        JPanel bloco = new JPanel(new BorderLayout(0, 5));
        bloco.setBackground(CINZA_BG);

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblTitulo.setForeground(VERDE_TEXTO);

        JTextArea txtConteudo = new JTextArea(conteudo != null && !conteudo.isEmpty() ? conteudo : "-");
        txtConteudo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtConteudo.setLineWrap(true);
        txtConteudo.setWrapStyleWord(true);
        txtConteudo.setEditable(false);
        txtConteudo.setBackground(CINZA_BG);

        bloco.add(lblTitulo, BorderLayout.NORTH);
        bloco.add(txtConteudo, BorderLayout.CENTER);

        return bloco;
    }

    private JTextArea criarTextoValor(String texto) {
        JTextArea txt = new JTextArea(texto != null && !texto.isEmpty() ? texto : "-");
        txt.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txt.setLineWrap(true);
        txt.setWrapStyleWord(true);
        txt.setEditable(false);
        txt.setBackground(CINZA_BG);
        return txt;
    }

    private JPanel criarSecaoReceitas(PANC panc) {
        JPanel pnlSecao = new JPanel();
        pnlSecao.setLayout(new BoxLayout(pnlSecao, BoxLayout.Y_AXIS));
        pnlSecao.setBackground(Color.WHITE);
        pnlSecao.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblTitulo = new JLabel("Receitas Relacionadas");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTitulo.setForeground(VERDE_TEXTO);
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        pnlSecao.add(lblTitulo);
        pnlSecao.add(Box.createVerticalStrut(15));

        List<Receita> receitas = panc.getReceitas_relacionadas();
        if (receitas != null && !receitas.isEmpty()) {
            for (Receita r : receitas) {
                pnlSecao.add(criarCardReceita(r));
                pnlSecao.add(Box.createVerticalStrut(12));
            }
        } else {
            JLabel lblSemReceita = new JLabel("Nenhuma receita cadastrada para esta PANC.");
            lblSemReceita.setFont(new Font("SansSerif", Font.ITALIC, 13));
            lblSemReceita.setForeground(Color.GRAY);
            lblSemReceita.setAlignmentX(Component.LEFT_ALIGNMENT);
            pnlSecao.add(lblSemReceita);
        }

        return pnlSecao;
    }

    private JPanel criarCardReceita(Receita receita) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDA_CINZA, 1, true),
                new EmptyBorder(12, 15, 12, 15)
        ));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblNome = new JLabel(receita.getTitulo());
        lblNome.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblNome.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextArea txtPreparo = new JTextArea("Modo de Preparo: " + receita.getPreparo());
        txtPreparo.setFont(new Font("SansSerif", Font.PLAIN, 12));
        txtPreparo.setForeground(Color.DARK_GRAY);
        txtPreparo.setLineWrap(true);
        txtPreparo.setWrapStyleWord(true);
        txtPreparo.setEditable(false);
        txtPreparo.setBackground(Color.WHITE);
        txtPreparo.setBorder(new EmptyBorder(5, 0, 0, 0));
        txtPreparo.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(lblNome);
        card.add(txtPreparo);

        return card;
    }
}