package negocio;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * RF19: log simples em texto com todas as alteracoes feitas pelos administradores.
 *
 * O arquivo fica em "logs/alteracoes.txt", na pasta de onde o programa e executado
 * (a raiz do projeto, quando rodado pelo Eclipse). Uma linha por alteracao:
 *
 *   16/11/2026 21:30:15 | admin | Cadastro | PANC | Ora-pro-nobis (id 12)
 */
public class LogAlteracoes {

    // acoes
    public static final String CADASTRO = "Cadastro";
    public static final String EDICAO = "Edição";
    public static final String EXCLUSAO = "Exclusão";
    public static final String ALTERACAO_SENHA = "Alteração de senha";

    // tipos de registro
    public static final String TIPO_PANC = "PANC";
    public static final String TIPO_RECEITA = "Receita";
    public static final String TIPO_SINTOMA = "Sintoma";
    public static final String TIPO_USUARIO = "Usuário";

    private static final Path ARQUIVO = Paths.get("logs", "alteracoes.txt");
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private static final String SEPARADOR = " | ";

    /** Uma linha ja separada em colunas. */
    public static class Registro {
        public final String dataHora;
        public final String administrador;
        public final String acao;
        public final String tipo;
        public final String registro;

        public Registro(String dataHora, String administrador, String acao, String tipo, String registro) {
            this.dataHora = dataHora;
            this.administrador = administrador;
            this.acao = acao;
            this.tipo = tipo;
            this.registro = registro;
        }
    }

    /**
     * Acrescenta uma linha ao log. Nunca lanca excecao: se nao conseguir gravar,
     * apenas avisa no console e devolve false (a alteracao no banco ja foi feita).
     */
    public static synchronized boolean registrar(Usuario admin, String acao, String tipo,
                                                 int idRegistro, String nomeRegistro) {
        String quem = (admin != null) ? admin.getNome() : "(desconhecido)";
        String linha = FORMATO.format(LocalDateTime.now())
                + SEPARADOR + limpar(quem)
                + SEPARADOR + acao
                + SEPARADOR + tipo
                + SEPARADOR + limpar(nomeRegistro) + " (id " + idRegistro + ")";

        try {
            Path pasta = ARQUIVO.toAbsolutePath().getParent();
            Files.createDirectories(pasta);
            Files.write(ARQUIVO, (linha + System.lineSeparator()).getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            return true;
        } catch (IOException e) {
            System.err.println("Nao foi possivel gravar o log de alteracoes: " + e.getMessage());
            return false;
        }
    }

    /** Le o arquivo inteiro. Do mais recente para o mais antigo. Vazio se ainda nao existir. */
    public static synchronized List<Registro> ler() throws IOException {
        List<Registro> lista = new ArrayList<>();
        if (!Files.exists(ARQUIVO)) {
            return lista;
        }

        // new String(...) troca bytes invalidos em vez de falhar, caso o arquivo seja editado a mao
        String conteudo = new String(Files.readAllBytes(ARQUIVO), StandardCharsets.UTF_8);
        for (String linha : conteudo.split("\\R")) {
            if (linha.isBlank()) {
                continue;
            }
            String[] partes = linha.split("\\s\\|\\s", 5);
            if (partes.length == 5) {
                lista.add(new Registro(partes[0].trim(), partes[1].trim(), partes[2].trim(),
                        partes[3].trim(), partes[4].trim()));
            } else {
                lista.add(new Registro("", "", "", "", linha.trim())); // linha fora do padrao: mostra como esta
            }
        }
        Collections.reverse(lista);
        return lista;
    }

    /** Caminho completo do arquivo, para exibir ao usuario. */
    public static Path getArquivo() {
        return ARQUIVO.toAbsolutePath();
    }

    // quebras de linha e "|" no meio de um nome estragariam o formato de uma linha por registro
    private static String limpar(String texto) {
        if (texto == null) {
            return "";
        }
        return texto.replaceAll("\\R", " ").replace("|", "/").trim();
    }
}
