package com.empresa.transportebusao;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Sort;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.LinkedHashMap;

import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Tag(name = "Motoristas")
@Path("/api/v1/motoristas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MotoristaResource {

    @GET
    public List<MotoristaRepresentation> listAll() {
        return Motorista.<Motorista>listAll()
                .stream()
                .map(MotoristaRepresentation::fromWithLinks)
                .collect(Collectors.toList());
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id) {
        Motorista m = Motorista.findById(id);
        if (m == null) return Response.status(Response.Status.NOT_FOUND).build();

        return Response.ok(MotoristaRepresentation.fromWithLinks(m)).build();
    }

    @POST
    @Transactional
    public Response create(@Valid Motorista motorista,
                           @HeaderParam("Idempotency-Key") String idempotencyKey,
                           @HeaderParam("X-API-Version") @DefaultValue("v1") String apiVersion) {

        motorista.idempotencyKey = idempotencyKey;
        motorista.apiVersion = apiVersion;

        motorista.persist();

        return Response.status(Response.Status.CREATED)
                .entity(MotoristaRepresentation.fromWithLinks(motorista))
                .build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response update(@PathParam("id") Long id, @Valid Motorista dadosAtualizados) {
        Motorista m = Motorista.findById(id);
        if (m == null) return Response.status(Response.Status.NOT_FOUND).build();

        m.nomeCompleto = dadosAtualizados.nomeCompleto;
        m.cpf = dadosAtualizados.cpf;
        m.numeroCnh = dadosAtualizados.numeroCnh;
        m.categoriaCnh = dadosAtualizados.categoriaCnh;

        return Response.ok(MotoristaRepresentation.fromWithLinks(m)).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        boolean deleted = Motorista.deleteById(id);
        return deleted ? Response.noContent().build() : Response.status(Response.Status.NOT_FOUND).build();
    }

    // 🔍 BUSCA AVANÇADA (+ paginação, + versionamento)
    @GET
    @Path("/search")
    public Response search(
            @QueryParam("q") @DefaultValue("") String q,
            @QueryParam("sort") @DefaultValue("nomeCompleto") String sort,
            @QueryParam("direction") @DefaultValue("Ascending") String direction,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size
    ) {
        // EVITA o erro ASCENDING/Descending
        Sort.Direction dir;

        if (direction.equalsIgnoreCase("Ascending")) {
            dir = Sort.Direction.Ascending;
        } else if (direction.equalsIgnoreCase("Descending")) {
            dir = Sort.Direction.Descending;
        } else {
            dir = Sort.Direction.valueOf(direction.toUpperCase());
        }

        Sort panacheSort = Sort.by(sort).direction(dir);

        String filtro = "nomeCompleto LIKE ?1 OR cpf LIKE ?1 OR numeroCnh LIKE ?1";
        String pattern = "%" + q + "%";

        PanacheQuery<Motorista> query =
                Motorista.find(filtro, panacheSort, pattern);

        query.page(page, size);

        Map<String, Object> resposta = new LinkedHashMap<>();
        resposta.put("data", query.list());
        resposta.put("totalElements", query.count());
        resposta.put("totalPages", query.pageCount());
        resposta.put("currentPage", page);
        resposta.put("pageSize", size);
        resposta.put("query", q);
        resposta.put("sort", sort);
        resposta.put("direction", direction);

        return Response.ok(resposta).build();
    }
}
