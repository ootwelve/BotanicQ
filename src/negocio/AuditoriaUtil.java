package negocio;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * RF19: monta a linha "Última alteração por ..." exibida nas telas de detalhe.
 */
public class AuditoriaUtil {

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");

    public static String descrever(String adminNome, LocalDateTime quando) {
        if (quando == null) {
            return "Última alteração: sem registro.";
        }
        String quem = (adminNome != null && !adminNome.isBlank()) ? adminNome : "(administrador removido)";
        return "Última alteração por " + quem + " em " + FORMATO.format(quando);
    }

    public static JLabel criarRotulo(String adminNome, LocalDateTime quando) {
        JLabel lbl = new JLabel(descrever(adminNome, quando));
        lbl.setFont(new Font("SansSerif", Font.ITALIC, 12));
        lbl.setForeground(Color.GRAY);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }
}
