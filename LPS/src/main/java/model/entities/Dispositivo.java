package model.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "dispositivos", uniqueConstraints = {
    @UniqueConstraint(columnNames = "nomeDispositivo")
})
public class Dispositivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String nomeDispositivo;

    private String categoria;
    private String marca;
    private String modelo;
    private String descricao;

    @Column(nullable = false)
    private String clienteEmail;

    public Dispositivo() {
    }

    public Dispositivo(String nomeDispositivo, String categoria, String marca, String modelo, String descricao, String clienteEmail) {
        this.nomeDispositivo = nomeDispositivo;
        this.categoria = categoria;
        this.marca = marca;
        this.modelo = modelo;
        this.descricao = descricao;
        this.clienteEmail = clienteEmail;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeDispositivo() {
        return nomeDispositivo;
    }

    public void setNomeDispositivo(String nomeDispositivo) {
        this.nomeDispositivo = nomeDispositivo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getClienteEmail() {
        return clienteEmail;
    }

    public void setClienteEmail(String clienteEmail) {
        this.clienteEmail = clienteEmail;
    }
}
