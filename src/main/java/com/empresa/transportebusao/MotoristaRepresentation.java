package com.empresa.transportebusao;

import java.util.Map;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import java.util.Collections; // Importar para garantir o Map.of funcionar

public class MotoristaRepresentation {

    public Long id;

    // ... (campos nomeCompleto, cpf, etc.) ...

    @Schema(example = "João da Silva")
    public String nomeCompleto;

    @Schema(example = "12345678900")
    public String cpf;

    @Schema(example = "D")
    public String categoriaCnh;

    public Long pontoAtivoId;

    // CAMPO HATEOAS
    public Map<String, Link> _links;

    private MotoristaRepresentation() {}

    // Método original de fábrica (sem links)
    public static MotoristaRepresentation from(Motorista motorista) {
        MotoristaRepresentation rep = new MotoristaRepresentation();
        rep.id = motorista.id;
        rep.nomeCompleto = motorista.nomeCompleto;
        rep.cpf = motorista.cpf;
        rep.categoriaCnh = motorista.categoriaCnh;

        if (motorista.pontoAtivo != null) {
            rep.pontoAtivoId = motorista.pontoAtivo.id;
        }

        return rep;
    }

    // MÉTODO QUE FALTAVA (fromWithLinks)
    public static MotoristaRepresentation fromWithLinks(Motorista motorista) {
        MotoristaRepresentation rep = from(motorista); // Chama o 'from' original sem links

        // Cria a URI base (ex: /motoristas/1)
        String uri = "/motoristas/" + motorista.id;

        // Adiciona os links HATEOAS
        rep._links = Map.of(
                "self", new Link(uri, "GET"), // Link para si mesmo
                "update", new Link(uri, "PUT"), // Link para atualização
                "delete", new Link(uri, "DELETE") // Link para exclusão
        );
        return rep;
    }
}