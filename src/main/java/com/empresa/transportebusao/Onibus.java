package com.empresa.transportebusao;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Entity
public class Onibus extends PanacheEntity {

    // 5. Validações: Nome do Modelo
    @NotBlank(message = "O modelo do ônibus não pode ser vazio.")
    public String modelo;

    // 5. Validações: Placa (Única e formato)
    @NotBlank(message = "A placa do ônibus não pode ser vazia.")
    @Column(unique = true)
    @Pattern(regexp = "[A-Z]{3}[0-9A-Z]{4}", message = "A placa deve seguir o padrão Mercosul (ex: AAA1B23).")
    public String placa;

    // 5. Validações: Capacidade
    @NotNull(message = "A capacidade do ônibus deve ser informada.")
    @Min(value = 10, message = "A capacidade mínima deve ser 10 passageiros.")
    @Max(value = 150, message = "A capacidade máxima deve ser 150 passageiros.")
    public Integer capacidade;

    // ✅ NOVO CAMPO: Tipo de ônibus (Para compatibilidade com o motorista)
    @NotBlank(message = "O tipo de ônibus (Comum/Articulado) é obrigatório.")
    @Pattern(regexp = "Comum|Articulado", message = "O tipo do ônibus deve ser 'Comum' ou 'Articulado'.")
    public String tipoOnibus;

    // 4.1. Idempotência
    @Column(unique = true)
    public String idempotencyKey;

    // 4.5. Versionamento
    @Column(name = "api_version")
    public String apiVersion = "v1";
}