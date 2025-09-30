package com.empresa.transportebusao;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.validation.constraints.NotBlank;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Entity
public class Motorista extends PanacheEntity {

    @NotBlank(message = "O CPF é obrigatório.")
    @Schema(example = "12345678900")
    public String cpf;

    // NOVO CAMPO ADICIONADO: O nome que faltava para o ViagemRepresentation!
    @NotBlank(message = "O nome completo é obrigatório.")
    @Schema(example = "João da Silva")
    public String nomeCompleto;

    @NotBlank(message = "A CNH é obrigatória.")
    @Schema(example = "98765432100")
    public String numeroCnh;

    @NotBlank(message = "A Categoria da CNH é obrigatória.")
    @Schema(example = "D")
    public String categoriaCnh;


    // Relacionamento One-to-One
    @OneToOne(mappedBy = "motorista", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    public RegistroDePonto pontoAtivo;
}