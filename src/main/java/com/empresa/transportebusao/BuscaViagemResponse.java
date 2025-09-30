package com.empresa.transportebusao;

import java.util.List;
import java.util.stream.Collectors;

public class BuscaViagemResponse {

    // Lista de Viagens convertida para a representação DTO
    public List<ViagemRepresentation> data;

    // Metadados da Paginação e Busca
    public long totalElements;
    public int totalPages;
    public int currentPage;
    public int pageSize;
    public String sort;
    public String direction;
    public String query;

    public static BuscaViagemResponse from(
            List<Viagem> viagemList,
            String q,
            String sort,
            String direction,
            int page,
            int size,
            long totalElements,
            int totalPages)
    {
        BuscaViagemResponse response = new BuscaViagemResponse();

        // Mapeia a lista de Entidades para a lista de Representações (que agora inclui 'status')
        response.data = viagemList.stream()
                .map(ViagemRepresentation::from) // Chama o método 'from' atualizado
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