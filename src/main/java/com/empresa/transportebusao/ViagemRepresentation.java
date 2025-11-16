package com.empresa.transportebusao;

import java.time.LocalDateTime;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "ViagemRepresentation", description = "Representação de uma Viagem")
public class ViagemRepresentation {

    public Long id;
    public String origem;
    public String destino;
    public LocalDateTime dataPartida;
    public String motoristaNome; // Exibe o nome em vez do ID
    public String onibusPlaca;   // Exibe a placa em vez do ID
    public String apiVersion;

    public static ViagemRepresentation from(Viagem v) {
        ViagemRepresentation rep = new ViagemRepresentation();
        rep.id = v.id;
        rep.origem = v.origem;
        rep.destino = v.destino;
        rep.dataPartida = v.dataPartida;

        // Mapeamento de FKs (assumindo que as entidades estão carregadas)
        rep.motoristaNome = v.motorista != null ? v.motorista.nome : "N/A";
        rep.onibusPlaca = v.onibus != null ? v.onibus.placa : "N/A";

        rep.apiVersion = v.apiVersion;
        return rep;
    }
}