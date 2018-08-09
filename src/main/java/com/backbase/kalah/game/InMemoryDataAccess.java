package com.backbase.kalah.game;

import com.backbase.kalah.exceptions.GameNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class implements {@link DataAccess} interface.
 * We can implement this interface to create ant type of data storage for our game.
 * For example we can implement an oracle data handler using any type of framework like JPA.
 *
 * This implementation is a dummy one that simply stores the game inside the memory.
 */
public class InMemoryDataAccess implements DataAccess {
    private static final InMemoryDataAccess instance = new InMemoryDataAccess();
    private static final String LOG_CREATE_TEMPLATE = "[New game created] [GameId: %d]";
    private static final String LOG_DELETE_TEMPLATE = "[Game deleted] [GameId: %d]";
    private static final String LOG_SAVE_TEMPLATE = "[Game saved] [GameId: %d]";

    private static final int PIT_COUNT = 14;
    private static final int STONE_COUNT = 6;

    private Logger logger = LoggerFactory.getLogger(InMemoryDataAccess.class);
    private ConcurrentHashMap<Integer, Controller> dataStorage = new ConcurrentHashMap<>();
    private AtomicInteger idRepo = new AtomicInteger(0);

    public static InMemoryDataAccess getInstance() {
        return instance;
    }

    private InMemoryDataAccess() {
    }

    @Override
    public int create() {
        int gameId = idRepo.getAndIncrement();

        BoardEntity boardEntity = new BoardEntity(PIT_COUNT, STONE_COUNT);
        Controller controller = new Controller(gameId, boardEntity);

        dataStorage.put(gameId, controller);
        logger.info(String.format(LOG_CREATE_TEMPLATE, gameId));

        return gameId;
    }

    @Override
    public void delete(int gameId) {
        if (dataStorage.remove(gameId) != null)
            logger.info(String.format(LOG_DELETE_TEMPLATE, gameId));
    }

    /**
     * Since this is an in-memory storage we don't need save method to do anything
     * because, every changes on an object is directly done on the final object.
     *
     * @param controller the game controller object
     */
    @Override
    public void save(Controller controller) {
        logger.info(String.format(LOG_SAVE_TEMPLATE, controller.getGameId()));
    }

    @Override
    public Controller get(int id) {
        if (!dataStorage.containsKey(id))
            throw new GameNotFoundException("Selected game was not found");

        return dataStorage.get(id);
    }
}
