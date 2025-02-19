package model.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "pecas")
public class Peca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String codigoPeca;

    private String nome;
    private String categoria;
    private String descricao;
    private Integer quantidade;

    public Peca() {
    }

    public Peca(String codigoPeca, String nome, String categoria, String descricao, Integer quantidade) {
        this.codigoPeca = codigoPeca;
        this.nome = nome;
        this.categoria = categoria;
        this.descricao = descricao;
        this.quantidade = quantidade;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigoPeca() {
        return codigoPeca;
    }

    public void setCodigoPeca(String codigoPeca) {
        this.codigoPeca = codigoPeca;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
}
