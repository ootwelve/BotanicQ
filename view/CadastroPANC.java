import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class  telacadastropanc extends JFrame {

    
    private final Color VERDE = new Color(45, 130, 75);
    private final Color VERDE_ESCURO = new Color(25, 95, 50);
    private final Color VERDE_CLARO = new Color(235, 248, 238);
    private final Color FUNDO = new Color(247, 251, 248);
    private final Color CINZA = new Color(90, 90, 90);

    public telacadastropanc() {

        setTitle("BotanicQ - Cadastro de PANCs");

        setSize(1100, 750);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);

        criarTela();
    }

    private void criarTela() {

     

        JPanel principal = new JPanel(new BorderLayout(20, 20));

        principal.setBackground(FUNDO);

        principal.setBorder(
                new EmptyBorder(25, 35, 25, 35)
        );


        JPanel cabecalho = new JPanel(new BorderLayout(20, 0));

        cabecalho.setBackground(FUNDO);

        JPanel textos = new JPanel(
                new GridLayout(2, 1, 0, 5)
        );

        textos.setBackground(FUNDO);

        JLabel titulo = new JLabel(
                "Cadastrar nova PANC"
        );

        titulo.setFont(
                new Font("Arial", Font.BOLD, 30)
        );

        titulo.setForeground(VERDE_ESCURO);

        JLabel subtitulo = new JLabel(
                "Preencha as informações para registrar uma nova planta alimentícia não convencional."
        );

        subtitulo.setFont(
                new Font("Arial", Font.PLAIN, 14)
        );

        subtitulo.setForeground(CINZA);

        textos.add(titulo);
        textos.add(subtitulo);

        cabecalho.add(
                textos,
                BorderLayout.CENTER
        );


        JPanel acesso = new JPanel(
                new GridLayout(3, 1, 0, 3)
        );

        acesso.setPreferredSize(
                new Dimension(190, 80)
        );

        acesso.setBackground(VERDE_CLARO);

        acesso.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(200, 225, 205)
                        ),
                        new EmptyBorder(8, 12, 8, 12)
                )
        );

        JLabel acessoTitulo = new JLabel(
                "✓ Acesso autorizado"
        );

        acessoTitulo.setFont(
                new Font("Arial", Font.BOLD, 13)
        );

        acessoTitulo.setForeground(VERDE_ESCURO);

        JLabel usuario = new JLabel(
                "Usuário: admin"
        );

        JLabel perfil = new JLabel(
                "Perfil: Administrador"
        );

        acesso.add(acessoTitulo);
        acesso.add(usuario);
        acesso.add(perfil);

        cabecalho.add(
                acesso,
                BorderLayout.EAST
        );

        principal.add(
                cabecalho,
                BorderLayout.NORTH
        );

  

        JPanel formulario = new JPanel(
                new GridBagLayout()
        );

        formulario.setBackground(Color.WHITE);

        formulario.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(220, 232, 223)
                        ),
                        new EmptyBorder(20, 20, 20, 20)
                )
        );

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(8, 8, 8, 8);

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        gbc.weightx = 1;


        JTextField nomePopular =
                new JTextField();

        adicionarCampo(
                formulario,
                gbc,
                "Nome Popular *",
                nomePopular,
                0,
                0
        );

 

        JTextField nomeCientifico =
                new JTextField();

        adicionarCampo(
                formulario,
                gbc,
                "Nome Científico *",
                nomeCientifico,
                1,
                0
        );



        String[] categorias = {
                "Selecione uma categoria",
                "Folhosa",
                "Fruto",
                "Raiz",
                "Flor",
                "Semente",
                "Caule",
                "Outra"
        };

        JComboBox<String> categoria =
                new JComboBox<>(categorias);

        adicionarCampo(
                formulario,
                gbc,
                "Categoria *",
                categoria,
                0,
                1
        );

   

        JTextField origem =
                new JTextField();

        adicionarCampo(
                formulario,
                gbc,
                "Origem Nativa *",
                origem,
                1,
                1
        );

 

        JPanel painelImagem =
                criarPainelImagem();

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;

        formulario.add(
                painelImagem,
                gbc
        );


        JTextArea descricao =
                criarAreaTexto();

        adicionarAreaTexto(
                formulario,
                gbc,
                "Descrição *",
                descricao,
                1,
                2
        );



        JTextArea usoMedicinal =
                criarAreaTexto();

        adicionarAreaTexto(
                formulario,
                gbc,
                "Uso Medicinal *",
                usoMedicinal,
                0,
                3
        );

 

        JTextArea receitas =
                criarAreaTexto();

        adicionarAreaTexto(
                formulario,
                gbc,
                "Receitas Relacionadas *",
                receitas,
                1,
                3
        );

     

        JTextArea modoConsumo =
                criarAreaTexto();

        adicionarAreaTexto(
                formulario,
                gbc,
                "Modo de Consumo *",
                modoConsumo,
                0,
                4
        );

   
        JPanel aviso = new JPanel(
                new BorderLayout()
        );

        aviso.setBackground(VERDE_CLARO);

        aviso.setBorder(
                new EmptyBorder(10, 12, 10, 12)
        );

        JLabel textoAviso = new JLabel(
                "Os campos marcados com * são obrigatórios."
        );

        textoAviso.setFont(
                new Font("Arial", Font.PLAIN, 12)
        );

        textoAviso.setForeground(
                VERDE_ESCURO
        );

        aviso.add(
                textoAviso,
                BorderLayout.CENTER
        );

        gbc.gridx = 1;
        gbc.gridy = 4;

        formulario.add(
                aviso,
                gbc
        );

        principal.add(
                new JScrollPane(formulario),
                BorderLayout.CENTER
        );

   

        JButton salvar =
                new JButton("Salvar PANC");

        salvar.setFont(
                new Font("Arial", Font.BOLD, 16)
        );

        salvar.setForeground(Color.WHITE);

        salvar.setBackground(VERDE);

        salvar.setFocusPainted(false);

        salvar.setPreferredSize(
                new Dimension(200, 45)
        );

        JPanel painelBotao =
                new JPanel();

        painelBotao.setBackground(FUNDO);

        painelBotao.add(salvar);

        principal.add(
                painelBotao,
                BorderLayout.SOUTH
        );

        add(principal);
    }


    private void adicionarCampo(
            JPanel painel,
            GridBagConstraints gbc,
            String texto,
            java.awt.Component campo,
            int coluna,
            int linha) {

        JPanel grupo =
                new JPanel(
                        new BorderLayout(5, 5)
                );

        grupo.setBackground(Color.WHITE);

        JLabel label =
                new JLabel(texto);

        label.setFont(
                new Font("Arial", Font.BOLD, 14)
        );

        label.setForeground(
                VERDE_ESCURO
        );

        grupo.add(
                label,
                BorderLayout.NORTH
        );

        grupo.add(
                campo,
                BorderLayout.CENTER
        );

        gbc.gridx = coluna;
        gbc.gridy = linha;
        gbc.gridwidth = 1;

        painel.add(
                grupo,
                gbc
        );
    }


    private JTextArea criarAreaTexto() {

        JTextArea area =
                new JTextArea(5, 20);

        area.setLineWrap(true);

        area.setWrapStyleWord(true);

        area.setFont(
                new Font("Arial", Font.PLAIN, 14)
        );

        return area;
    }

    private void adicionarAreaTexto(
            JPanel painel,
            GridBagConstraints gbc,
            String texto,
            JTextArea area,
            int coluna,
            int linha) {

        JPanel grupo =
                new JPanel(
                        new BorderLayout(5, 5)
                );

        grupo.setBackground(Color.WHITE);

        JLabel label =
                new JLabel(texto);

        label.setFont(
                new Font("Arial", Font.BOLD, 14)
        );

        label.setForeground(
                VERDE_ESCURO
        );

        grupo.add(
                label,
                BorderLayout.NORTH
        );

        grupo.add(
                new JScrollPane(area),
                BorderLayout.CENTER
        );

        gbc.gridx = coluna;
        gbc.gridy = linha;
        gbc.gridwidth = 1;

        painel.add(
                grupo,
                gbc
        );
    }


    private JPanel criarPainelImagem() {

        JPanel painel =
                new JPanel(
                        new BorderLayout(5, 5)
                );

        painel.setBackground(Color.WHITE);

        JLabel titulo =
                new JLabel(
                        "Imagem da PANC *"
                );

        titulo.setFont(
                new Font("Arial", Font.BOLD, 14)
        );

        titulo.setForeground(
                VERDE_ESCURO
        );

        painel.add(
                titulo,
                BorderLayout.NORTH
        );

        JLabel imagem =
                new JLabel(
                        "🖼  Imagem da PANC",
                        SwingConstants.CENTER
                );

        imagem.setPreferredSize(
                new Dimension(300, 120)
        );

        imagem.setForeground(
                new Color(120, 150, 125)
        );

        imagem.setBorder(
                BorderFactory.createLineBorder(
                        new Color(200, 225, 205)
                )
        );

        painel.add(
                imagem,
                BorderLayout.CENTER
        );

        JButton escolher =
                new JButton(
                        "Selecionar imagem"
                );

    
        painel.add(
                escolher,
                BorderLayout.SOUTH
        );

        return painel;
    }

 
    public static void main(String[] args) {

    	telacadastropanc tela =
                new telacadastropanc();

        tela.setVisible(true);
    }
}