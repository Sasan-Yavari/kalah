package com.backbase.kalah.ui.rest.entity;

import com.backbase.kalah.game.Controller;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@JsonPropertyOrder({"id", "url", "status"})
public class MoveResponse {
    @JsonProperty
    private String id;

    @JsonProperty
    private String url;

    @JsonProperty
    private Map<String, String> status;

    public MoveResponse(int id, String url, Controller controller) {
        this.id = String.valueOf(id);
        this.url = url;
        this.status = new HashMap<>();

        AtomicInteger idx = new AtomicInteger(1);
        controller.streamBoard().forEach(stoneCount -> status.put(String.valueOf(idx.getAndIncrement()), String.valueOf(stoneCount)));
    }
}
