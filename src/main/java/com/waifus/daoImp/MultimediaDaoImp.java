package com.waifus.daoImp;

import com.waifus.dao.GenericDao;
import com.waifus.exceptions.ChannelNotFoundException;
import com.waifus.exceptions.UserException;
import com.waifus.model.Multimedia;
import com.waifus.services.DBConnection;
import com.waifus.services.PropertiesService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

public class MultimediaDaoImp implements GenericDao<Multimedia> {
    private Connection connection;
    public static MultimediaDaoImp instance = null;
    public static Properties prop;

    private MultimediaDaoImp() throws SQLException, ClassNotFoundException {
        this.connection = DBConnection.getConnection();
        prop = PropertiesService.getProperties("config_es");
    }

    public static MultimediaDaoImp getInstance() throws SQLException, ClassNotFoundException {
        if (instance == null){
            instance = new MultimediaDaoImp();
        }
        return instance;
    }

    @Override
    public boolean update(Multimedia obj) throws SQLException, Exception {
        return false;
    }

    @Override
    public boolean delete(Multimedia obj) throws SQLException, UserException {
        return false;
    }

    @Override
    public Multimedia add(Multimedia obj) throws SQLException, Exception {
        return null;
    }

    @Override
    public ArrayList<Multimedia> getAll() throws SQLException, ChannelNotFoundException {
        return null;
    }

    @Override
    public ArrayList<Multimedia> search(int idx, int pag, String term) throws SQLException {
        return null;
    }

    @Override
    public Multimedia get(int id) throws SQLException, Exception {
        return null;
    }

    @Override
    public int count(String term) throws SQLException {
        return 0;
    }
}
