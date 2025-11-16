package com.empresa.transportebusao;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class Viagem extends PanacheEntity {

    // 5. Validações: Garante que os campos de rota são preenchidos
    @NotBlank(message = "O ponto de origem é obrigatório.")
    public String origem;

    @NotBlank(message = "O ponto de destino é obrigatório.")
    public String destino;

    // 5. Validações: Garante que a data/hora é no futuro ou presente
    @NotNull(message = "A data e hora da partida são obrigatórias.")
    @FutureOrPresent(message = "A data da partida deve ser futura ou o momento atual.")
    public LocalDateTime dataPartida;

    // Relacionamentos - Garante que o ID da FK é preenchido
    @ManyToOne
    @NotNull(message = "O motorista da viagem é obrigatório.")
    public Motorista motorista;

    @ManyToOne
    @NotNull(message = "O ônibus da viagem é obrigatório.")
    public Onibus onibus;

    // 4.1. Idempotência (Campo para simulação de verificação via DB)
    @Column(unique = true)
    public String idempotencyKey;

    // 4.5. Versionamento (Para auditoria no registro)
    @Column(name = "api_version")
    public String apiVersion = "v1";
}