package com.empresa.transportebusao;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "MotoristaRepresentation", description = "Representação de Motorista para a saída da API")
public class MotoristaRepresentation {

    public Long id;
    public String nome;
    public String cnh;
    public String cpf;
    public String apiVersion; // 4.5. Versionamento

    public static MotoristaRepresentation from(Motorista m) {
        MotoristaRepresentation rep = new MotoristaRepresentation();
        rep.id = m.id;
        rep.nome = m.nome;
        rep.cnh = m.cnh;
        rep.cpf = m.cpf;
        rep.apiVersion = m.apiVersion;
        return rep;
    }

    // O método fromWithLinks FOI REMOVIDO
}