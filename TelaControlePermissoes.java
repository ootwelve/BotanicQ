
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class TelaControlePermissoes extends JFrame {

   

    private final Color VERDE = new Color(45, 130, 75);
    private final Color VERDE_ESCURO = new Color(25, 95, 50);
    private final Color VERDE_CLARO = new Color(235, 248, 238);
    private final Color FUNDO = new Color(247, 251, 248);
    private final Color CINZA = new Color(90, 90, 90);
    private final Color CINZA_CLARO = new Color(245, 245, 245);

  

    public TelaControlePermissoes() {

        setTitle("BotanicQ - Controle de Permissões");

        setSize(950, 650);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);

        criarTela();
    }



    private void criarTela() {

        JPanel principal =
                new JPanel(new BorderLayout(20, 20));

        principal.setBackground(FUNDO);

        principal.setBorder(
                new EmptyBorder(30, 40, 30, 40)
        );

     

        JPanel cabecalho =
                new JPanel(new BorderLayout());

        cabecalho.setBackground(FUNDO);

        JPanel textos =
                new JPanel(new GridLayout(2, 1, 0, 5));

        textos.setBackground(FUNDO);

        JLabel titulo =
                new JLabel("Controle de Permissões");

        titulo.setFont(
                new Font("Arial", Font.BOLD, 30)
        );

        titulo.setForeground(VERDE_ESCURO);

        JLabel subtitulo =
                new JLabel(
                        "Gerencie o acesso às funcionalidades do sistema."
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

    
        JPanel usuario =
                new JPanel(new GridLayout(3, 1, 0, 3));

        usuario.setPreferredSize(
                new Dimension(220, 85)
        );

        usuario.setBackground(VERDE_CLARO);

        usuario.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(200, 225, 205)
                        ),
                        new EmptyBorder(8, 12, 8, 12)
                )
        );

        JLabel usuarioTitulo =
                new JLabel("✓ Usuário autenticado");

        usuarioTitulo.setFont(
                new Font("Arial", Font.BOLD, 13)
        );

        usuarioTitulo.setForeground(
                VERDE_ESCURO
        );

        JLabel nome =
                new JLabel("Nome: Administrador");

        JLabel nivel =
                new JLabel("Nível: ADMINISTRADOR");

        usuario.add(usuarioTitulo);
        usuario.add(nome);
        usuario.add(nivel);

        cabecalho.add(
                usuario,
                BorderLayout.EAST
        );

        principal.add(
                cabecalho,
                BorderLayout.NORTH
        );


        JPanel centro =
                new JPanel(new GridLayout(1, 2, 20, 0));

        centro.setBackground(FUNDO);


        JPanel permissoes =
                new JPanel(new BorderLayout(10, 15));

        permissoes.setBackground(Color.WHITE);

        permissoes.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(220, 232, 223)
                        ),
                        new EmptyBorder(20, 20, 20, 20)
                )
        );

        JLabel tituloPermissoes =
                new JLabel("Permissões de acesso");

        tituloPermissoes.setFont(
                new Font("Arial", Font.BOLD, 20)
        );

        tituloPermissoes.setForeground(
                VERDE_ESCURO
        );

        permissoes.add(
                tituloPermissoes,
                BorderLayout.NORTH
        );

        JPanel lista =
                new JPanel(
                        new GridLayout(4, 1, 0, 12)
                );

        lista.setBackground(Color.WHITE);

        lista.add(
                criarPermissao(
                        "✓",
                        "Visualizar",
                        "Acesso permitido"
                )
        );

        lista.add(
                criarPermissao(
                        "✓",
                        "Cadastrar",
                        "Acesso permitido"
                )
        );

        lista.add(
                criarPermissao(
                        "✓",
                        "Editar",
                        "Apenas administradores"
                )
        );

        lista.add(
                criarPermissao(
                        "✓",
                        "Remover",
                        "Apenas administradores"
                )
        );

        permissoes.add(
                lista,
                BorderLayout.CENTER
        );

        centro.add(permissoes);

   

        JPanel restricao =
                new JPanel(new BorderLayout(10, 15));

        restricao.setBackground(Color.WHITE);

        restricao.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(220, 232, 223)
                        ),
                        new EmptyBorder(20, 20, 20, 20)
                )
        );

        JLabel tituloRestricao =
                new JLabel("Ações disponíveis");

        tituloRestricao.setFont(
                new Font("Arial", Font.BOLD, 20)
        );

        tituloRestricao.setForeground(
                VERDE_ESCURO
        );

        restricao.add(
                tituloRestricao,
                BorderLayout.NORTH
        );

        JPanel acoes =
                new JPanel(
                        new GridLayout(3, 1, 0, 12)
                );

        acoes.setBackground(Color.WHITE);

        JButton visualizar =
                criarBotao(
                        "Visualizar"
                );

        JButton editar =
                criarBotao(
                        "Editar"
                );

        JButton remover =
                criarBotao(
                        "Remover"
                );

        acoes.add(visualizar);
        acoes.add(editar);
        acoes.add(remover);

        restricao.add(
                acoes,
                BorderLayout.CENTER
        );

        JLabel aviso =
                new JLabel(
                        "<html><center>🔒 Apenas usuários administradores<br>"
                        + "podem visualizar e utilizar<br>"
                        + "as opções Editar e Remover.</center></html>",
                        SwingConstants.CENTER
                );

        aviso.setFont(
                new Font("Arial", Font.PLAIN, 13)
        );

        aviso.setForeground(
                VERDE_ESCURO
        );

        aviso.setOpaque(true);

        aviso.setBackground(VERDE_CLARO);

        aviso.setBorder(
                new EmptyBorder(12, 10, 12, 10)
        );

        restricao.add(
                aviso,
                BorderLayout.SOUTH
        );

        centro.add(restricao);

        principal.add(
                centro,
                BorderLayout.CENTER
        );

 
        JPanel rodape =
                new JPanel(new BorderLayout());

        rodape.setBackground(FUNDO);

        JLabel informacao =
                new JLabel(
                        "As permissões apresentadas são determinadas pelo nível de acesso do usuário."
                );

        informacao.setFont(
                new Font("Arial", Font.PLAIN, 12)
        );

        informacao.setForeground(CINZA);

        rodape.add(
                informacao,
                BorderLayout.WEST
        );

        principal.add(
                rodape,
                BorderLayout.SOUTH
        );

        add(principal);
    }


    private JPanel criarPermissao(
            String simbolo,
            String nome,
            String descricao) {

        JPanel painel =
                new JPanel(new BorderLayout(10, 0));

        painel.setBackground(Color.WHITE);

        painel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(230, 235, 231)
                        ),
                        new EmptyBorder(10, 10, 10, 10)
                )
        );

        JLabel icone =
                new JLabel(simbolo);

        icone.setFont(
                new Font("Arial", Font.BOLD, 18)
        );

        icone.setForeground(VERDE);

        icone.setPreferredSize(
                new Dimension(25, 25)
        );

        painel.add(
                icone,
                BorderLayout.WEST
        );

        JPanel textos =
                new JPanel(new GridLayout(2, 1));

        textos.setBackground(Color.WHITE);

        JLabel titulo =
                new JLabel(nome);

        titulo.setFont(
                new Font("Arial", Font.BOLD, 14)
        );

        titulo.setForeground(VERDE_ESCURO);

        JLabel detalhe =
                new JLabel(descricao);

        detalhe.setFont(
                new Font("Arial", Font.PLAIN, 11)
        );

        detalhe.setForeground(CINZA);

        textos.add(titulo);
        textos.add(detalhe);

        painel.add(
                textos,
                BorderLayout.CENTER
        );

        return painel;
    }



    private JButton criarBotao(String texto) {

        JButton botao =
                new JButton(texto);

        botao.setFont(
                new Font("Arial", Font.BOLD, 14)
        );

        botao.setForeground(VERDE_ESCURO);

        botao.setBackground(CINZA_CLARO);

        botao.setFocusPainted(false);

        botao.setPreferredSize(
                new Dimension(180, 45)
        );

    

        return botao;
    }



    public static void main(String[] args) {

        TelaControlePermissoes tela =
                new TelaControlePermissoes();

        tela.setVisible(true);
    }
}