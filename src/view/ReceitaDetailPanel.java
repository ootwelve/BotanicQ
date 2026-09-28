package view;

import model.Receita;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class ReceitaDetailPanel extends JPanel {

    private Receita receita;
    private Runnable onVoltarCallback;

    private static final Color VERDE_TEXTO = new Color(34, 112, 62);

    public ReceitaDetailPanel(JFrame parentFrame, Receita receita, Runnable onVoltarCallback) {
        this.receita = receita;
        this.onVoltarCallback = onVoltarCallback;

        setLayout(new BorderLayout(20, 20));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(25, 25, 25, 25));

        add(criarTopo(), BorderLayout.NORTH);

        add(criarConteudoCentral(), BorderLayout.CENTER);
    }

	private JPanel criarTopo() {
        JPanel pnlTopo = new JPanel(new BorderLayout(15, 0));
        pnlTopo.setOpaque(false);

        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnVoltar.setPreferredSize(new Dimension(100, 36));
        btnVoltar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVoltar.addActionListener(e -> {
            if (onVoltarCallback != null) {
                onVoltarCallback.run();
            }
        });

        JLabel lblTitulo = new JLabel(receita.getTitulo() != null ? receita.getTitulo() : "Detalhes da Receita");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTitulo.setForeground(VERDE_TEXTO);

        pnlTopo.add(btnVoltar, BorderLayout.WEST);
        pnlTopo.add(lblTitulo, BorderLayout.CENTER);

        return pnlTopo;
    }

    private JComponent criarConteudoCentral() {
        JPanel pnlConteudo = new JPanel(new GridBagLayout());
        pnlConteudo.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;

        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 0.4; gbc.weighty = 1.0;

        JLabel lblImagem = new JLabel();
        lblImagem.setHorizontalAlignment(SwingConstants.CENTER);
        lblImagem.setOpaque(true);
        lblImagem.setBackground(new Color(245, 247, 245));
        lblImagem.setBorder(new LineBorder(new Color(220, 225, 220), 1, true));

        if (receita.getSrc_imagem() != null && !receita.getSrc_imagem().isEmpty()) {
            ImageIcon icon = new ImageIcon(receita.getSrc_imagem());
            Image img = icon.getImage().getScaledInstance(320, 240, Image.SCALE_SMOOTH);
            lblImagem.setIcon(new ImageIcon(img));
        } else {
            lblImagem.setText("<html><center>🖼️<br>Sem imagem disponível</center></html>");
            lblImagem.setFont(new Font("SansSerif", Font.PLAIN, 14));
            lblImagem.setForeground(Color.GRAY);
        }

        pnlConteudo.add(lblImagem, gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        gbc.weightx = 0.6; gbc.weighty = 1.0;

        JPanel pnlDetalhes = new JPanel();
        pnlDetalhes.setLayout(new BoxLayout(pnlDetalhes, BoxLayout.Y_AXIS));
        pnlDetalhes.setBackground(Color.WHITE);

        JLabel lblTituloIngredientes = new JLabel("Ingredientes:");
        lblTituloIngredientes.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblTituloIngredientes.setForeground(VERDE_TEXTO);

        String textoIngredientes;
        if (receita.getOutros_ingredientes() != null && !receita.getOutros_ingredientes().isEmpty()) {
            textoIngredientes = "• " + String.join("\n• ", receita.getOutros_ingredientes());
        } else {
            textoIngredientes = "Ingredientes não especificados.";
        }

        JTextArea txtIngredientes = criarTextAreaTexto(textoIngredientes);

        JLabel lblTituloPreparo = new JLabel("Modo de Preparo:");
        lblTituloPreparo.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblTituloPreparo.setForeground(VERDE_TEXTO);

        JTextArea txtPreparo = criarTextAreaTexto(
                receita.getPreparo() != null && !receita.getPreparo().isEmpty() 
                        ? receita.getPreparo() 
                        : "Modo de preparo não informado."
        );

        pnlDetalhes.add(lblTituloIngredientes);
        pnlDetalhes.add(Box.createVerticalStrut(5));
        pnlDetalhes.add(new JScrollPane(txtIngredientes));
        pnlDetalhes.add(Box.createVerticalStrut(15));
        pnlDetalhes.add(lblTituloPreparo);
        pnlDetalhes.add(Box.createVerticalStrut(5));
        pnlDetalhes.add(new JScrollPane(txtPreparo));

        pnlConteudo.add(pnlDetalhes, gbc);

        return pnlConteudo;
    }

    private JTextArea criarTextAreaTexto(String conteudo) {
        JTextArea area = new JTextArea(conteudo);
        area.setFont(new Font("SansSerif", Font.PLAIN, 13));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setEditable(false);
        area.setBackground(new Color(250, 251, 250));
        area.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(230, 235, 230), 1),
                new EmptyBorder(8, 8, 8, 8)
        ));
        return area;
    }
}