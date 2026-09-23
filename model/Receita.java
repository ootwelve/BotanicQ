import java.util.ArrayList;
import java.util.List;

public class Receita {
    private int id;
    private String titulo;
    private String src_imagem;
    private String desc;
    private ArrayList<String> outros_ingredientes;
    private String preparo;
    private String uso;
    private List<PANC> panc_ingredientes;

    public Receita() {
        this.outros_ingredientes = new ArrayList<>();
        this.panc_ingredientes = new ArrayList<>();
    }

    public Receita(int id, String titulo, String src_imagem, String desc, 
                   ArrayList<String> outros_ingredientes, String preparo, String uso) {
        this.id = id;
        this.titulo = titulo;
        this.src_imagem = src_imagem;
        this.desc = desc;
        this.outros_ingredientes = outros_ingredientes != null ? outros_ingredientes : new ArrayList<>();
        this.preparo = preparo;
        this.uso = uso;
        this.panc_ingredientes = new ArrayList<>();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getSrc_imagem() { return src_imagem; }
    public void setSrc_imagem(String src_imagem) { this.src_imagem = src_imagem; }

    public String getDesc() { return desc; }
    public void setDesc(String desc) { this.desc = desc; }

    public ArrayList<String> getOutros_ingredientes() { return outros_ingredientes; }
    public void setOutros_ingredientes(ArrayList<String> outros_ingredientes) { this.outros_ingredientes = outros_ingredientes; }

    public String getPreparo() { return preparo; }
    public void setPreparo(String preparo) { this.preparo = preparo; }

    public String getUso() { return uso; }
    public void setUso(String uso) { this.uso = uso; }

    public List<PANC> getPanc_ingredientes() { return panc_ingredientes; }
    public void setPanc_ingredientes(List<PANC> panc_ingredientes) { this.panc_ingredientes = panc_ingredientes; }
}
