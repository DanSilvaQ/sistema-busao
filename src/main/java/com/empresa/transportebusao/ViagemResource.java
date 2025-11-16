package com.empresa.transportebusao;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Tag(name = "Viagens")
@Path("/api/v1/viagens") // Requisito 4.5: Versionamento via URL (V1)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ViagemResource {

    // --- GET (Lista todos) ---
    @GET
    @Operation(summary = "Lista todas as viagens agendadas (V1)")
    public List<ViagemRepresentation> listAll() {
        return Viagem.<Viagem>listAll().stream()
                .map(ViagemRepresentation::from)
                .collect(Collectors.toList());
    }

    // --- GET por ID ---
    @GET
    @Path("/{id}")
    @Operation(summary = "Busca uma viagem por ID (V1)")
    @APIResponse(responseCode = "404", description = "Viagem não encontrada.")
    public Response findById(@PathParam("id") Long id) {
        Viagem viagem = Viagem.findById(id);
        if (viagem == null) {
            return Response.status(Response.Status.NOT_FOUND).build(); // Requisito 5: HTTP 404
        }
        return Response.ok(ViagemRepresentation.from(viagem)).build();
    }

    // --- POST (Criação Idempotente e Versionada) ---
    @POST
    @Transactional
    @Operation(
            summary = "Agenda uma nova viagem (V1)",
            description = "A operação suporta Idempotência e valida a existência do motorista/ônibus."
    )
    @APIResponse(responseCode = "201", description = "Viagem agendada com sucesso.")
    @APIResponse(responseCode = "400", description = "Dados ou IDs de relacionamento inválidos.")
    @APIResponse(responseCode = "404", description = "Motorista ou Ônibus não encontrados.")
    @APIResponse(responseCode = "409", description = "Operação já processada com esta Idempotency-Key.")
    public Response create(
            @Valid ViagemInputDTO input,
            @HeaderParam("Idempotency-Key") String idempotencyKey,
            @HeaderParam("X-API-Version") @DefaultValue("v1") String apiVersion) {

        // 1. Lógica de Idempotência (Simulação via DB)
        if (idempotencyKey != null && !idempotencyKey.isBlank()) {
            if (Viagem.find("idempotencyKey", idempotencyKey).firstResultOptional().isPresent()) {
                return Response.status(409)
                        .entity(Map.of("message", "Operação já processada com esta Idempotency-Key."))
                        .build();
            }
        }

        // 2. Validação e Carregamento de Relacionamentos (Requisito 5: Tratamento de Erro 404)
        Motorista motorista = Motorista.findById(input.motoristaId);
        if (motorista == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("message", "Motorista com ID " + input.motoristaId + " não encontrado."))
                    .build();
        }

        Onibus onibus = Onibus.findById(input.onibusId);
        if (onibus == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("message", "Ônibus com ID " + input.onibusId + " não encontrado."))
                    .build();
        }

        // 3. Mapeamento e Persistência
        Viagem novaViagem = new Viagem();
        novaViagem.origem = input.origem;
        novaViagem.destino = input.destino;
        novaViagem.dataPartida = input.dataPartida;
        novaViagem.motorista = motorista;
        novaViagem.onibus = onibus;

        // 4. Dados de Auditoria
        novaViagem.apiVersion = apiVersion;
        novaViagem.idempotencyKey = idempotencyKey;

        novaViagem.persist();

        return Response.status(Response.Status.CREATED)
                .entity(ViagemRepresentation.from(novaViagem))
                .build();
    }

    // --- PUT (Atualização) ---
    @PUT
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Atualiza uma viagem existente (V1)")
    @APIResponse(responseCode = "404", description = "Viagem, Motorista ou Ônibus não encontrados.")
    public Response update(@PathParam("id") Long id, @Valid ViagemInputDTO dados) {
        Viagem v = Viagem.findById(id);
        if (v == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        // 1. Carregamento de Relacionamentos (simples, sem checagem de 404 completa para simplificar o PUT)
        Motorista motorista = Motorista.findById(dados.motoristaId);
        Onibus onibus = Onibus.findById(dados.onibusId);

        if (motorista == null || onibus == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("message", "Motorista ou Ônibus referenciado não encontrado."))
                    .build();
        }

        // 2. Atualização dos campos validados
        v.origem = dados.origem;
        v.destino = dados.destino;
        v.dataPartida = dados.dataPartida;
        v.motorista = motorista;
        v.onibus = onibus;

        return Response.ok(ViagemRepresentation.from(v)).build();
    }

    // --- DELETE ---
    @DELETE
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Cancela uma viagem por ID (V1)")
    @APIResponse(responseCode = "204", description = "Cancelamento bem-sucedido.")
    @APIResponse(responseCode = "404", description = "Viagem não encontrada.")
    public Response delete(@PathParam("id") Long id) {
        boolean deleted = Viagem.deleteById(id);
        return deleted ? Response.noContent().build() : Response.status(Response.Status.NOT_FOUND).build();
    }
}