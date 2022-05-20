package com.waifus.dao;


import java.sql.SQLException;
import java.util.ArrayList;

public interface GenericDao<G> {
    public boolean update(G obj);
    public boolean delete(G obj);
    public G add(G obj) throws SQLException, Exception ;
    public ArrayList<G> getAll(int idx, int pag);
    public ArrayList<G> search(int idx, int pag, String term);
    public G get(int id);
}
