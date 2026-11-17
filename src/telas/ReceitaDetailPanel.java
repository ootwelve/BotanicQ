package telas;

import negocio.AuditoriaUtil;
import negocio.PANC;
import negocio.Receita;
import negocio.Sintoma;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class ReceitaDetailPanel extends JPanel {

    private Receita receita;
    private Runnable onVoltarCallback;

    private static final Color VERDE_TEXTO = new Color(34, 112, 62);
    private static final Color CINZA_BG = new Color(248, 249, 250);
    private static final Color BORDA_CINZA = new Color(220, 224, 230);

    public ReceitaDetailPanel(JFrame parentFrame, Receita receita, Runnable onVoltarCallback) {
        this.receita = receita;
        this.onVoltarCallback = onVoltarCallback;

        setLayout(new BorderLayout(20, 20));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(25, 25, 25, 25));

        add(criarTopo(), BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(criarConteudoCentral());
        scroll.setBorder(null);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(12);

        add(scroll, BorderLayout.CENTER);
    }

    private JPanel criarTopo() {
        JPanel pnlTopo = new JPanel(new BorderLayout(15, 0));
        pnlTopo.setOpaque(false);

        JPanel pnlEsquerda = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlEsquerda.setOpaque(false);

        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnVoltar.setPreferredSize(new Dimension(100, 36));
        btnVoltar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVoltar.addActionListener(e -> {
            if (onVoltarCallback != null) {
                onVoltarCallback.run();
            }
        });
        pnlEsquerda.add(btnVoltar);

        JLabel lblTitulo = new JLabel(receita.getTitulo() != null ? receita.getTitulo() : "Detalhes da Receita");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTitulo.setForeground(VERDE_TEXTO);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));

        JPanel pnlCentro = new JPanel(new BorderLayout());
        pnlCentro.setOpaque(false);
        pnlCentro.add(lblTitulo, BorderLayout.WEST);

        JButton btnExportar = new JButton("<> Exportar TXT");
        btnExportar.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnExportar.setBackground(new Color(60, 130, 180));
        btnExportar.setForeground(Color.WHITE);
        btnExportar.setFocusPainted(false);
        btnExportar.setBorderPainted(false);
        btnExportar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnExportar.setPreferredSize(new Dimension(140, 36));
        btnExportar.addActionListener(e -> exportarParaTxt());

        pnlTopo.add(pnlEsquerda, BorderLayout.WEST);
        pnlTopo.add(pnlCentro, BorderLayout.CENTER);
        pnlTopo.add(btnExportar, BorderLayout.EAST);

        return pnlTopo;
    }

    private JPanel criarConteudoCentral() {
        JPanel pnlConteudo = new JPanel(new GridBagLayout());
        pnlConteudo.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;

        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 0.4; gbc.weighty = 1.0;

        JLabel lblImagem = new JLabel();
        lblImagem.setHorizontalAlignment(SwingConstants.CENTER);
        lblImagem.setVerticalAlignment(SwingConstants.CENTER);
        lblImagem.setOpaque(true);
        lblImagem.setBackground(new Color(245, 247, 245));
        lblImagem.setBorder(new LineBorder(BORDA_CINZA, 1, true));
        lblImagem.setPreferredSize(new Dimension(320, 240));

        if (receita.getSrc_imagem() != null && !receita.getSrc_imagem().trim().isEmpty()) {
            java.io.File fileImg = new java.io.File(receita.getSrc_imagem());
            if (fileImg.exists()) {
                ImageIcon icon = new ImageIcon(receita.getSrc_imagem());
                Image img = icon.getImage().getScaledInstance(320, 240, Image.SCALE_SMOOTH);
                lblImagem.setIcon(new ImageIcon(img));
            } else {
                lblImagem.setText("<html><center>📷<br>Imagem não encontrada</center></html>");
                lblImagem.setFont(new Font("SansSerif", Font.PLAIN, 13));
                lblImagem.setForeground(Color.GRAY);
            }
        } else {
            lblImagem.setText("<html><center>🖼️<br>Sem imagem disponível</center></html>");
            lblImagem.setFont(new Font("SansSerif", Font.PLAIN, 13));
            lblImagem.setForeground(Color.GRAY);
        }

        pnlConteudo.add(lblImagem, gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        gbc.weightx = 0.6; gbc.weighty = 1.0;

        JPanel pnlDetalhes = new JPanel();
        pnlDetalhes.setLayout(new BoxLayout(pnlDetalhes, BoxLayout.Y_AXIS));
        pnlDetalhes.setBackground(Color.WHITE);

        if (receita.getDesc() != null && !receita.getDesc().isEmpty()) {
            pnlDetalhes.add(criarSecaoTexto("Descrição", receita.getDesc()));
            pnlDetalhes.add(Box.createVerticalStrut(15));
        }

        StringBuilder pancsStr = new StringBuilder();
        if (receita.getPanc_ingredientes() != null && !receita.getPanc_ingredientes().isEmpty()) {
            for (int i = 0; i < receita.getPanc_ingredientes().size(); i++) {
                PANC p = receita.getPanc_ingredientes().get(i);
                pancsStr.append("• ").append(p.getNome());

                if (i < receita.getPanc_ingredientes().size() - 1) {
                    pancsStr.append("\n");
                }
            }
        } else {
            pancsStr.append("Nenhuma PANC vinculada a esta receita.");
        }
        pnlDetalhes.add(criarSecaoTexto("PANCs Utilizadas", pancsStr.toString()));
        pnlDetalhes.add(Box.createVerticalStrut(15));

        StringBuilder sintomasStr = new StringBuilder();
        if (receita.getSintoma_associados() != null && !receita.getSintoma_associados().isEmpty()) {
            for (int i = 0; i < receita.getSintoma_associados().size(); i++) {
                Sintoma s = receita.getSintoma_associados().get(i);
                sintomasStr.append("• ").append(s.getNome());
                
                if (i < receita.getSintoma_associados().size() - 1) {
                    sintomasStr.append("\n");
                }
            }
        } else {
            sintomasStr.append("Nenhum sintoma vinculado a esta receita.");
        }
        pnlDetalhes.add(criarSecaoTexto("Sintomas / Benefícios Associados", sintomasStr.toString()));
        pnlDetalhes.add(Box.createVerticalStrut(15));

        String textoIngredientes;
        if (receita.getOutros_ingredientes() != null && !receita.getOutros_ingredientes().isEmpty()) {
            textoIngredientes = "• " + String.join("\n• ", receita.getOutros_ingredientes());
        } else {
            textoIngredientes = "Outros ingredientes não especificados.";
        }
        pnlDetalhes.add(criarSecaoTexto("Outros Ingredientes", textoIngredientes));
        pnlDetalhes.add(Box.createVerticalStrut(15));

        String textoPreparo = (receita.getPreparo() != null && !receita.getPreparo().isEmpty())
                ? receita.getPreparo()
                : "Modo de preparo não informado.";
        pnlDetalhes.add(criarSecaoTexto("Modo de Preparo", textoPreparo));
        pnlDetalhes.add(Box.createVerticalStrut(15));
        pnlDetalhes.add(AuditoriaUtil.criarRotulo(receita.getUltimo_admin(), receita.getUltima_alteracao())); // RF19

        pnlConteudo.add(pnlDetalhes, gbc);

        return pnlConteudo;
    }

    private JPanel criarSecaoTexto(String titulo, String conteudo) {
        JPanel bloco = new JPanel(new BorderLayout(0, 5));
        bloco.setBackground(Color.WHITE);
        bloco.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblTitulo.setForeground(VERDE_TEXTO);

        JTextArea txtArea = new JTextArea(conteudo);
        txtArea.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtArea.setLineWrap(true);
        txtArea.setWrapStyleWord(true);
        txtArea.setEditable(false);
        txtArea.setBackground(CINZA_BG);
        txtArea.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDA_CINZA, 1),
                new EmptyBorder(8, 8, 8, 8)
        ));

        bloco.add(lblTitulo, BorderLayout.NORTH);
        bloco.add(txtArea, BorderLayout.CENTER);

        return bloco;
    }

    private void exportarParaTxt() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Salvar Receita como Texto Simples");
        fileChooser.setSelectedFile(new File((receita.getTitulo() != null ? receita.getTitulo() : "receita") + ".txt"));

        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File arquivoParaSalvar = fileChooser.getSelectedFile();

            try (PrintWriter writer = new PrintWriter(new FileWriter(arquivoParaSalvar))) {
                writer.println("==========================================");
                writer.println(" RECEITA: " + (receita.getTitulo() != null ? receita.getTitulo() : "Sem Título"));
                writer.println("==========================================");
                writer.println();

                if (receita.getDesc() != null && !receita.getDesc().isEmpty()) {
                    writer.println("DESCRIÇÃO:");
                    writer.println(receita.getDesc());
                    writer.println();
                }

                writer.println("PANCs UTILIZADAS:");
                if (receita.getPanc_ingredientes() != null && !receita.getPanc_ingredientes().isEmpty()) {
                    for (PANC p : receita.getPanc_ingredientes()) {
                        writer.println(" - " + p.getNome());
                    }
                } else {
                    writer.println(" - Nenhuma PANC vinculada.");
                }
                writer.println();

                writer.println("OUTROS INGRIEDIENTES:");
                if (receita.getOutros_ingredientes() != null && !receita.getOutros_ingredientes().isEmpty()) {
                    for (String ing : receita.getOutros_ingredientes()) {
                        writer.println(" - " + ing);
                    }
                } else {
                    writer.println(" - Não especificados.");
                }
                writer.println();

                writer.println("MODO DE PREPARO:");
                writer.println(receita.getPreparo() != null ? receita.getPreparo() : "Não informado.");
                writer.println();

                writer.println("SINTOMAS / BENEFÍCIOS ASSOCIADOS:");
                if (receita.getSintoma_associados() != null && !receita.getSintoma_associados().isEmpty()) {
                    for (Sintoma s : receita.getSintoma_associados()) {
                        writer.println(" - " + s.getNome());
                    }
                } else {
                    writer.println(" - Nenhum sintoma vinculado.");
                }
                writer.println();
                writer.println("==========================================");
                writer.println("Gerado por BotanicQ System");

                JOptionPane.showMessageDialog(this, "Receita exportada com sucesso para:\n" + arquivoParaSalvar.getAbsolutePath(), 
                        "Suporte à Exportação", JOptionPane.INFORMATION_MESSAGE);

            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao salvar o arquivo: " + e.getMessage(), 
                        "Erro de I/O", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}