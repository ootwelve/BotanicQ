package telas;

import javax.swing.*;
import java.awt.*;

/**
 * RF19: janela que exibe o log de alteracoes (logs/alteracoes.txt) dentro do programa.
 */
public class LogAlteracoesDialog extends JDialog {

    public LogAlteracoesDialog(Frame parent) {
        super(parent, "Log de Alterações", true);

        setSize(980, 620);
        setLocationRelativeTo(parent);
        setContentPane(new LogAlteracoesPanel(this::dispose));

        // ESC fecha a janela
        getRootPane().registerKeyboardAction(e -> dispose(),
                KeyStroke.getKeyStroke("ESCAPE"), JComponent.WHEN_IN_FOCUSED_WINDOW);
    }
}
