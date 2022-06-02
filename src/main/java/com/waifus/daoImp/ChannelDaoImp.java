package com.waifus.daoImp;

import com.waifus.dao.GenericDao;
import com.waifus.exceptions.ChannelException;
import com.waifus.exceptions.ChannelNotFoundException;
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
    public boolean update(Channel obj) throws SQLException, Exception {
        return false;
    }

    @Override
    public boolean delete(Channel obj) {
        return false;
    }

    @Override
    public Channel add(Channel channel) throws SQLException, ChannelException, ChannelNotFoundException {
        Channel result;
        String query = "select id_channel from waifus.channels where name=?";
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
                query = "select id_channel from waifus.channels where name=?;";
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
            throw new ChannelException(prop.getProperty("error.generic"));
        }
        return result;
    }

    @Override
    public ArrayList<Channel> getAll(int idx, int pag) {
        return null;
    }

    @Override
    public ArrayList<Channel> search(int idx, int pag, String term) {
        return null;
    }

    @Override
    public Channel get(int id) throws SQLException, ChannelNotFoundException  {
        Channel result=null;
        String query = "select id_channel, date_channel, photo, banner, name, description, fk_user, deleted from waifus.channels where id_channel=?";
        PreparedStatement stmt2 = this.connection.prepareStatement(query);
        stmt2.setInt(1, id);
        ResultSet rs2 = stmt2.executeQuery();
        if (rs2.next()){
            result = new Channel(rs2.getInt("id_channel"), rs2.getString("date_channel"),
                    rs2.getString("photo"),rs2.getString("banner"),
                    rs2.getString("name"),rs2.getString("description"),
                    rs2.getBoolean("deleted"), rs2.getInt("fk_user"));
        }else{
            result = null;
            throw new ChannelNotFoundException(prop.getProperty("error.generic"));
        }
        return result;
    }
}
