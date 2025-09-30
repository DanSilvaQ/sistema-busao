package com.empresa.transportebusao;

import java.time.LocalDateTime;
import java.util.Map;


// Garanta que o Link esteja importado
import com.empresa.transportebusao.Link;


public class ViagemRepresentation {

    public Long id;

    // Representação do relacionamento
    public Long motoristaId;
    public String motoristaNome;

    public String origem;
    public String destino;
    public LocalDateTime dataPartida;
    public StatusViagem status;

    // CAMPO HATEOAS
    public Map<String, Link> _links;

    private ViagemRepresentation() {}

    // Método de fábrica simples (sem links)
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

        return rep;
    }

    // Método que garante o HATEOAS
    public static ViagemRepresentation fromWithLinks(Viagem viagem) {
        ViagemRepresentation rep = from(viagem);

        // A Viagem precisa de um ID para gerar links, então verificamos
        if (viagem.id != null) {
            String uri = "/viagens/" + viagem.id;
            rep._links = Map.of(
                    "self", new Link(uri, "GET"),
                    "update", new Link(uri, "PUT"),
                    "delete", new Link(uri, "DELETE")
            );
        }

        return rep;
    }
}