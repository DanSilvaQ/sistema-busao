package com.empresa.transportebusao;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "OnibusInput", description = "Dados de entrada necessários para criar ou atualizar um Ônibus")
public class OnibusInputDTO {

    // 5. Validação: Garante que o modelo não é nulo nem vazio
    @NotBlank(message = "O modelo do ônibus não pode ser vazio.")
    public String modelo;

    // 5. Validação: Garante que a placa segue o padrão e não é vazia
    @NotBlank(message = "A placa do ônibus não pode ser vazia.")
    @Pattern(regexp = "[A-Z]{3}[0-9A-Z]{4}", message = "A placa deve seguir o padrão (ex: AAA1B23).")
    public String placa;

    // 5. Validação: Garante que a capacidade é informada e está dentro dos limites físicos
    @NotNull(message = "A capacidade do ônibus deve ser informada.")
    @Min(value = 10, message = "A capacidade mínima deve ser 10 passageiros.")
    @Max(value = 150, message = "A capacidade máxima deve ser 150 passageiros.")
    public Integer capacidade;
}