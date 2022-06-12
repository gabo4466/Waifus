package com.waifus.dao;

import java.sql.SQLException;
import java.util.ArrayList;

public interface ObjectInteractionDao<G, T> {
    public G add(G obj1,T obj2) throws SQLException, Exception;
    public boolean delete(G obj1, T obj2) throws SQLException;
    public ArrayList<G> search(int idx, int pag) throws SQLException;
}
