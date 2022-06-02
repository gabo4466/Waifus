package com.waifus.daoImp;

import com.waifus.dao.GenericDao;
import com.waifus.exceptions.ThreadNotFoundException;
import com.waifus.model.Channel;
import com.waifus.services.DBConnection;
import com.waifus.services.PropertiesService;
import com.waifus.model.Thread;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

public class ThreadDaoImp implements GenericDao<Thread> {
    private Connection connection;
    public static ThreadDaoImp instance = null;
    public static Properties prop;

    private ThreadDaoImp() throws SQLException, ClassNotFoundException {
        this.connection = DBConnection.getConnection();
        prop = PropertiesService.getProperties("config_es");
    }

    public static ThreadDaoImp getInstance() throws SQLException, ClassNotFoundException {
        if (instance == null){
            instance = new ThreadDaoImp();
        }
        return instance;
    }

    @Override
    public boolean update(Thread thread) {
        return false;
    }

    @Override
    public boolean delete(Thread thread) {
        return false;
    }

    @Override
    public Thread add(Thread thread) {
        return null;
    }

    @Override
    public ArrayList<Thread> getAll() {
        return null;
    }

    @Override
    public ArrayList<Thread> search(int idx, int pag, String term) throws SQLException {
        ArrayList<Thread> result = new ArrayList<Thread>();
        PreparedStatement stmt;
        String queryNoTerm = "select id_thread, date_thread, name, content, fk_user, fk_channel from waifus.threads where name limit ?,?;";
        String queryTerm = "select id_thread, date_thread, name, content, fk_user, fk_channel from waifus.threads where name like '%'+?+'%' and deleted=0 limit ?,?;";
        if(term.equals("")){
            stmt = this.connection.prepareStatement(queryNoTerm);
            stmt.setInt(1, idx);
            stmt.setInt(2, idx+pag);
        }else {
            stmt = this.connection.prepareStatement(queryTerm);
            stmt.setString(1, term);
            stmt.setInt(2, idx);
            stmt.setInt(3, idx+pag);
        }
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            result.add(new Thread(rs.getInt("id_thread"), rs.getString("date_thread"),
                    rs.getString("name"), rs.getString("content"),
                    rs.getInt("fk_user"), rs.getInt("fk_channel")));
        }
        return result;
    }

    @Override
    public Thread get(int id) throws SQLException, ThreadNotFoundException {
        Thread result=null;
        String query = "select id_thread, date_thread, name, content, fk_user, deleted, fk_channel from waifus.threads where id_thread=?";
        PreparedStatement stmt = this.connection.prepareStatement(query);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()){
            result = new Thread(rs.getInt("id_thread"), rs.getString("date_thread"),
                    rs.getString("name"), rs.getString("content"),
                    rs.getBoolean("deleted"), rs.getInt("fk_user"),
                    rs.getInt("fk_channel"));
        }else{
            result = null;
            throw new ThreadNotFoundException(prop.getProperty("error.generic"));
        }
        return result;
    }

    @Override
    public int count(String term) throws SQLException {
        int result = 0;
        PreparedStatement stmt;
        String queryNoTerm = "select count(*) as Quantity from waifus.threads where deleted=0;";
        String queryTerm = "select count(*) as Quantity from waifus.threads where name like '%'+?+'%' and deleted=0;";
        if(term.equals("")){
            stmt = this.connection.prepareStatement(queryNoTerm);
        }else {
            stmt = this.connection.prepareStatement(queryTerm);
            stmt.setString(1, term);
        }
        ResultSet rs = stmt.executeQuery();
        if(rs.next()){
            result = rs.getInt("Quantity");
        }
        return result;
    }
}
