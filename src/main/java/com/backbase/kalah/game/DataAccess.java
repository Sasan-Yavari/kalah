package com.backbase.kalah.game;

public interface DataAccess {
    /**
     * Creates a new record inside the data storage
     *
     * @return new record's id
     */
    int create();

    /**
     * Removes a record from the data storage
     *
     * @param id id of the record to delete
     */
    void delete(final int id);

    /**
     * Saves changes of the record inside the data storage
     *
     * @param controller instance of {@link Controller} to save
     */
    void save(final Controller controller);

    /**
     * Retrieves a record from data storage using given id and returns it
     *
     * @param id id of the record
     * @return Founded record
     */
    Controller get(final int id);
}
