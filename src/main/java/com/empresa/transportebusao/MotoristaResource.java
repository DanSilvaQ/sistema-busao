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
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.headers.Header;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Tag(name = "Motoristas")
@Path("/api/v1/motoristas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MotoristaResource {

    // --- GET (Lista todos) ---
    @GET
    @Operation(
            summary = "Lista todos os motoristas (V1)",
            description = "Lista todos os motoristas cadastrados. Este endpoint está sujeito a rate limiting (10 requisições por hora)."
    )
    @APIResponse(
            responseCode = "200",
            description = "Lista de motoristas retornada com sucesso",
            headers = {
                    @Header(
                            name = "X-RateLimit-Limit",
                            description = "Número máximo de requisições permitidas por hora",
                            schema = @Schema(implementation = Integer.class, example = "10")
                    ),
                    @Header(
                            name = "X-RateLimit-Remaining",
                            description = "Número de requisições restantes na janela atual",
                            schema = @Schema(implementation = Integer.class, example = "9")
                    ),
                    @Header(
                            name = "X-RateLimit-Reset",
                            description = "Timestamp Unix (em segundos) quando o limite será resetado",
                            schema = @Schema(implementation = Long.class, example = "1732305600")
                    )
            },
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = MotoristaRepresentation.class, type = SchemaType.ARRAY)
            )
    )
    @APIResponse(
            responseCode = "429",
            description = "Rate limit excedido. Você fez muitas requisições em um curto período.",
            headers = {
                    @Header(
                            name = "X-RateLimit-Limit",
                            description = "Número máximo de requisições permitidas por hora",
                            schema = @Schema(implementation = Integer.class, example = "10")
                    ),
                    @Header(
                            name = "X-RateLimit-Remaining",
                            description = "Número de requisições restantes (0 quando bloqueado)",
                            schema = @Schema(implementation = Integer.class, example = "0")
                    ),
                    @Header(
                            name = "X-RateLimit-Reset",
                            description = "Timestamp Unix quando o limite será resetado",
                            schema = @Schema(implementation = Long.class, example = "1732305600")
                    ),
                    @Header(
                            name = "Retry-After",
                            description = "Número de segundos até que você possa fazer uma nova requisição",
                            schema = @Schema(implementation = Long.class, example = "3540")
                    )
            },
            content = @Content(
                    mediaType = "application/json",
                    example = "{\"error\": \"Too Many Requests\", \"message\": \"Rate limit exceeded. Try again later.\"}"
            )
    )
    public List<MotoristaRepresentation> listAll() {
        return Motorista.<Motorista>listAll().stream()
                .map(MotoristaRepresentation::from)
                .collect(Collectors.toList());
    }

    // --- GET por ID ---
    @GET
    @Path("/{id}")
    @Operation(
            summary = "Busca um motorista por ID (V1)",
            description = "Busca um motorista específico por ID. Sujeito a rate limiting (10 requisições/hora)."
    )
    @APIResponse(
            responseCode = "200",
            description = "Motorista encontrado com sucesso",
            headers = {
                    @Header(
                            name = "X-RateLimit-Limit",
                            description = "Limite máximo de requisições",
                            schema = @Schema(implementation = Integer.class, example = "10")
                    ),
                    @Header(
                            name = "X-RateLimit-Remaining",
                            description = "Requisições restantes",
                            schema = @Schema(implementation = Integer.class, example = "8")
                    ),
                    @Header(
                            name = "X-RateLimit-Reset",
                            description = "Timestamp de reset",
                            schema = @Schema(implementation = Long.class, example = "1732305600")
                    )
            },
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = MotoristaRepresentation.class)
            )
    )
    @APIResponse(
            responseCode = "404",
            description = "Motorista não encontrado."
    )
    @APIResponse(
            responseCode = "429",
            description = "Rate limit excedido",
            headers = {
                    @Header(name = "Retry-After", schema = @Schema(implementation = Long.class, example = "3540"))
            },
            content = @Content(
                    mediaType = "application/json",
                    example = "{\"error\": \"Too Many Requests\", \"message\": \"Rate limit exceeded. Try again later.\"}"
            )
    )
    public Response findById(@PathParam("id") Long id) {
        Motorista motorista = Motorista.findById(id);
        if (motorista == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(MotoristaRepresentation.from(motorista)).build();
    }

    // --- GET (Busca por Tipo de Ônibus Habilitado) ---
    @GET
    @Path("/search")
    @Operation(
            summary = "Busca motoristas habilitados para um tipo específico de ônibus (Comum ou Articulado) (V1)",
            description = "Pesquisa motoristas por tipo de habilitação. Sujeito a rate limiting (10 requisições/hora)."
    )
    @APIResponse(
            responseCode = "200",
            description = "Resultados da busca retornados com sucesso",
            headers = {
                    @Header(name = "X-RateLimit-Limit", schema = @Schema(implementation = Integer.class, example = "10")),
                    @Header(name = "X-RateLimit-Remaining", schema = @Schema(implementation = Integer.class, example = "7")),
                    @Header(name = "X-RateLimit-Reset", schema = @Schema(implementation = Long.class, example = "1732305600"))
            },
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = MotoristaRepresentation.class, type = SchemaType.ARRAY)
            )
    )
    @APIResponse(
            responseCode = "429",
            description = "Rate limit excedido",
            headers = {
                    @Header(name = "Retry-After", schema = @Schema(implementation = Long.class, example = "3540"))
            },
            content = @Content(
                    mediaType = "application/json",
                    example = "{\"error\": \"Too Many Requests\", \"message\": \"Rate limit exceeded. Try again later.\"}"
            )
    )
    public List<MotoristaRepresentation> searchByTipoHabilitacao(
            @QueryParam("tipo")
            @Parameter(description = "Tipo de ônibus para o qual o motorista está habilitado (Comum ou Articulado).")
            String tipo
    ) {
        if (tipo == null || tipo.isBlank()) {
            return listAll();
        }

        String tipoNormalizado = tipo.substring(0, 1).toUpperCase() + tipo.substring(1).toLowerCase();

        if (!tipoNormalizado.equals("Comum") && !tipoNormalizado.equals("Articulado")) {
            return List.of();
        }

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

        if (idempotencyKey != null && !idempotencyKey.isBlank()) {
            if (Motorista.find("idempotencyKey", idempotencyKey).firstResultOptional().isPresent()) {
                return Response.status(409)
                        .entity(Map.of("message", "Operação já processada com esta Idempotency-Key."))
                        .build();
            }
        }

        Motorista novo = new Motorista();
        novo.nome = input.nome;
        novo.cnh = input.cnh;
        novo.cpf = input.cpf;
        novo.tipoHabilitacaoOnibus = input.tipoHabilitacaoOnibus;

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
    @Operation(
            summary = "Atualiza um motorista existente (V1)",
            description = "Atualiza os dados de um motorista. Sujeito a rate limiting (10 requisições/hora)."
    )
    @APIResponse(
            responseCode = "200",
            description = "Motorista atualizado com sucesso",
            headers = {
                    @Header(name = "X-RateLimit-Limit", schema = @Schema(implementation = Integer.class, example = "10")),
                    @Header(name = "X-RateLimit-Remaining", schema = @Schema(implementation = Integer.class, example = "6")),
                    @Header(name = "X-RateLimit-Reset", schema = @Schema(implementation = Long.class, example = "1732305600"))
            },
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = MotoristaRepresentation.class)
            )
    )
    @APIResponse(responseCode = "404", description = "Motorista não encontrado.")
    @APIResponse(
            responseCode = "429",
            description = "Rate limit excedido",
            headers = {
                    @Header(name = "Retry-After", schema = @Schema(implementation = Long.class, example = "3540"))
            }
    )
    public Response update(@PathParam("id") Long id, @Valid MotoristaInputDTO dados) {
        Motorista m = Motorista.findById(id);
        if (m == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        m.nome = dados.nome;
        m.cnh = dados.cnh;
        m.cpf = dados.cpf;
        m.tipoHabilitacaoOnibus = dados.tipoHabilitacaoOnibus;

        return Response.ok(MotoristaRepresentation.from(m)).build();
    }

    // --- DELETE ---
    @DELETE
    @Path("/{id}")
    @Transactional
    @Operation(
            summary = "Remove um motorista por ID (V1)",
            description = "Exclui um motorista do sistema. Sujeito a rate limiting (10 requisições/hora)."
    )
    @APIResponse(
            responseCode = "204",
            description = "Remoção bem-sucedida.",
            headers = {
                    @Header(name = "X-RateLimit-Limit", schema = @Schema(implementation = Integer.class, example = "10")),
                    @Header(name = "X-RateLimit-Remaining", schema = @Schema(implementation = Integer.class, example = "5")),
                    @Header(name = "X-RateLimit-Reset", schema = @Schema(implementation = Long.class, example = "1732305600"))
            }
    )
    @APIResponse(responseCode = "404", description = "Motorista não encontrado.")
    @APIResponse(
            responseCode = "429",
            description = "Rate limit excedido",
            headers = {
                    @Header(name = "Retry-After", schema = @Schema(implementation = Long.class, example = "3540"))
            }
    )
    public Response delete(@PathParam("id") Long id) {
        boolean deleted = Motorista.deleteById(id);
        return deleted ? Response.noContent().build() : Response.status(Response.Status.NOT_FOUND).build();
    }
}
