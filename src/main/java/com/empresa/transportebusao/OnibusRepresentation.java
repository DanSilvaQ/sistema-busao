package com.empresa.transportebusao;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

public class OnibusRepresentation {

    @Schema(example = "1")
    public Long id;

    @Schema(example = "ABC-1234")
    public String placa;

    @Schema(example = "2020")
    public int anoFabricacao;

    @Schema(example = "50")
    public int capacidadePassageiros;

    private OnibusRepresentation() {}

    // Método estático de fábrica para converter a Entidade em Representação
    public static OnibusRepresentation from(Onibus onibus) {
        OnibusRepresentation rep = new OnibusRepresentation();
        rep.id = onibus.id;
        rep.placa = onibus.placa;
        rep.anoFabricacao = onibus.anoFabricacao;
        rep.capacidadePassageiros = onibus.capacidadePassageiros;
        return rep;
    }
}