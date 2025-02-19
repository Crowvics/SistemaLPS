package model.entities;

import jakarta.persistence.*;
import java.util.Map;

@Entity
@Table(name = "relatorios")
public class Relatorio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "atendimento_id", nullable = false, unique = true)
    private Atendimento atendimento;

    @Column(nullable = false, length = 500)
    private String descricaoServico;

    @ElementCollection
    @CollectionTable(name = "relatorio_pecas", joinColumns = @JoinColumn(name = "relatorio_id"))
    @MapKeyColumn(name = "nome_peca")
    @Column(name = "quantidade_utilizada")
    private Map<String, Integer> pecasUtilizadas;

    public Relatorio() {
    }

    public Relatorio(Atendimento atendimento, String descricaoServico, Map<String, Integer> pecasUtilizadas) {
        this.atendimento = atendimento;
        this.descricaoServico = descricaoServico;
        this.pecasUtilizadas = pecasUtilizadas;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Atendimento getAtendimento() {
        return atendimento;
    }

    public void setAtendimento(Atendimento atendimento) {
        this.atendimento = atendimento;
    }

    public String getDescricaoServico() {
        return descricaoServico;
    }

    public void setDescricaoServico(String descricaoServico) {
        this.descricaoServico = descricaoServico;
    }

    public Map<String, Integer> getPecasUtilizadas() {
        return pecasUtilizadas;
    }

    public void setPecasUtilizadas(Map<String, Integer> pecasUtilizadas) {
        this.pecasUtilizadas = pecasUtilizadas;
    }
}
