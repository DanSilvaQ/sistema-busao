package com.empresa.transportebusao;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Entity
public class Motorista extends PanacheEntity {

    @NotBlank(message = "O CPF é obrigatório.")
    @Schema(example = "12345678900")
    public String cpf;

    @NotBlank(message = "O nome completo é obrigatório.")
    @Schema(example = "João da Silva")
    public String nomeCompleto;

    @NotBlank(message = "O número da CNH é obrigatório.")
    @Schema(example = "98765432100")
    public String numeroCnh;

    @NotBlank(message = "A categoria da CNH é obrigatória.")
    @Schema(example = "D")
    public String categoriaCnh;

    @OneToOne(mappedBy = "motorista", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    public RegistroDePonto pontoAtivo;

    // ================================
    // 🔹 CAMPOS NOVOS (IDEMPOTÊNCIA + VERSÃO DA API)
    // ================================
    @Column(unique = true)
    public String idempotencyKey;

    @Column(name = "api_version")
    public String apiVersion = "v1";
}
