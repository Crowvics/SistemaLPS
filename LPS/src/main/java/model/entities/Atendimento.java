package model.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "atendimentos")
public class Atendimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "dispositivo_id", nullable = false)
    private Dispositivo dispositivo;

    @Column(nullable = false)
    private String problema;

    @Column(nullable = false)
    private String status;

    @Column
    private String feedbackNota = " - ";

    public Atendimento() {
    }

    public Atendimento(Dispositivo dispositivo, String problema, String status, String feedbackNota) {
        this.dispositivo = dispositivo;
        this.problema = problema;
        this.status = status;
        this.feedbackNota = " - ";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Dispositivo getDispositivo() {
        return dispositivo;
    }

    public void setDispositivo(Dispositivo dispositivo) {
        this.dispositivo = dispositivo;
    }

    public String getProblema() {
        return problema;
    }

    public void setProblema(String problema) {
        this.problema = problema;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFeedbackNota() {
        return feedbackNota;
    }

    public void setFeedbackNota(String feedbackNota) {
        this.feedbackNota = feedbackNota;
    }
}
