package com.empresa.transportebusao;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;



@Tag(name = "Onibus")
@Path("/onibus")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OnibusResource {

    // LISTAR TODOS (Original)
    @GET
    public List<Onibus> listAll() {
        return Onibus.listAll();
    }

    // BUSCAR POR ID (Original)
    @GET
    @Path("/{id}")
    public Onibus findById(@PathParam("id") Long id) {
        return Onibus.findById(id);
    }

    // CRIAR (Original)
    @POST
    @Transactional
    public Response create(Onibus onibus) {
        onibus.persist();
        return Response.status(Response.Status.CREATED).entity(onibus).build();
    }

    // ATUALIZAR (Original)
    @PUT
    @Path("/{id}")
    @Transactional
    public Response update(@PathParam("id") Long id, Onibus dadosAtualizados) {
        Onibus onibus = Onibus.findById(id);
        if (onibus == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        onibus.placa = dadosAtualizados.placa;
        onibus.anoFabricacao = dadosAtualizados.anoFabricacao;
        onibus.capacidadePassageiros = dadosAtualizados.capacidadePassageiros;
        return Response.ok(onibus).build();
    }

    // DELETAR (Original)
    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        boolean deleted = Onibus.deleteById(id);
        if (deleted) {
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }


    // MÉTODO CORRIGIDO: BUSCA AVANÇADA E PAGINAÇÃO (GET /onibus/search?q=...)
    @GET
    @Path("/search")
    public BuscaOnibusResponse search( // <-- TIPO DE RETORNO CORRIGIDO
                                       @QueryParam("q") @DefaultValue("") String q,
                                       @QueryParam("sort") @DefaultValue("id") String sort,
                                       @QueryParam("direction") @DefaultValue("Ascending") String direction,
                                       @QueryParam("page") @DefaultValue("0") int page,
                                       @QueryParam("size") @DefaultValue("10") int size)
    {
        // 1. Configurar Ordenação
        Sort panacheSort = Sort.by(sort, Sort.Direction.valueOf(direction.toUpperCase()));

        // 2. Definir a Consulta (Query)
        String panacheQuery = "placa LIKE ?1 OR CAST(anoFabricacao AS string) LIKE ?1";
        String searchTerm = "%" + q + "%";

        PanacheQuery<Onibus> query = Onibus.find(panacheQuery, panacheSort, searchTerm);

        // 3. Aplicar Paginação
        query = query.page(Page.of(page, size));

        // 4. Obter resultados e metadados
        List<Onibus> onibusList = query.list();
        long totalElements = query.count();
        int totalPages = query.pageCount();

        // 5. Construir e Retornar a Resposta Paginada
        return BuscaOnibusResponse.from( // <-- MÉTODO DE CONSTRUÇÃO CORRIGIDO
                onibusList,
                q,
                sort,
                direction,
                page,
                size,
                totalElements,
                totalPages
        );
    }
}