package com.empresa.transportebusao;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Tag(name = "Onibus")
@Path("/api/v1/onibus")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OnibusResource {

    // GET - lista todos os ônibus
    @GET
    public List<OnibusRepresentation> listAll() {
        return Onibus.<Onibus>listAll().stream()
                .map(OnibusRepresentation::fromWithLinks)
                .collect(Collectors.toList());
    }

    // GET - busca por ID
    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id) {
        Onibus onibus = Onibus.findById(id);
        if (onibus == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(OnibusRepresentation.fromWithLinks(onibus)).build();
    }

    // POST - cria um novo ônibus (com suporte a Idempotência e Versionamento)
    @POST
    @Transactional
    public Response create(@Valid Onibus novo,
                           @HeaderParam("Idempotency-Key") String idempotencyKey,
                           @HeaderParam("X-API-Version") @DefaultValue("v1") String apiVersion) {

        novo.idempotencyKey = idempotencyKey;
        novo.apiVersion = apiVersion;

        novo.persist();
        return Response.status(Response.Status.CREATED)
                .entity(OnibusRepresentation.fromWithLinks(novo))
                .build();
    }

    // PUT - atualiza um ônibus
    @PUT
    @Path("/{id}")
    @Transactional
    public Response update(@PathParam("id") Long id, @Valid Onibus dados) {
        Onibus o = Onibus.findById(id);
        if (o == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        o.modelo = dados.modelo;
        o.placa = dados.placa;
        o.capacidade = dados.capacidade;
        return Response.ok(OnibusRepresentation.fromWithLinks(o)).build();
    }

    // DELETE - remove um ônibus
    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        boolean deleted = Onibus.deleteById(id);
        return deleted ? Response.noContent().build() : Response.status(Response.Status.NOT_FOUND).build();
    }
}
