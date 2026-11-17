package negocio;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Sintoma {
    private int id;
    private String nome;
    private String desc;
    private List<Receita> receitas_relacionadas;
    private String ultimo_admin;
    private LocalDateTime ultima_alteracao;

    public Sintoma() {
        this.receitas_relacionadas = new ArrayList<>();
    }

    public Sintoma(int id, String nome, String desc) {
        this.id = id;
        this.nome = nome;
        this.desc = desc;
        this.receitas_relacionadas = new ArrayList<>();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDesc() { return desc; }
    public void setDesc(String desc) { this.desc = desc; }

    public List<Receita> getReceitas_relacionadas() { return receitas_relacionadas; }
    public void setReceitas_relacionadas(List<Receita> receitas_relacionadas) { this.receitas_relacionadas = receitas_relacionadas; }

    public String getUltimo_admin() { return ultimo_admin; }
    public void setUltimo_admin(String ultimo_admin) { this.ultimo_admin = ultimo_admin; }

    public LocalDateTime getUltima_alteracao() { return ultima_alteracao; }
    public void setUltima_alteracao(LocalDateTime ultima_alteracao) { this.ultima_alteracao = ultima_alteracao; }
}