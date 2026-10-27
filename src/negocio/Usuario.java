package model;

import java.time.LocalDate;

public class Usuario {
    private int id;
    private String nome;
    private String senha;
    private int acesso;
    private LocalDate data;
    private String obs;

    public Usuario() {
    }

    public Usuario(int id, String nome, String senha, int acesso, LocalDate data, String obs) {
        this.id = id;
        this.nome = nome;
        this.senha = senha;
        this.acesso = acesso;
        this.data = data;
        this.obs = obs;
    }
  
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public int getAcesso() { return acesso; }
    public void setAcesso(int acesso) { this.acesso = acesso; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public String getObs() { return obs; }
    public void setObs(String obs) { this.obs = obs; }
}