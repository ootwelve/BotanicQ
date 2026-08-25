import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Main {

    // Gerenciador de Estado
    static class AppState {
        private final PropertyChangeSupport support = new PropertyChangeSupport(this);
        private String telaAtual = "TELA_1";

        public String getTelaAtual() {
            return telaAtual;
        }

        public void setTelaAtual(String novaTela) {
            String antiga = this.telaAtual;
            this.telaAtual = novaTela;
            support.firePropertyChange("telaAtual", antiga, novaTela);
        }

        public void addObserver(PropertyChangeListener listener) {
            support.addPropertyChangeListener(listener);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("BotanicQ");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 400);
        frame.setLayout(new BorderLayout());

        AppState state = new AppState();

        // Menu
        JPanel menuEsquerda = new JPanel();
        menuEsquerda.setBackground(new Color(55, 55, 55));
        menuEsquerda.setLayout(new GridLayout(4, 1, 10, 10));
        menuEsquerda.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton btn1 = new JButton("Opção 1");
        JButton btn2 = new JButton("Opção 2");
        JButton btn3 = new JButton("Opção 3");
        JButton btn4 = new JButton("Opção 4");

        btn1.addActionListener(e -> state.setTelaAtual("TELA_1"));
        btn2.addActionListener(e -> state.setTelaAtual("TELA_2"));
        btn3.addActionListener(e -> state.setTelaAtual("TELA_3"));
        btn4.addActionListener(e -> state.setTelaAtual("TELA_4"));

        menuEsquerda.add(btn1);
        menuEsquerda.add(btn2);
        menuEsquerda.add(btn3);
        menuEsquerda.add(btn4);

        frame.add(menuEsquerda, BorderLayout.WEST);

        // Painel
        CardLayout cardLayout = new CardLayout();
        JPanel painelCentro = new JPanel(cardLayout);

        // Criando menus/telas diferentes para a direita
        painelCentro.add(criarMenuDireita("Menu 1", new String[]{"Sub-item 1.1", "Sub-item 1.2", "Sub-item 1.3", "Sub-item 1.4"}), "TELA_1");
        painelCentro.add(criarMenuDireita("Menu 2", new String[]{"Ação 2.1", "Ação 2.2", "Ação 2.3", "Ação 2.4"}), "TELA_2");
        painelCentro.add(criarMenuDireita("Menu 3", new String[]{"Config A", "Config B", "Config C", "Config D"}), "TELA_3");
        painelCentro.add(criarMenuDireita("Menu 4", new String[]{"Relatório 1", "Relatório 2", "Relatório 3", "Relatório 4"}), "TELA_4");

        // 4. Observer que escuta a mudança de estado e troca o Card
        state.addObserver(evt -> {
            if ("telaAtual".equals(evt.getPropertyName())) {
                String novaTela = (String) evt.getNewValue();
                cardLayout.show(painelCentro, novaTela);
            }
        });

        frame.add(painelCentro, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    // Função auxiliar para gerar os painéis da direita dinamicamente
    private static JPanel criarMenuDireita(String titulo, String[] botoes) {
        JPanel menuDireita = new JPanel();
        menuDireita.setBackground(new Color(45, 45, 45));
        menuDireita.setLayout(new GridLayout(botoes.length + 1, 1, 10, 10));
        menuDireita.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitulo = new JLabel(titulo, SwingConstants.CENTER);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        menuDireita.add(lblTitulo);

        for (String nomeBotao : botoes) {
            menuDireita.add(new JButton(nomeBotao));
        }

        return menuDireita;
    }
}