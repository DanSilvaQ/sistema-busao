package com.empresa.transportebusao;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Onibus extends PanacheEntity {

    @NotBlank(message = "O modelo do ônibus não pode ser vazio.")
    public String modelo;

    @NotBlank(message = "A placa do ônibus não pode ser vazia.")
    @Column(unique = true)
    public String placa;

    @NotNull(message = "A capacidade do ônibus deve ser informada.")
    public Integer capacidade;

    // 🔹 NOVOS CAMPOS PARA OS RECURSOS AVANÇADOS
    @Column(unique = true)
    public String idempotencyKey; // para prevenir duplicação de POST

    @Column(name = "api_version")
    public String apiVersion = "v1"; // guarda a versão da API usada na criação
}
