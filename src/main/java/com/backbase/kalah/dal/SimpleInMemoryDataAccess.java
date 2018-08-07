package com.backbase.kalah.dal;

import com.backbase.kalah.entity.Board;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleInMemoryDataAccess implements DataAccess {
    private ConcurrentHashMap<Integer, Board> inMemoryData = new ConcurrentHashMap<>();

    @Override
    public void save(final Board board) throws NullPointerException {
        if (board == null)
            throw new NullPointerException("pits is null");

        inMemoryData.putIfAbsent(board.getId(), board);
    }

    @Override
    public Optional<Board> load(final int id) {
        return Optional.ofNullable(inMemoryData.get(id));
    }

    @Override
    public void delete(int id) {
    }
}
