package com.waifus.dao;

import com.waifus.exceptions.UserException;

import java.sql.SQLException;
import java.util.ArrayList;

public interface GenericDao<G> {
    public boolean update(G obj);
    public boolean delete(G obj);
    public boolean add(G obj);
    public ArrayList<G> getAll(int idx, int pag);
    public ArrayList<G> search(int idx, int pag, String term);
    public G get(int id) throws SQLException, Exception;
}
