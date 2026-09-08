import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class main {

    public static void main(String[] args) {

        FlatLightLaf.setup();

        SwingUtilities.invokeLater(() -> {
            new TelaLogin();
        });
    }
}


// =====================================================
// CORES
// =====================================================

class Cores {

    static Color verdeEscuro = new Color(27, 94, 32);
    static Color verde = new Color(46, 125, 50);
    static Color verdeMedio = new Color(67, 160, 71);
    static Color verdeClaro = new Color(200, 230, 201);
    static Color fundo = new Color(232, 245, 233);
    static Color branco = Color.WHITE;
    static Color cinza = new Color(90, 90, 90);
}


// =====================================================
// TELA DE LOGIN
// =====================================================

class TelaLogin extends JFrame {

    public TelaLogin() {

        setTitle("PANCs - Login");
        setSize(700, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel principal = new JPanel(new BorderLayout());
        principal.setBackground(Cores.fundo);

        // CABEÇALHO

        JPanel cabecalho = new JPanel();
        cabecalho.setBackground(Cores.verdeEscuro);
        cabecalho.setLayout(new BoxLayout(cabecalho, BoxLayout.Y_AXIS));
        cabecalho.setBorder(new EmptyBorder(25, 20, 25, 20));

        JLabel titulo = new JLabel("PANCs");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Arial", Font.BOLD, 36));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitulo = new JLabel(
                "Conheça, pesquise e descubra novas possibilidades"
        );
        subtitulo.setForeground(Cores.verdeClaro);
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        cabecalho.add(titulo);
        cabecalho.add(Box.createVerticalStrut(8));
        cabecalho.add(subtitulo);

        principal.add(cabecalho, BorderLayout.NORTH);


        // CARTÃO

        JPanel cartao = new JPanel();
        cartao.setBackground(Color.WHITE);
        cartao.setLayout(new BoxLayout(cartao, BoxLayout.Y_AXIS));
        cartao.setBorder(new EmptyBorder(30, 100, 30, 100));

        JLabel loginTitulo = new JLabel("Bem-vindo!");
        loginTitulo.setForeground(Cores.verdeEscuro);
        loginTitulo.setFont(new Font("Arial", Font.BOLD, 26));
        loginTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel mensagem = new JLabel("Entre na sua conta para continuar");
        mensagem.setForeground(Cores.cinza);
        mensagem.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField usuario = criarCampo();

        JPasswordField senha = new JPasswordField();
        senha.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));

        JButton entrar = new JButton("ENTRAR");
        entrar.setBackground(Cores.verde);
        entrar.setForeground(Color.WHITE);
        entrar.setFont(new Font("Arial", Font.BOLD, 15));
        entrar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        entrar.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton cadastro = new JButton("Ainda não tenho cadastro");
        cadastro.setForeground(Cores.verde);
        cadastro.setBackground(Color.WHITE);
        cadastro.setBorderPainted(false);
        cadastro.setAlignmentX(Component.CENTER_ALIGNMENT);

        cartao.add(loginTitulo);
        cartao.add(Box.createVerticalStrut(8));
        cartao.add(mensagem);
        cartao.add(Box.createVerticalStrut(25));

        adicionarCampo(cartao, "Usuário", usuario);
        adicionarCampo(cartao, "Senha", senha);

        cartao.add(Box.createVerticalStrut(5));
        cartao.add(entrar);
        cartao.add(Box.createVerticalStrut(5));
        cartao.add(cadastro);

        JPanel centro = new JPanel(new GridBagLayout());
        centro.setBackground(Cores.fundo);
        centro.add(cartao);

        principal.add(centro, BorderLayout.CENTER);


        // AÇÕES

        cadastro.addActionListener(e -> {
            dispose();
            new TelaCadastro();
        });

        entrar.addActionListener(e -> {

            if (usuario.getText().isEmpty()
                    || senha.getPassword().length == 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Preencha o usuário e a senha.",
                        "Atenção",
                        JOptionPane.WARNING_MESSAGE
                );

            } else {

                dispose();
                new TelaPrincipal(usuario.getText());
            }
        });

        add(principal);
        setVisible(true);
    }


    private static JTextField criarCampo() {

        JTextField campo = new JTextField();
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));

        return campo;
    }


    private static void adicionarCampo(
            JPanel painel,
            String texto,
            JComponent campo) {

        JLabel label = new JLabel(texto);
        label.setForeground(Cores.verdeEscuro);
        label.setFont(new Font("Arial", Font.BOLD, 13));

        painel.add(label);
        painel.add(Box.createVerticalStrut(5));
        painel.add(campo);
        painel.add(Box.createVerticalStrut(12));
    }
}


