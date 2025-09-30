package com.empresa.transportebusao;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

public class Link {

    @Schema(example = "http://localhost:8080/motoristas/1")
    public String href;

    @Schema(example = "GET")
    public String method;

    public Link(String href, String method) {
        this.href = href;
        this.method = method;
    }
}