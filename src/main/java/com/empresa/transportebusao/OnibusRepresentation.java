package com.empresa.transportebusao;

import java.util.Map;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "OnibusRepresentation", description = "Representação de Ônibus com HATEOAS e versão da API")
public class OnibusRepresentation {

    public Long id;
    public String modelo;
    public String placa;
    public Integer capacidade;

    // 4.5. Versionamento (Informado ao cliente)
    public String apiVersion;

    public Map<String, Link> _links;

    public static OnibusRepresentation from(Onibus o) {
        OnibusRepresentation rep = new OnibusRepresentation();
        rep.id = o.id;
        rep.modelo = o.modelo;
        rep.placa = o.placa;
        rep.capacidade = o.capacidade;
        rep.apiVersion = o.apiVersion;
        return rep;
    }

    public static OnibusRepresentation fromWithLinks(Onibus o) {
        OnibusRepresentation rep = from(o);
        if (o.id != null) {
            // Incorpora a versão correta do recurso nos links (HATEOAS)
            String base = "/api/" + o.apiVersion + "/onibus/" + o.id;
            rep._links = Map.of(
                    "self", new Link(base, "GET"),
                    "update", new Link(base, "PUT"),
                    "delete", new Link(base, "DELETE")
            );
        }
        return rep;
    }

    // Classe auxiliar de Link
    public static class Link {
        public String href;
        public String method;

        public Link(String href, String method) {
            this.href = href;
            this.method = method;
        }
    }
}