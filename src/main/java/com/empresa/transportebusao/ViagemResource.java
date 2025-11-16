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

@Tag(name = "Viagens")
@Path("/api/v1/viagens")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ViagemResource {

    // === 4.3 Rate Limiting será aplicado por filtro global (não aqui diretamente) ===
    // === 4.1 Idempotência também é gerenciada por filtro global ===

    // 1. LISTAR TODOS
    @GET
    public List<ViagemRepresentation> listAll() {
        return Viagem.<Viagem>listAll().stream()
                .map(ViagemRepresentation::fromWithLinks)
                .collect(Collectors.toList());
    }

    // 2. BUSCAR POR ID
    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id) {
        Viagem viagem = Viagem.findById(id);
        if (viagem == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(ViagemRepresentation.fromWithLinks(viagem)).build();
    }

    // 3. CRIAR (com Idempotency-Key)
    @POST
    @Transactional
    public Response create(@Valid Viagem viagem, @HeaderParam("Idempotency-Key") String idempotencyKey,
                           @HeaderParam("X-API-Version") @DefaultValue("v1") String apiVersion) {

        // Preenche campos novos
        viagem.idempotencyKey = idempotencyKey;
        viagem.apiVersion = apiVersion;

        viagem.persist();
        return Response.status(Response.Status.CREATED)
                .entity(ViagemRepresentation.fromWithLinks(viagem))
                .build();
    }

    // 4. ATUALIZAR
    @PUT
    @Path("/{id}")
    @Transactional
    public Response update(@PathParam("id") Long id, @Valid Viagem dadosAtualizados) {
        Viagem viagem = Viagem.findById(id);
        if (viagem == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        viagem.motorista = dadosAtualizados.motorista;
        viagem.origem = dadosAtualizados.origem;
        viagem.destino = dadosAtualizados.destino;
        viagem.dataPartida = dadosAtualizados.dataPartida;
        viagem.status = dadosAtualizados.status;

        return Response.ok(ViagemRepresentation.fromWithLinks(viagem)).build();
    }

    // 5. DELETAR
    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        boolean deleted = Viagem.deleteById(id);
        return deleted ? Response.noContent().build() : Response.status(Response.Status.NOT_FOUND).build();
    }
}
