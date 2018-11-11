package com.example.phnf2.projeto2unidade.modelo;

public class Conversa {

    private int id;
    private String mensagem;
    private String nome;
    private String data;
    private String photoUrl;

    public Conversa() {
    }

    public Conversa(String mensagem, String nome, String data, String photoUrl) {
        id++;
        this.mensagem = mensagem;
        this.nome = nome;
        this.data = data;
        this.photoUrl = photoUrl;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

}
