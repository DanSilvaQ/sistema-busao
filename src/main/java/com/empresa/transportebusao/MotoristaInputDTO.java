package com.empresa.transportebusao;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "MotoristaInput", description = "Dados de entrada para registro ou atualização de Motorista")
public class MotoristaInputDTO {

    @NotBlank(message = "O nome completo do motorista é obrigatório.")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres.")
    public String nome;

    @NotBlank(message = "A CNH é obrigatória.")
    @Pattern(regexp = "\\d{11}", message = "A CNH deve ter exatamente 11 dígitos numéricos.")
    public String cnh;

    // ✅ NOVO CAMPO: CPF
    @NotBlank(message = "O CPF é obrigatório.")
    @Pattern(regexp = "\\d{11}", message = "O CPF deve ter exatamente 11 dígitos numéricos.")
    public String cpf;

    @NotBlank(message = "O tipo de ônibus que o motorista pode dirigir (Comum/Articulado) é obrigatório.")
    @Pattern(regexp = "Comum|Articulado", message = "O tipo de habilitação deve ser 'Comum' ou 'Articulado'.")
    public String tipoHabilitacaoOnibus;
// ... (Resto da classe MotoristaInputDTO)
}