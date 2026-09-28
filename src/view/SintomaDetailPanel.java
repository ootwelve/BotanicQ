package view;

import model.Receita;
import model.Sintoma;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.List;

public class SintomaDetailPanel extends JPanel {

    private static final Color VERDE_TEXTO = new Color(34, 112, 62);
    private static final Color CINZA_BG = new Color(248, 249, 250);
    private static final Color BORDA_CINZA = new Color(220, 224, 230);

    public SintomaDetailPanel(JFrame parentFrame, Sintoma sintoma, Runnable onVoltarCallback) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 25, 20, 25));

        add(criarPainelTopo(sintoma, onVoltarCallback), BorderLayout.NORTH);

        JPanel pnlCorpo = new JPanel();
        pnlCorpo.setLayout(new BoxLayout(pnlCorpo, BoxLayout.Y_AXIS));
        pnlCorpo.setBackground(Color.WHITE);

        JPanel cardInfo = criarCardInformacoes(sintoma);
        cardInfo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel secaoReceitas = criarSecaoReceitas(sintoma);
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

    private JPanel criarPainelTopo(Sintoma sintoma, Runnable onVoltarCallback) {
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

        JLabel lblNome = new JLabel(sintoma.getNome());
        lblNome.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblNome.setForeground(VERDE_TEXTO);

        pnlTopo.add(btnVoltar);
        pnlTopo.add(lblNome);

        return pnlTopo;
    }

    private JPanel criarCardInformacoes(Sintoma sintoma) {
        JPanel card = new JPanel(new BorderLayout(0, 10));
        card.setBackground(CINZA_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDA_CINZA, 1, true),
                new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel lblDescTitulo = new JLabel("Descrição / Diagnóstico Objetivo:");
        lblDescTitulo.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblDescTitulo.setForeground(VERDE_TEXTO);

        JTextArea txtDescricao = new JTextArea(
                sintoma.getDesc() != null && !sintoma.getDesc().isEmpty() 
                ? sintoma.getDesc() 
                : "Nenhuma descrição informada."
        );
        txtDescricao.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtDescricao.setLineWrap(true);
        txtDescricao.setWrapStyleWord(true);
        txtDescricao.setEditable(false);
        txtDescricao.setBackground(CINZA_BG);

        card.add(lblDescTitulo, BorderLayout.NORTH);
        card.add(txtDescricao, BorderLayout.CENTER);

        return card;
    }

    private JPanel criarSecaoReceitas(Sintoma sintoma) {
        JPanel pnlSecao = new JPanel();
        pnlSecao.setLayout(new BoxLayout(pnlSecao, BoxLayout.Y_AXIS));
        pnlSecao.setBackground(Color.WHITE);
        pnlSecao.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblTitulo = new JLabel("Receitas Indicadas para este Sintoma");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTitulo.setForeground(VERDE_TEXTO);
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        pnlSecao.add(lblTitulo);
        pnlSecao.add(Box.createVerticalStrut(15));

        List<Receita> receitas = sintoma.getReceitas_relacionadas();
        if (receitas != null && !receitas.isEmpty()) {
            for (Receita r : receitas) {
                pnlSecao.add(criarCardReceita(r));
                pnlSecao.add(Box.createVerticalStrut(12));
            }
        } else {
            JLabel lblSemReceita = new JLabel("Nenhuma receita vinculada a este sintoma no momento.");
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