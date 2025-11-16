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

@Tag(name = "Motoristas") // Corrigido para uma única Tag (Requisito 6)
@Path("/api/v1/motoristas") // Requisito 4.5: Versionamento via URL (V1)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MotoristaResource {

    // --- GET (Lista todos) ---
    @GET
    @Operation(summary = "Lista todos os motoristas (V1)")
    public List<MotoristaRepresentation> listAll() {
        return Motorista.<Motorista>listAll().stream()
                .map(MotoristaRepresentation::from) // Simplificado: sem HATEOAS
                .collect(Collectors.toList());
    }

    // --- GET por ID ---
    @GET
    @Path("/{id}")
    @Operation(summary = "Busca um motorista por ID (V1)")
    @APIResponse(responseCode = "404", description = "Motorista não encontrado.")
    public Response findById(@PathParam("id") Long id) {
        Motorista motorista = Motorista.findById(id);
        if (motorista == null) {
            return Response.status(Response.Status.NOT_FOUND).build(); // Requisito 5: HTTP 404
        }
        return Response.ok(MotoristaRepresentation.from(motorista)).build();
    }

    // --- POST (Criação Idempotente e Versionada) ---
    @POST
    @Transactional
    @Operation(
            summary = "Cria um novo motorista (V1)",
            description = "A operação suporta Idempotência via cabeçalho 'Idempotency-Key'."
    )
    @APIResponse(responseCode = "201", description = "Motorista criado com sucesso.")
    @APIResponse(responseCode = "400", description = "Dados de entrada inválidos (Bean Validation).") // Requisito 5
    @APIResponse(responseCode = "409", description = "Operação já processada com esta Idempotency-Key.") // Requisito 4.1
    @APIResponse(responseCode = "429", description = "Limite de requisições excedido (Rate Limit).") // Requisito 4.3 (Documentação)
    public Response create(
            // Requisito 5: Validação com @Valid e DTO
            @Valid MotoristaInputDTO input,

            // Requisito 4.1: Captura a chave de Idempotência
            @Parameter(description = "Chave única para Idempotência.")
            @HeaderParam("Idempotency-Key") String idempotencyKey,

            // Requisito 4.5: Captura a versão para auditoria
            @HeaderParam("X-API-Version") @DefaultValue("v1") String apiVersion) {

        // 1. Lógica de Idempotência (Simulação via DB)
        if (idempotencyKey != null && !idempotencyKey.isBlank()) {
            if (Motorista.find("idempotencyKey", idempotencyKey).firstResultOptional().isPresent()) {
                return Response.status(409)
                        .entity(Map.of("message", "Operação já processada com esta Idempotency-Key."))
                        .build();
            }
        }

        // 2. Mapeamento e Persistência
        Motorista novo = new Motorista();
        novo.nome = input.nome;
        novo.cnh = input.cnh;
        novo.cpf = input.cpf;

        // 3. Dados de Auditoria
        novo.apiVersion = apiVersion;
        novo.idempotencyKey = idempotencyKey;

        novo.persist();

        return Response.status(Response.Status.CREATED)
                .entity(MotoristaRepresentation.from(novo))
                .build();
    }

    // --- PUT (Atualização) ---
    @PUT
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Atualiza um motorista existente (V1)")
    @APIResponse(responseCode = "404", description = "Motorista não encontrado.")
    public Response update(@PathParam("id") Long id, @Valid MotoristaInputDTO dados) {
        Motorista m = Motorista.findById(id);
        if (m == null) {
            return Response.status(Response.Status.NOT_FOUND).build(); // Requisito 5: HTTP 404
        }

        // Atualização dos campos validados
        m.nome = dados.nome;
        m.cnh = dados.cnh;
        m.cpf = dados.cpf;

        return Response.ok(MotoristaRepresentation.from(m)).build();
    }

    // --- DELETE ---
    @DELETE
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Remove um motorista por ID (V1)")
    @APIResponse(responseCode = "204", description = "Remoção bem-sucedida.")
    @APIResponse(responseCode = "404", description = "Motorista não encontrado.") // Requisito 5
    public Response delete(@PathParam("id") Long id) {
        boolean deleted = Motorista.deleteById(id);
        return deleted ? Response.noContent().build() : Response.status(Response.Status.NOT_FOUND).build();
    }
}