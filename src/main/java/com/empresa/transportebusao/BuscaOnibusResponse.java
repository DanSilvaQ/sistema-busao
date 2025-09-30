package com.empresa.transportebusao;

import java.util.List;
import java.util.stream.Collectors;

public class BuscaOnibusResponse {

    // Lista de ônibus convertida para a representação DTO
    public List<OnibusRepresentation> data;

    // Metadados da Paginação e Busca
    public long totalElements;
    public int totalPages;
    public int currentPage;
    public int pageSize;
    public String sort;
    public String direction;
    public String query;

    // Método estático para construir a resposta
    public static BuscaOnibusResponse from(
            List<Onibus> onibusList,
            String q,
            String sort,
            String direction,
            int page,
            int size,
            long totalElements,
            int totalPages)
    {
        BuscaOnibusResponse response = new BuscaOnibusResponse();

        // Mapeia a lista de Entidades para a lista de Representações
        response.data = onibusList.stream()
                .map(OnibusRepresentation::from)
                .collect(Collectors.toList());

        response.totalElements = totalElements;
        response.totalPages = totalPages;
        response.currentPage = page;
        response.pageSize = size;
        response.sort = sort;
        response.direction = direction;
        response.query = q;

        return response;
    }
}