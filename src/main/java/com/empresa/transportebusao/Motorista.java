package com.empresa.transportebusao;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
public class Motorista extends PanacheEntity {

    // 5. Validações: Garante que o nome não é vazio
    @NotBlank(message = "O nome completo do motorista é obrigatório.")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres.")
    public String nome;

    // 5. Validações: Valida o formato e obrigatoriedade da CNH
    @NotBlank(message = "A CNH é obrigatória.")
    @Column(unique = true)
    @Pattern(regexp = "\\d{11}", message = "A CNH deve ter exatamente 11 dígitos numéricos.")
    public String cnh;

    // ✅ NOVO CAMPO: CPF (Validação básica de 11 dígitos numéricos)
    @NotBlank(message = "O CPF é obrigatório.")
    @Column(unique = true)
    @Pattern(regexp = "\\d{11}", message = "O CPF deve ter exatamente 11 dígitos numéricos.")
    public String cpf;

    // Dentro da classe Motorista...
    // 5. Validações: Tipo de ônibus que o motorista pode dirigir
    @NotBlank(message = "O tipo de ônibus que o motorista pode dirigir (Comum/Articulado) é obrigatório.")
    @Pattern(regexp = "Comum|Articulado", message = "O tipo de habilitação deve ser 'Comum' ou 'Articulado'.")
    public String tipoHabilitacaoOnibus;

// ... (Resto da classe Motorista)

    // 4.1. Idempotência (Campo para simulação de verificação via DB)
    @Column(unique = true)
    public String idempotencyKey;

    // 4.5. Versionamento (Para auditoria no registro)
    @Column(name = "api_version")
    public String apiVersion = "v1";
}