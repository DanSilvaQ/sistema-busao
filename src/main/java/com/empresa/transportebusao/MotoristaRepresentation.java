package com.empresa.transportebusao;

import java.util.Map;

public class MotoristaRepresentation {

    public Long id;
    public String nomeCompleto;
    public String cpf;
    public String numeroCnh;
    public String categoriaCnh;

    public Long pontoAtivoId;

    // 🔹 Novos campos
    public String idempotencyKey;
    public String apiVersion;

    public Map<String, Link> _links;

    // ======================================
    //      CONVERSÃO SIMPLES
    // ======================================
    public static MotoristaRepresentation from(Motorista m) {
        MotoristaRepresentation rep = new MotoristaRepresentation();

        rep.id = m.id;
        rep.nomeCompleto = m.nomeCompleto;
        rep.cpf = m.cpf;
        rep.numeroCnh = m.numeroCnh;
        rep.categoriaCnh = m.categoriaCnh;

        rep.idempotencyKey = m.idempotencyKey;
        rep.apiVersion = m.apiVersion;

        if (m.pontoAtivo != null)
            rep.pontoAtivoId = m.pontoAtivo.id;

        return rep;
    }

    // ======================================
    //      CONVERSÃO + HATEOAS
    // ======================================
    public static MotoristaRepresentation fromWithLinks(Motorista m) {
        MotoristaRepresentation rep = from(m);

        String base = "/api/" + m.apiVersion + "/motoristas/" + m.id;

        rep._links = Map.of(
                "self", new Link(base, "GET"),
                "update", new Link(base, "PUT"),
                "delete", new Link(base, "DELETE")
        );

        return rep;
    }
}
