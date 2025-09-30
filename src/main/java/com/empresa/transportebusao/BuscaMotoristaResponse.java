package com.empresa.transportebusao;

import java.util.List;
import java.util.stream.Collectors;

public class BuscaMotoristaResponse {

    public List<MotoristaRepresentation> data;

    // Metadados da Paginação
    public long totalElements;
    public int totalPages;
    public int currentPage;
    public int pageSize;
    public String sort;
    public String direction;
    public String query;

    // Constrói a resposta a partir da lista de Entidades (Motorista)
    public static BuscaMotoristaResponse from(
            List<Motorista> motoristaList,
            String q,
            String sort,
            String direction,
            int page,
            int size,
            long totalElements,
            int totalPages)
    {
        BuscaMotoristaResponse response = new BuscaMotoristaResponse();

        response.data = motoristaList.stream()
                .map(MotoristaRepresentation::from)
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