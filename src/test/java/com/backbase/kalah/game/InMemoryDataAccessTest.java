package com.backbase.kalah.game;

import com.backbase.kalah.exceptions.GameNotFoundException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class InMemoryDataAccessTest {
    private InMemoryDataAccess dataAccess;

    @Before
    public void setUp() {
        dataAccess = InMemoryDataAccess.getInstance();
    }

    @Test
    public void create() {
        assertEquals(0, dataAccess.create());
        assertEquals(1, dataAccess.create());
    }

    @Test
    public void delete() {
        dataAccess.delete(0);
        dataAccess.create();
        dataAccess.delete(0);
    }

    @Test(expected = GameNotFoundException.class)
    public void getInvalid() {
        dataAccess.get(0);
    }

    @Test
    public void getValid() {
        int gameId = dataAccess.create();
        Controller controller = dataAccess.get(gameId);
        assertNotNull(controller);
        assertEquals(gameId, controller.getGameId());
    }
}