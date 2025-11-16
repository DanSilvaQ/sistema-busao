package com.empresa.transportebusao;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class Viagem extends PanacheEntity {

    @ManyToOne
    @JoinColumn(name = "motorista_id", nullable = false)
    @NotNull(message = "A viagem deve ter um motorista associado.")
    public Motorista motorista;

    @NotBlank(message = "O local de origem não pode ser nulo ou vazio.")
    public String origem;

    @NotBlank(message = "O local de destino não pode ser nulo ou vazio.")
    public String destino;

    @NotNull(message = "A data e hora da partida não podem ser nulas.")
    public LocalDateTime dataPartida;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "O status da viagem não pode ser nulo.")
    @Column(name = "viagem_status_enum")
    public StatusViagem status = StatusViagem.AGENDADA;

    // --- NOVO: CAMPOS PARA IDPOTÊNCIA E VERSIONAMENTO ---
    @Column(unique = true)
    public String idempotencyKey;

    @Column(name = "api_version")
    public String apiVersion = "v1";
}
