package com.empresa.transportebusao;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;


@Tag(name = "Motoristas")
@Path("/motoristas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

public class MotoristaResource {

    @GET
    public List<MotoristaRepresentation> listAll() {
        // Encadeamento simples que força a obtenção da List<Motorista> primeiro.
        return Motorista.<Motorista>listAll().stream()
                .map(MotoristaRepresentation::fromWithLinks)
                .collect(Collectors.toList());
    }

    // 2. BUSCAR POR ID (GET /motoristas/{id}) - Retorna HATEOAS
    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id) {
        Motorista motorista = Motorista.findById(id);

        if (motorista == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        // Retorna a Representação com links
        return Response.ok(MotoristaRepresentation.fromWithLinks(motorista)).build();
    }

    // 3. CRIAR (POST /motoristas) - Retorna HATEOAS e Status 201
    @POST
    @Transactional
    public Response create(@Valid Motorista motorista) {
        motorista.persist();

        // Retorna a Representação com links no status 201
        return Response.status(Response.Status.CREATED)
                .entity(MotoristaRepresentation.fromWithLinks(motorista))
                .build();
    }

    // 4. ATUALIZAR (PUT /motoristas/{id}) - Retorna HATEOAS
    @PUT
    @Path("/{id}")
    @Transactional
    public Response update(@PathParam("id") Long id, @Valid Motorista dadosAtualizados) {
        Motorista motorista = Motorista.findById(id);

        if (motorista == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        // Atualiza os campos
        motorista.nomeCompleto = dadosAtualizados.nomeCompleto;
        motorista.cpf = dadosAtualizados.cpf;
        motorista.numeroCnh = dadosAtualizados.numeroCnh;
        motorista.categoriaCnh = dadosAtualizados.categoriaCnh;

        // Retorna a Representação atualizada com links
        return Response.ok(MotoristaRepresentation.fromWithLinks(motorista)).build();
    }

    // 5. DELETAR (DELETE /motoristas/{id}) - Retorna Status 204
    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        boolean deleted = Motorista.deleteById(id);

        if (deleted) {
            return Response.noContent().build(); // Status 204 No Content
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }


    // ENDPOINT EXTRA: BUSCA AVANÇADA E PAGINAÇÃO (GET /motoristas/search?q=...)
    @GET
    @Path("/search")
    public BuscaMotoristaResponse search(
            @QueryParam("q") @DefaultValue("") String q,
            @QueryParam("sort") @DefaultValue("nomeCompleto") String sort,
            @QueryParam("direction") @DefaultValue("Ascending") String direction,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size)
    {
        Sort panacheSort = Sort.by(sort, Sort.Direction.valueOf(direction.toUpperCase()));
        String panacheQuery = "nomeCompleto LIKE ?1 OR cpf LIKE ?1 OR numeroCnh LIKE ?1";
        String searchTerm = "%" + q + "%";
        PanacheQuery<Motorista> query = Motorista.find(panacheQuery, panacheSort, searchTerm);
        query = query.page(Page.of(page, size));

        List<Motorista> motoristaList = query.list();
        long totalElements = query.count();
        int totalPages = query.pageCount();

        // Mapeia para Representação e adiciona os links HATEOAS
        List<MotoristaRepresentation> repListWithLinks = motoristaList.stream()
                .map(MotoristaRepresentation::fromWithLinks)
                .collect(Collectors.toList());

        // Cria e retorna o DTO de resposta da busca
        BuscaMotoristaResponse response = new BuscaMotoristaResponse();
        response.data = repListWithLinks;
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