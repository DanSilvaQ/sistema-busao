package com.empresa.transportebusao;

import java.util.Map;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "OnibusRepresentation", description = "Representação de Ônibus com HATEOAS e versão da API")
public class OnibusRepresentation {

    public Long id;
    public String modelo;
    public String placa;
    public Integer capacidade;

    // 🔹 Novos campos
    public String idempotencyKey;
    public String apiVersion;

    public Map<String, Link> _links;

    public static OnibusRepresentation from(Onibus o) {
        OnibusRepresentation rep = new OnibusRepresentation();
        rep.id = o.id;
        rep.modelo = o.modelo;
        rep.placa = o.placa;
        rep.capacidade = o.capacidade;
        rep.apiVersion = o.apiVersion;
        rep.idempotencyKey = o.idempotencyKey;
        return rep;
    }

    public static OnibusRepresentation fromWithLinks(Onibus o) {
        OnibusRepresentation rep = from(o);
        if (o.id != null) {
            String base = "/api/" + o.apiVersion + "/onibus/" + o.id;
            rep._links = Map.of(
                    "self", new Link(base, "GET"),
                    "update", new Link(base, "PUT"),
                    "delete", new Link(base, "DELETE")
            );
        }
        return rep;
    }
}
