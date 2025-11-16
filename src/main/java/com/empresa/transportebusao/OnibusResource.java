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

@Tag(name = "Onibus")
@Path("/api/v1/onibus") // 4.5. Versionamento via URL (V1)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OnibusResource {

    // ⚠️ ATENÇÃO: Esta classe OnibusInputDTO DEVE estar em um arquivo separado: OnibusInputDTO.java
    // (Incluindo os imports de Bean Validation @NotBlank, @Min, etc.)

    // --- GET (Lista todos) ---
    @GET
    @Operation(summary = "Lista todos os ônibus (V1)")
    public List<OnibusRepresentation> listAll() {
        return Onibus.<Onibus>listAll().stream()
                .map(OnibusRepresentation::fromWithLinks)
                .collect(Collectors.toList());
    }

    // --- GET por ID ---
    @GET
    @Path("/{id}")
    @Operation(summary = "Busca um ônibus por ID (V1)")
    @APIResponse(responseCode = "404", description = "Ônibus não encontrado.")
    public Response findById(@PathParam("id") Long id) {
        Onibus onibus = Onibus.findById(id);
        if (onibus == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(OnibusRepresentation.fromWithLinks(onibus)).build();
    }

    // --- POST (Criação Idempotente e Versionada) ---
    @POST
    @Transactional
    @Operation(
            summary = "Cria um novo ônibus (V1)",
            description = "A operação suporta Idempotência via cabeçalho 'Idempotency-Key'."
    )
    @APIResponse(responseCode = "201", description = "Ônibus criado com sucesso.")
    @APIResponse(responseCode = "400", description = "Dados de entrada inválidos (Bean Validation).")
    @APIResponse(responseCode = "409", description = "Operação já processada com esta Idempotency-Key.")
    public Response create(
            @Valid OnibusInputDTO input,
            @Parameter(description = "Chave única para Idempotência.")
            @HeaderParam("Idempotency-Key") String idempotencyKey,
            @HeaderParam("X-API-Version") @DefaultValue("v1") String apiVersion) {

        // 1. Simulação da Lógica de Idempotência (4.1)
        if (idempotencyKey != null && !idempotencyKey.isBlank()) {
            if (Onibus.find("idempotencyKey", idempotencyKey).firstResultOptional().isPresent()) {
                return Response.status(409)
                        .entity(Map.of("message", "Operação já processada com esta Idempotency-Key."))
                        .build();
            }
        }

        // 2. Mapeamento do DTO para o Modelo (CORREÇÃO DE ERRO)
        Onibus novo = new Onibus();
        novo.modelo = input.modelo;
        novo.placa = input.placa;
        novo.capacidade = input.capacidade;

        // 3. Dados de Auditoria
        novo.apiVersion = apiVersion;
        novo.idempotencyKey = idempotencyKey;

        novo.persist();

        return Response.status(Response.Status.CREATED)
                .entity(OnibusRepresentation.fromWithLinks(novo))
                .build();
    }

    // --- PUT (Atualização) ---
    @PUT
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Atualiza um ônibus existente (V1)")
    @APIResponse(responseCode = "404", description = "Ônibus não encontrado.")
    public Response update(@PathParam("id") Long id, @Valid OnibusInputDTO dados) {
        Onibus o = Onibus.findById(id);
        if (o == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        o.modelo = dados.modelo;
        o.placa = dados.placa;
        o.capacidade = dados.capacidade;

        return Response.ok(OnibusRepresentation.fromWithLinks(o)).build();
    }

    // --- DELETE ---
    @DELETE
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Remove um ônibus por ID (V1)")
    @APIResponse(responseCode = "204", description = "Remoção bem-sucedida.")
    @APIResponse(responseCode = "404", description = "Ônibus não encontrado.")
    public Response delete(@PathParam("id") Long id) {
        boolean deleted = Onibus.deleteById(id);
        return deleted ? Response.noContent().build() : Response.status(Response.Status.NOT_FOUND).build();
    }
}