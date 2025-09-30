package com.empresa.transportebusao;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;
import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import java.time.LocalDateTime;

@Entity
public class RegistroDePonto extends PanacheEntity {

    @NotNull(message = "O tipo de registro (entrada/saída) não pode ser nulo.")
    @Schema(example = "ENTRADA")
    public String tipoRegistro; // Ex: "ENTRADA", "SAIDA"

    @NotNull(message = "O horário do registro não pode ser nulo.")
    @Schema(example = "2025-09-28T07:55:00")
    public LocalDateTime horario;

    // Relacionamento One-to-One: Cada registro pertence a UM motorista
    // O motorista terá a chave estrangeira (JOIN COLUMN)
    @OneToOne
    @JoinColumn(name = "motorista_id", unique = true) // 'unique=true' garante o One-to-One
    @NotNull(message = "O registro de ponto deve estar ligado a um motorista.")
    public Motorista motorista;

    // Campo de localização, opcional
    @Schema(example = "Latitude: -23.5, Longitude: -46.6")
    public String localizacao;
}