package com.backbase.kalah.ui;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController("kalah-rest-api")
public class RestUserInterface {
    @PostMapping(path = "/games", consumes = "application/json", produces = "application/json")
    public String createGame(HttpServletRequest request) {
        return "";
    }

    @PutMapping(path = "/games/{gameId}/pits/{pitId}", consumes = "application/json", produces = "application/json")
    public String move(HttpServletRequest request,
                       @RequestParam(name = "gameId") String gameId,
                       @RequestParam(name = "pitId") String pitId) {
        return "";
    }
}
