package com.backbase.kalah.dal;

import com.backbase.kalah.entity.Board;

import java.util.Optional;

public interface DataAccess {
    void save(final Board board) throws NullPointerException;
    void delete(final int id);
    Optional<Board> load(final int id);
}
