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
@Path("/viagens")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

public class ViagemResource {

    // 1. LISTAR TODOS (GET /viagens) - Com correção de tipagem
    @GET
    public List<ViagemRepresentation> listAll() {
        // CORREÇÃO: Força o Panache a retornar List<Viagem> antes do stream()
        return Viagem.<Viagem>listAll().stream()
                .map(ViagemRepresentation::fromWithLinks)
                .collect(Collectors.toList());
    }

    // 2. BUSCAR POR ID (GET /viagens/{id})
    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id) {
        Viagem viagem = Viagem.findById(id);
        if (viagem == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(ViagemRepresentation.fromWithLinks(viagem)).build();
    }

    // 3. CRIAR (POST /viagens)
    @POST
    @Transactional
    public Response create(@Valid Viagem viagem) {
        viagem.persist();
        return Response.status(Response.Status.CREATED)
                .entity(ViagemRepresentation.fromWithLinks(viagem))
                .build();
    }

    // 4. ATUALIZAR (PUT /viagens/{id}) - APENAS CAMPOS EXISTENTES
    @PUT
    @Path("/{id}")
    @Transactional
    public Response update(@PathParam("id") Long id, @Valid Viagem dadosAtualizados) {
        Viagem viagem = Viagem.findById(id);
        if (viagem == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        // Atualiza APENAS os campos presentes na entidade simplificada
        viagem.motorista = dadosAtualizados.motorista;
        viagem.origem = dadosAtualizados.origem;
        viagem.destino = dadosAtualizados.destino;
        viagem.dataPartida = dadosAtualizados.dataPartida;
        viagem.status = dadosAtualizados.status;

        // Garante que não há referência a onibus ou rotas aqui.

        return Response.ok(ViagemRepresentation.fromWithLinks(viagem)).build();
    }

    // 5. DELETAR (DELETE /viagens/{id})
    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        boolean deleted = Viagem.deleteById(id);
        if (deleted) {
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    // O endpoint de BUSCA AVANÇADA (/search) FOI REMOVIDO PARA SIMPLICIDADE.
}