package com.waifus.daoImp;

import com.waifus.dao.GenericDao;
import com.waifus.exceptions.ThreadException;
import com.waifus.exceptions.ThreadNotFoundException;
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
    public boolean update(Thread thread) throws SQLException, ThreadException {
        boolean result;
        String query = "update waifus.threads set deleted=?, content=?, name=? where id_thread=?;";
        PreparedStatement stmt = this.connection.prepareStatement(query);
        stmt.setBoolean(1, thread.isDeleted());
        stmt.setString(2, thread.getContent());
        stmt.setString(5, thread.getName());
        stmt.setInt(6, thread.getIdThread());
        int rs = stmt.executeUpdate();
        if(rs>0){
            result = true;
        }else {
            result = false;
            throw new ThreadException(prop.getProperty("error.generic"));
        }
        return result;
    }

    @Override
    public boolean delete(Thread thread) throws SQLException, ThreadException {
        boolean result;
        String query = "update waifus.threads set deleted=? where id_thread=?;";
        PreparedStatement stmt = this.connection.prepareStatement(query);
        stmt.setBoolean(1, thread.isDeleted());
        stmt.setInt(2, thread.getIdThread());
        int rs = stmt.executeUpdate();
        if(rs>0){
            result = true;
        }else {
            result = false;
            throw new ThreadException(prop.getProperty("error.generic"));
        }
        return result;
    }

    @Override
    public Thread add(Thread thread) throws ThreadException, SQLException, ThreadNotFoundException {
        Thread result;
        String query = "insert into waifus.threads (content, fk_channel, name, date_thread, fk_user, deleted) values (?,?,?,?,?,0);";
        PreparedStatement stmt = this.connection.prepareStatement(query);
        stmt.setString(1, thread.getContent());
        stmt.setInt(2, thread.getChannel());
        stmt.setString(3, thread.getName());
        stmt.setString(4, thread.getDateThread());
        stmt.setInt(5, thread.getUser());
        int rs = stmt.executeUpdate();
        if (rs>0){
            query = "select id_thread from waifus.threads where name=? and fk_channel=? and fk_user=? and deleted=0;";
            stmt = this.connection.prepareStatement(query);
            stmt.setString(1, thread.getName());
            stmt.setInt(2, thread.getChannel());
            stmt.setInt(3, thread.getUser());
            ResultSet rs2 = stmt.executeQuery();
            if (rs2.next()){
                result = this.get(rs2.getInt("id_thread"));
            }else {
                result = null;
                throw new ThreadException(prop.getProperty("error.generic"));
            }
        }else {
            result = null;
            throw new ThreadException(prop.getProperty("error.generic"));
        }
        return result;
    }

    @Override
    public ArrayList<Thread> getAll() throws SQLException {
        ArrayList<Thread> result = new ArrayList<Thread>();
        String query = "select id_thread, date_thread, name, content, fk_user, fk_channel from waifus.threads;";
        PreparedStatement stmt = this.connection.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            result.add(new Thread(rs.getInt("id_thread"), rs.getString("date_thread"),
                    rs.getString("name"), rs.getString("content"),
                    rs.getInt("fk_user"), rs.getInt("fk_channel")));
        }
        return result;
    }

    @Override
    public ArrayList<Thread> search(int idx, int pag, String term) throws SQLException {
        ArrayList<Thread> result = new ArrayList<Thread>();
        PreparedStatement stmt;
        idx -= 1;
        System.out.println(term);
        String queryNoTerm = "select id_thread, date_thread, name, content, fk_user, fk_channel from waifus.threads where deleted=0 limit ?,?;";
        String queryTerm = "select id_thread, date_thread, name, content, fk_user, fk_channel from waifus.threads where name like ? and deleted=0 limit ?,?;";
        if(term.equals("")){
            stmt = this.connection.prepareStatement(queryNoTerm);
            stmt.setInt(1, idx);
            stmt.setInt(2, pag);
        }else {
            stmt = this.connection.prepareStatement(queryTerm);
            stmt.setString(1, "%"+term+"%");
            stmt.setInt(2, idx);
            stmt.setInt(3, pag);
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
