package com.empresa.transportebusao;

import java.time.LocalDateTime;
import java.util.Map;

import com.empresa.transportebusao.Link;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "ViagemRepresentation", description = "Representação da Viagem com HATEOAS e versão da API")
public class ViagemRepresentation {

    public Long id;
    public Long motoristaId;
    public String motoristaNome;
    public String origem;
    public String destino;
    public LocalDateTime dataPartida;
    public StatusViagem status;

    // NOVO: informações extras
    public String apiVersion;
    public String idempotencyKey;

    public Map<String, Link> _links;

    private ViagemRepresentation() {}

    public static ViagemRepresentation from(Viagem viagem) {
        ViagemRepresentation rep = new ViagemRepresentation();
        rep.id = viagem.id;

        if (viagem.motorista != null) {
            rep.motoristaId = viagem.motorista.id;
            rep.motoristaNome = viagem.motorista.nomeCompleto;
        }

        rep.origem = viagem.origem;
        rep.destino = viagem.destino;
        rep.dataPartida = viagem.dataPartida;
        rep.status = viagem.status;

        rep.apiVersion = viagem.apiVersion;
        rep.idempotencyKey = viagem.idempotencyKey;
        return rep;
    }

    public static ViagemRepresentation fromWithLinks(Viagem viagem) {
        ViagemRepresentation rep = from(viagem);
        if (viagem.id != null) {
            String uri = "/api/" + viagem.apiVersion + "/viagens/" + viagem.id;
            rep._links = Map.of(
                    "self", new Link(uri, "GET"),
                    "update", new Link(uri, "PUT"),
                    "delete", new Link(uri, "DELETE")
            );
        }
        return rep;
    }
}