// =====================================================
// TELA DE CADASTRO
// =====================================================

class TelaCadastro extends JFrame {

    public TelaCadastro() {

        setTitle("PANCs - Cadastro");

        // TAMANHO DESKTOP
        setSize(800, 600);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel principal = new JPanel(new BorderLayout());
        principal.setBackground(Cores.fundo);


        // =================================================
        // CABEÇALHO
        // =================================================

        JPanel cabecalho = new JPanel();
        cabecalho.setBackground(Cores.verdeEscuro);
        cabecalho.setLayout(new BoxLayout(cabecalho, BoxLayout.Y_AXIS));
        cabecalho.setBorder(new EmptyBorder(25, 20, 25, 20));

        JLabel titulo = new JLabel("Criar sua conta");

        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Arial", Font.BOLD, 30));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitulo = new JLabel(
                "Faça parte da comunidade de pesquisa sobre PANCs"
        );

        subtitulo.setForeground(Cores.verdeClaro);
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        cabecalho.add(titulo);
        cabecalho.add(Box.createVerticalStrut(7));
        cabecalho.add(subtitulo);

        principal.add(cabecalho, BorderLayout.NORTH);


        // =================================================
        // FORMULÁRIO
        // =================================================

        JPanel formulario = new JPanel();

        formulario.setBackground(Color.WHITE);
        formulario.setLayout(new GridBagLayout());
        formulario.setBorder(
                new EmptyBorder(30, 60, 30, 60)
        );

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(8, 12, 8, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;


        // =================================================
        // CAMPOS
        // =================================================

        JTextField nome = criarCampo();
        JTextField usuario = criarCampo();

        JPasswordField senha = new JPasswordField();

        senha.setPreferredSize(
                new Dimension(280, 40)
        );

        JTextField data = criarCampo();


        // =================================================
        // NÍVEL DE ACESSO
        // =================================================

        String[] niveis = {
                "Usuário",
                "Administrador"
        };

        JComboBox<String> nivelAcesso =
                new JComboBox<>(niveis);

        nivelAcesso.setPreferredSize(
                new Dimension(280, 40)
        );


        // =================================================
        // COLUNA ESQUERDA
        // =================================================

        gbc.gridx = 0;
        gbc.gridy = 0;

        adicionarCampoGrid(
                formulario,
                gbc,
                "Nome completo",
                nome
        );


        gbc.gridy++;

        adicionarCampoGrid(
                formulario,
                gbc,
                "Usuário",
                usuario
        );


        gbc.gridy++;

        adicionarCampoGrid(
                formulario,
                gbc,
                "Nível de acesso",
                nivelAcesso
        );


        // =================================================
        // COLUNA DIREITA
        // =================================================

        gbc.gridx = 1;
        gbc.gridy = 0;

        adicionarCampoGrid(
                formulario,
                gbc,
                "Senha",
                senha
        );


        gbc.gridy++;

        adicionarCampoGrid(
                formulario,
                gbc,
                "Data de cadastro",
                data
        );


        // =================================================
        // AVISO
        // =================================================

        JLabel aviso = new JLabel(
                "Selecione o nível de acesso correspondente ao usuário."
        );

        aviso.setForeground(Cores.cinza);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;

        gbc.insets = new Insets(15, 12, 10, 12);

        formulario.add(aviso, gbc);


        // =================================================
        // BOTÃO CADASTRAR
        // =================================================

        JButton cadastrar =
                new JButton("CRIAR CONTA");

        cadastrar.setBackground(
                Cores.verdeMedio
        );

        cadastrar.setForeground(
                Color.WHITE
        );

        cadastrar.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        15
                )
        );

        cadastrar.setPreferredSize(
                new Dimension(300, 45)
        );

        gbc.gridy = 4;

        gbc.anchor = GridBagConstraints.CENTER;

        formulario.add(
                cadastrar,
                gbc
        );


        // =================================================
        // BOTÃO VOLTAR
        // =================================================

        JButton voltar =
                new JButton("Já tenho uma conta");

        voltar.setBackground(Color.WHITE);
        voltar.setForeground(Cores.verde);
        voltar.setBorderPainted(false);

        gbc.gridy = 5;

        formulario.add(
                voltar,
                gbc
        );


        JPanel centro =
                new JPanel(new GridBagLayout());

        centro.setBackground(
                Cores.fundo
        );

        centro.add(formulario);

        principal.add(
                centro,
                BorderLayout.CENTER
        );


        // =================================================
        // AÇÃO CADASTRAR
        // =================================================

        cadastrar.addActionListener(e -> {

            if (nome.getText().isEmpty()
                    || usuario.getText().isEmpty()
                    || senha.getPassword().length == 0
                    || data.getText().isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Preencha os campos obrigatórios!",
                        "Atenção",
                        JOptionPane.WARNING_MESSAGE
                );

            } else {

                String nivel =
                        (String) nivelAcesso.getSelectedItem();

                JOptionPane.showMessageDialog(
                        this,
                        "Cadastro realizado com sucesso!"
                                + "\nNível de acesso: "
                                + nivel,
                        "PANCs",
                        JOptionPane.INFORMATION_MESSAGE
                );

                dispose();

                new TelaLogin();
            }
        });


        // =================================================
        // VOLTAR
        // =================================================

        voltar.addActionListener(e -> {

            dispose();

            new TelaLogin();
        });


        add(principal);

        setVisible(true);
    }


    private JTextField criarCampo() {

        JTextField campo =
                new JTextField();

        campo.setPreferredSize(
                new Dimension(280, 40)
        );

        return campo;
    }


    private void adicionarCampoGrid(
            JPanel painel,
            GridBagConstraints gbc,
            String texto,
            JComponent campo) {

        JPanel grupo =
                new JPanel();

        grupo.setBackground(
                Color.WHITE
        );

        grupo.setLayout(
                new BoxLayout(
                        grupo,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel label =
                new JLabel(texto);

        label.setForeground(
                Cores.verdeEscuro
        );

        label.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        grupo.add(label);

        grupo.add(
                Box.createVerticalStrut(5)
        );

        grupo.add(campo);

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        painel.add(
                grupo,
                gbc
        );
    }
}


// =====================================================
// TELA PRINCIPAL
// =====================================================

class TelaPrincipal extends JFrame {

    public TelaPrincipal(String usuario) {

        setTitle("PANCs - Pesquisa");

        setSize(1000, 700);

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        setLocationRelativeTo(null);

        setResizable(false);


        JPanel principal =
                new JPanel(new BorderLayout());

        principal.setBackground(
                Cores.fundo
        );


        // =================================================
        // CABEÇALHO
        // =================================================

        JPanel cabecalho =
                new JPanel(new BorderLayout());

        cabecalho.setBackground(
                Cores.verdeEscuro
        );

        cabecalho.setBorder(
                new EmptyBorder(
                        20,
                        30,
                        20,
                        30
                )
        );


        JLabel titulo =
                new JLabel("🌿 PANCs");

        titulo.setForeground(
                Color.WHITE
        );

        titulo.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        30
                )
        );


        JLabel usuarioLabel =
                new JLabel(
                        "Olá, " + usuario + "!"
                );

        usuarioLabel.setForeground(
                Cores.verdeClaro
        );

        usuarioLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        15
                )
        );


        cabecalho.add(
                titulo,
                BorderLayout.WEST
        );

        cabecalho.add(
                usuarioLabel,
                BorderLayout.EAST
        );


        principal.add(
                cabecalho,
                BorderLayout.NORTH
        );


        // =================================================
        // CONTEÚDO
        // =================================================

        JPanel conteudo =
                new JPanel();

        conteudo.setBackground(
                Cores.fundo
        );

        conteudo.setLayout(
                new BoxLayout(
                        conteudo,
                        BoxLayout.Y_AXIS
                )
        );

        conteudo.setBorder(
                new EmptyBorder(
                        50,
                        100,
                        50,
                        100
                )
        );


        JLabel bemVindo =
                new JLabel(
                        "Explore o mundo das PANCs"
                );

        bemVindo.setForeground(
                Cores.verdeEscuro
        );

        bemVindo.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        28
                )
        );

        bemVindo.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );


        JLabel explicacao =
                new JLabel(
                        "Pesquise informações sobre as plantas que já foram estudadas."
                );

        explicacao.setForeground(
                Cores.cinza
        );

        explicacao.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );


        conteudo.add(bemVindo);

        conteudo.add(
                Box.createVerticalStrut(8)
        );

        conteudo.add(explicacao);

        conteudo.add(
                Box.createVerticalStrut(35)
        );


        // =================================================
        // PESQUISA
        // =================================================

        JPanel pesquisa =
                new JPanel(
                        new BorderLayout(10, 0)
                );

        pesquisa.setBackground(
                Color.WHITE
        );

        pesquisa.setBorder(
                new EmptyBorder(
                        15,
                        15,
                        15,
                        15
                )
        );

        pesquisa.setMaximumSize(
                new Dimension(750, 65)
        );


        JTextField campoPesquisa =
                new JTextField();


        JButton pesquisar =
                new JButton("PESQUISAR");

        pesquisar.setBackground(
                Cores.verde
        );

        pesquisar.setForeground(
                Color.WHITE
        );


        pesquisa.add(
                campoPesquisa,
                BorderLayout.CENTER
        );

        pesquisa.add(
                pesquisar,
                BorderLayout.EAST
        );


        conteudo.add(pesquisa);

        conteudo.add(
                Box.createVerticalStrut(30)
        );


        // =================================================
        // BOTÕES
        // =================================================

        JPanel botoes =
                new JPanel(
                        new GridLayout(
                                1,
                                2,
                                20,
                                0
                        )
                );

        botoes.setBackground(
                Cores.fundo
        );

        botoes.setMaximumSize(
                new Dimension(750, 100)
        );


        JButton todas =
                new JButton(
                        "🌱 VER TODAS AS PANCs"
                );

        JButton receitas =
                new JButton(
                        "🍃 RECEITAS"
                );


        todas.setBackground(
                Cores.verdeMedio
        );

        todas.setForeground(
                Color.WHITE
        );


        receitas.setBackground(
                Cores.verde
        );

        receitas.setForeground(
                Color.WHITE
        );


        botoes.add(todas);
        botoes.add(receitas);


        conteudo.add(botoes);


        principal.add(
                conteudo,
                BorderLayout.CENTER
        );


        // =================================================
        // PESQUISA
        // =================================================

        pesquisar.addActionListener(e -> {

            String busca =
                    campoPesquisa.getText();


            if (busca.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Digite o nome de uma PANC para pesquisar."
                );

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Pesquisa por: "
                                + busca
                                + "\n\nAqui aparecerão as informações da planta."
                );
            }
        });


        // =================================================
        // TODAS AS PANCs
        // =================================================

        todas.addActionListener(e -> {

            JOptionPane.showMessageDialog(
                    this,
                    "Aqui aparecerá a lista de todas as PANCs cadastradas."
            );
        });


        // =================================================
        // RECEITAS
        // =================================================

        receitas.addActionListener(e -> {

            JOptionPane.showMessageDialog(
                    this,
                    "Aqui aparecerão as receitas das PANCs."
            );
        });


        add(principal);

        setVisible(true);
    }
}