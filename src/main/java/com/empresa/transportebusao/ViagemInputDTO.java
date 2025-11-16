package com.empresa.transportebusao;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "ViagemInput", description = "Dados de entrada para agendamento de Viagem")
public class ViagemInputDTO {

    @NotBlank(message = "O ponto de origem é obrigatório.")
    public String origem;

    @NotBlank(message = "O ponto de destino é obrigatório.")
    public String destino;

    @NotNull(message = "A data e hora da partida são obrigatórias.")
    @FutureOrPresent(message = "A data da partida deve ser futura ou o momento atual.")
    public LocalDateTime dataPartida;

    // Validação para IDs de Relacionamento (FKs)
    @NotNull(message = "O ID do motorista é obrigatório.")
    @Min(value = 1, message = "O ID do motorista deve ser positivo.")
    public Long motoristaId;

    @NotNull(message = "O ID do ônibus é obrigatório.")
    @Min(value = 1, message = "O ID do ônibus deve ser positivo.")
    public Long onibusId;
}