package com.waifus.daoImp;

import com.waifus.dao.GenericDao;
import com.waifus.exceptions.ChannelException;
import com.waifus.exceptions.ChannelNotFoundException;
import com.waifus.exceptions.UserException;
import com.waifus.model.Channel;
import com.waifus.model.User;
import com.waifus.services.DBConnection;
import com.waifus.services.PropertiesService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

public class ChannelDaoImp implements GenericDao<Channel> {
    private Connection connection;
    public static ChannelDaoImp instance = null;
    public static Properties prop;

    private ChannelDaoImp() throws SQLException, ClassNotFoundException {
        this.connection = DBConnection.getConnection();
        prop = PropertiesService.getProperties("config_es");
    }

    public static ChannelDaoImp getInstance() throws SQLException, ClassNotFoundException {
        if (instance == null){
            instance = new ChannelDaoImp();
        }
        return instance;
    }
    @Override
    public boolean update(Channel channel) throws SQLException, UserException {
        boolean result;
        String query = "update waifus.channels set deleted=?, description=?, photo=?, banner=?, name=? where id_channel=?;";
        PreparedStatement stmt = this.connection.prepareStatement(query);
        stmt.setBoolean(1, channel.isDeleted());
        stmt.setString(2, channel.getDescription());
        stmt.setString(3, channel.getPhoto());
        stmt.setString(4, channel.getBanner());
        stmt.setString(5, channel.getName());
        stmt.setInt(6, channel.getIdChannel());
        int rs = stmt.executeUpdate();
        if(rs>0){
            result = true;
        }else {
            result = false;
            throw new UserException(prop.getProperty("error.generic"));
        }
        return result;
    }

    @Override
    public boolean delete(Channel channel) throws SQLException, UserException {
        boolean result;
        String query = "update waifus.channels set deleted=? where id_channel=?;";
        PreparedStatement stmt = this.connection.prepareStatement(query);
        stmt.setBoolean(1, channel.isDeleted());
        stmt.setInt(2, channel.getIdChannel());
        int rs = stmt.executeUpdate();
        if(rs>0){
            result = true;
        }else {
            result = false;
            throw new UserException(prop.getProperty("error.generic"));
        }
        return result;
    }

    @Override
    public Channel add(Channel channel) throws SQLException, ChannelException, ChannelNotFoundException {
        Channel result;
        String query = "select id_channel from waifus.channels where name=? and deleted=0";
        PreparedStatement stmt = this.connection.prepareStatement(query);
        stmt.setString(1, channel.getName());
        ResultSet rs = stmt.executeQuery();
        if (!rs.next()){
            query = "insert into waifus.channels (description, photo, banner, name, date_channel, fk_user, deleted) values (?,?,?,?,?,?,0);";
            PreparedStatement stmt2 = this.connection.prepareStatement(query);
            stmt2.setString(1, channel.getDescription());
            stmt2.setString(2, channel.getPhoto());
            stmt2.setString(3, channel.getBanner());
            stmt2.setString(4, channel.getName());
            stmt2.setString(5, channel.getDateChannel());
            stmt2.setInt(6, channel.getUser());
            int rs2 = stmt2.executeUpdate();
            if (rs2>0){
                query = "select id_channel from waifus.channels where name=? and deleted=0;";
                stmt = this.connection.prepareStatement(query);
                stmt.setString(1, channel.getName());
                rs = stmt.executeQuery();
                if (rs.next()){
                    result = this.get(rs.getInt("id_channel"));
                }else {
                    result = null;
                    throw new ChannelException(prop.getProperty("error.generic"));
                }
            }else {
                result = null;
                throw new ChannelException(prop.getProperty("error.generic"));
            }
        }else {
            result = null;
            throw new ChannelException(prop.getProperty("error.existingChannel"));
        }
        return result;
    }

    @Override
    public ArrayList<Channel> getAll() throws SQLException, ChannelNotFoundException {
        ArrayList<Channel> result = new ArrayList<Channel>();
        String query = "select id_channel, date_channel, photo, banner, name, description, deleted, fk_user from waifus.channels;";
        PreparedStatement stmt = this.connection.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            result.add(new Channel(rs.getInt("id_channel"), rs.getString("date_channel"),
                    rs.getString("photo"), rs.getString("banner"),
                    rs.getString("name"), rs.getString("description"),
                    rs.getBoolean("deleted") ,rs.getInt("fk_user")));
        }
        return result;
    }

    @Override
    public ArrayList<Channel> search(int idx, int pag, String term) throws SQLException {
        ArrayList<Channel> result = new ArrayList<Channel>();
        PreparedStatement stmt;
        String queryNoTerm = "select id_channel, date_channel, photo, banner, name, description, fk_user from waifus.channels where deleted=0 limit ?,?;";
        String queryTerm = "select id_channel, date_channel, photo, banner, name, description, fk_user from waifus.channels where name like '%'+?+'%' and deleted=0 limit ?,?;";
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
            result.add(new Channel(rs.getInt("id_channel"), rs.getString("date_channel"),
                    rs.getString("photo"), rs.getString("banner"),
                    rs.getString("name"), rs.getString("description"),
                    rs.getInt("fk_user")));
        }
        return result;
    }

    @Override
    public Channel get(int id) throws SQLException, ChannelNotFoundException  {
        Channel result=null;
        String query = "select id_channel, date_channel, photo, banner, name, description, fk_user, deleted from waifus.channels where id_channel=?";
        PreparedStatement stmt = this.connection.prepareStatement(query);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()){
            result = new Channel(rs.getInt("id_channel"), rs.getString("date_channel"),
                    rs.getString("photo"),rs.getString("banner"),
                    rs.getString("name"),rs.getString("description"),
                    rs.getBoolean("deleted"), rs.getInt("fk_user"));
        }else{
            result = null;
            throw new ChannelNotFoundException(prop.getProperty("error.generic"));
        }
        return result;
    }

    @Override
    public int count(String term) throws SQLException {
        int result = 0;
        PreparedStatement stmt;
        String queryNoTerm = "select count(*) as Quantity from waifus.channels where deleted=0;";
        String queryTerm = "select count(*) as Quantity from waifus.channels where name like '%'+?+'%' and deleted=0;";
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

    public boolean nameCheck(Channel channel) throws SQLException {
        boolean result;
        String query = "select id_channel from waifus.channels where name=?";
        PreparedStatement stmt = this.connection.prepareStatement(query);
        stmt.setString(1, channel.getName());
        ResultSet rs = stmt.executeQuery();
        if (rs.next()){
            return false;
        }
        return true;
    }
}
