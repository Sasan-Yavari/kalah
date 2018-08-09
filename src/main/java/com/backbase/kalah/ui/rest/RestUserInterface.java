package com.backbase.kalah.ui.rest;

import com.backbase.kalah.game.Controller;
import com.backbase.kalah.game.DataAccess;
import com.backbase.kalah.game.InMemoryDataAccess;
import com.backbase.kalah.ui.rest.entity.CreateResponse;
import com.backbase.kalah.ui.rest.entity.MoveResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Api(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@RestController("kalah-rest-api")
@RequestMapping(path = "/games", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class RestUserInterface {
    private static final String URI_TEMPLATE = "%s://%s:%d%s/%d";

    private DataAccess dataAccess = InMemoryDataAccess.getInstance();

    @ApiOperation(value = "Creates a new game")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "")
    public CreateResponse createGame(HttpServletRequest request) {
        int gameId = dataAccess.create();

        return new CreateResponse(gameId, String.format(URI_TEMPLATE,
                request.getScheme(),
                request.getServerName(),
                request.getServerPort(),
                request.getContextPath() + request.getServletPath(),
                gameId));
    }

    @ApiOperation(value = "Moves stones of selected pitId in the selected gameId and returns the final status of the game")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping(path = "/{gameId}/pits/{pitId}")
    public MoveResponse move(HttpServletRequest request,
                             @PathVariable(name = "gameId") final int gameId,
                             @PathVariable(name = "pitId") final int pitId) {
        Controller controller = dataAccess.get(gameId);
        controller.move(pitId - 1);
        dataAccess.save(controller);

        return new MoveResponse(gameId, String.format(URI_TEMPLATE,
                request.getScheme(),
                request.getServerName(),
                request.getServerPort(),
                request.getContextPath() + request.getServletPath(),
                gameId), controller);
    }
}
