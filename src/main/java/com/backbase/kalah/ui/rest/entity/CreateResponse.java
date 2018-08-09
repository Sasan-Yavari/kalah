package com.backbase.kalah.ui.rest.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"id", "uri"})
public class CreateResponse {
    @JsonProperty
    private String id;

    @JsonProperty
    private String uri;

    public CreateResponse(int id, String uri) {
        this.id = String.valueOf(id);
        this.uri = uri;
    }
}
