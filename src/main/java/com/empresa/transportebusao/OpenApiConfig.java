package com.empresa.transportebusao;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@ApplicationPath("/") // Define o caminho base da sua API
@OpenAPIDefinition(
        info = @Info(
                title = "Metropole ts",
                version = "1.0.0",
                description = "API RESTful para gerenciar motoristas, ônibus e viagens, incluindo suporte HATEOAS.",
                contact = @Contact(
                        name = "Equipe de Desenvolvimento",
                        email = "dev@transportebusao.com"
                )
        ),
        tags = {
                @Tag(name = "Motoristas", description = "Operações de CRUD e HATEOAS para a entidade Motorista."),
                @Tag(name = "Onibus", description = "Operações de CRUD e HATEOAS para a entidade Ônibus."),
                @Tag(name = "Viagens", description = "Operações de CRUD e HATEOAS para a entidade Viagem.")
        }
)
public class OpenApiConfig extends Application {
    // Esta classe não precisa de conteúdo, serve apenas para hospedar as anotações.
}