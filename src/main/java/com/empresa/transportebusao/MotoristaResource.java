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

@Tag(name = "Motoristas") // Tag única para evitar duplicação no Swagger
@Path("/api/v1/motoristas") // Requisito 4.5: Versionamento V1
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MotoristaResource {

    // --- GET (Lista todos) ---
    @GET
    @Operation(summary = "Lista todos os motoristas (V1)")
    public List<MotoristaRepresentation> listAll() {
        return Motorista.<Motorista>listAll().stream()
                .map(MotoristaRepresentation::from) // Sem HATEOAS
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
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(MotoristaRepresentation.from(motorista)).build();
    }

    // --- GET (Busca por Tipo de Ônibus Habilitado) ---
    // Este método contém a correção para a busca por "Comum" ou "Articulado"
    @GET
    @Path("/search")
    @Operation(summary = "Busca motoristas habilitados para um tipo específico de ônibus (Comum ou Articulado) (V1)")
    public List<MotoristaRepresentation> searchByTipoHabilitacao(
            @QueryParam("tipo")
            @Parameter(description = "Tipo de ônibus para o qual o motorista está habilitado (Comum ou Articulado).")
            String tipo
    ) {
        if (tipo == null || tipo.isBlank()) {
            return listAll();
        }

        // 1. Normalização da String de Entrada: Garante que a primeira letra seja maiúscula (Ex: comum -> Comum)
        String tipoNormalizado = tipo.substring(0, 1).toUpperCase() + tipo.substring(1).toLowerCase();

        // 2. Validação estrita dos termos permitidos
        if (!tipoNormalizado.equals("Comum") && !tipoNormalizado.equals("Articulado")) {
            return List.of();
        }

        // 3. Busca exata no Panache
        // Usa a busca exata (find) com o valor normalizado
        return Motorista.<Motorista>find("tipoHabilitacaoOnibus", tipoNormalizado)
                .stream()
                .map(MotoristaRepresentation::from)
                .collect(Collectors.toList());
    }

    // --- POST (Criação Idempotente e Versionada) ---
    @POST
    @Transactional
    @Operation(
            summary = "Cria um novo motorista (V1)",
            description = "A operação suporta Idempotência via cabeçalho 'Idempotency-Key'."
    )
    @APIResponse(responseCode = "201", description = "Motorista criado com sucesso.")
    @APIResponse(responseCode = "400", description = "Dados de entrada inválidos (Bean Validation).")
    @APIResponse(responseCode = "409", description = "Operação já processada com esta Idempotency-Key.")
    public Response create(
            @Valid MotoristaInputDTO input,
            @Parameter(description = "Chave única para Idempotência.")
            @HeaderParam("Idempotency-Key") String idempotencyKey,
            @HeaderParam("X-API-Version") @DefaultValue("v1") String apiVersion) {

        // 1. Lógica de Idempotência
        if (idempotencyKey != null && !idempotencyKey.isBlank()) {
            if (Motorista.find("idempotencyKey", idempotencyKey).firstResultOptional().isPresent()) {
                return Response.status(409)
                        .entity(Map.of("message", "Operação já processada com esta Idempotency-Key."))
                        .build();
            }
        }

        // 2. Mapeamento e Persistência (Incluindo o novo campo)
        Motorista novo = new Motorista();
        novo.nome = input.nome;
        novo.cnh = input.cnh;
        novo.cpf = input.cpf;
        novo.tipoHabilitacaoOnibus = input.tipoHabilitacaoOnibus; // NOVO CAMPO

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
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        // Atualização dos campos validados (Incluindo o novo campo)
        m.nome = dados.nome;
        m.cnh = dados.cnh;
        m.cpf = dados.cpf;
        m.tipoHabilitacaoOnibus = dados.tipoHabilitacaoOnibus; // NOVO CAMPO

        return Response.ok(MotoristaRepresentation.from(m)).build();
    }

    // --- DELETE ---
    @DELETE
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Remove um motorista por ID (V1)")
    @APIResponse(responseCode = "204", description = "Remoção bem-sucedida.")
    @APIResponse(responseCode = "404", description = "Motorista não encontrado.")
    public Response delete(@PathParam("id") Long id) {
        boolean deleted = Motorista.deleteById(id);
        return deleted ? Response.noContent().build() : Response.status(Response.Status.NOT_FOUND).build();
    }
}