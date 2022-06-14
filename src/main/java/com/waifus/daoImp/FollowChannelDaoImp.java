package com.waifus.daoImp;

import com.waifus.dao.GenericDao;
import com.waifus.exceptions.*;
import com.waifus.model.FollowChannel;
import com.waifus.services.DBConnection;
import com.waifus.services.PropertiesService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

public class FollowChannelDaoImp implements GenericDao<FollowChannel> {

    private Connection connection;
    public static FollowChannelDaoImp instance = null;
    public static Properties prop;

    private FollowChannelDaoImp() throws SQLException, ClassNotFoundException {
        this.connection = DBConnection.getConnection();
        prop = PropertiesService.getProperties("config_es");
    }

    public static FollowChannelDaoImp getInstance() throws SQLException, ClassNotFoundException {
        if (instance == null){
            instance = new FollowChannelDaoImp();
        }
        return instance;
    }

    @Override
    public boolean update(FollowChannel obj) throws SQLException, Exception {
        return false;
    }

    @Override
    public boolean delete(FollowChannel obj) throws SQLException, FollowChannelException {
        boolean result = false;
        String query = "delete from waifus.users_follows_channels where id_user = ? and id_channel = ?";
        PreparedStatement stmt = this.connection.prepareStatement(query);
        stmt.setInt(1, obj.getIdUser());
        stmt.setInt(2, obj.getIdChannel());
        int rs = stmt.executeUpdate();
        if (rs > 0){
            result = true;
        }
        return result;
    }

    @Override
    public FollowChannel add(FollowChannel obj) throws SQLException, FollowChannelException {
        String query = "select date_follows from waifus.users_follows_channels where id_channel = ? and id_user = ?";
        PreparedStatement stmt = this.connection.prepareStatement(query);
        stmt.setInt(1, obj.getIdChannel());
        stmt.setInt(2, obj.getIdUser());
        ResultSet rs = stmt.executeQuery();
        if (rs.next()){
            throw new FollowChannelException(prop.getProperty("error.follow"));
        }else{
            FollowChannel result = null;
            query = "insert into waifus.users_follows_channels (id_user, id_channel, date_follows) values (?,?,?);";
            stmt = this.connection.prepareStatement(query);
            stmt.setInt(1, obj.getIdUser());
            stmt.setInt(2, obj.getIdChannel());
            stmt.setString(3, obj.getDateFollow());
            int rss = stmt.executeUpdate();
            if (rss == 0){
                throw new SQLException(prop.getProperty("error.db"));
            }

        }

        return null;
    }

    @Override
    public ArrayList<FollowChannel> getAll() throws SQLException, ChannelNotFoundException {
        return null;
    }

    @Override
    public ArrayList<FollowChannel> search(int idx, int pag, String term) throws SQLException {
        return null;
    }

    @Override
    public FollowChannel get(int id) throws SQLException, Exception {
        return null;
    }

    @Override
    public int count(String term) throws SQLException {
        return 0;
    }

    public void follows(FollowChannel obj) throws FollowChannelException, SQLException {
        String query = "select date_follows from waifus.users_follows_channels where id_channel = ? and id_user = ?";
        PreparedStatement stmt = this.connection.prepareStatement(query);
        stmt.setInt(1, obj.getIdChannel());
        stmt.setInt(2, obj.getIdUser());
        ResultSet rs = stmt.executeQuery();
        if (!rs.next()){
            throw new FollowChannelException(prop.getProperty("error.follow"));
        }
    }

    public ArrayList<FollowChannel> getFChannels(FollowChannel obj) throws SQLException {
        ArrayList<FollowChannel> fChannels = new ArrayList<FollowChannel>();
        String query = "select id_channel from waifus.users_follows_channels where id_user = ?;";
        PreparedStatement stmt = this.connection.prepareStatement(query);
        stmt.setInt(1, obj.getIdUser());
        ResultSet rs = stmt.executeQuery();
        while (rs.next()){
            FollowChannel fChannelAux = new FollowChannel();
            fChannelAux.setIdChannel(rs.getInt("id_channel"));
            fChannels.add(fChannelAux);
        }
        return fChannels;
    }

}
