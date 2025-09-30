package com.empresa.transportebusao;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

@Entity
public class Onibus extends PanacheEntity {
    public String placa;
    public int anoFabricacao;
    public int capacidadePassageiros;
}
