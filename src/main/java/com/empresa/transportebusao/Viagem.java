package com.empresa.transportebusao;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class Viagem extends PanacheEntity {

    // APENAS O RELACIONAMENTO NECESSÁRIO
    @ManyToOne
    @JoinColumn(name = "motorista_id", nullable = false)
    @NotNull(message = "A viagem deve ter um motorista associado.")
    public Motorista motorista;

    // CAMPOS SIMPLES
    @NotBlank(message = "O local de origem não pode ser nulo ou vazio.")
    public String origem;

    @NotBlank(message = "O local de destino não pode ser nulo ou vazio.")
    public String destino;

    @NotNull(message = "A data e hora da partida não podem ser nulas.")
    public LocalDateTime dataPartida;

    // STATUS (CORRIGIDO PARA O PROBLEMA DE PALAVRA RESERVADA SQL)
    @Enumerated(EnumType.STRING)
    @NotNull(message = "O status da viagem não pode ser nulo.")
    @Column(name = "viagem_status_enum")
    public StatusViagem status = StatusViagem.AGENDADA;

    // CAMPOS ANTERIORES COMENTADOS/REMOVIDOS
    /*
    public Onibus onibus;
    public List<Rota> rotas;
    */
}