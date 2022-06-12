package com.waifus.daoImp;

import com.waifus.dao.GenericDao;
import com.waifus.exceptions.*;
import com.waifus.model.FollowChannel;
import com.waifus.services.DBConnection;
import com.waifus.services.PropertiesService;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
    public boolean delete(FollowChannel obj) throws SQLException, UserException, ChannelException, ThreadException, MultimediaException {
        return false;
    }

    @Override
    public FollowChannel add(FollowChannel obj) throws SQLException, FollowChannelException {
        FollowChannel result = null;
        String query = "insert into waifus.users_follows_channels (id_user, id_channel, date_follows) values (?,?,?);";
        PreparedStatement stmt = this.connection.prepareStatement(query);
        stmt.setInt(1, obj.getIdUser());
        stmt.setInt(2, obj.getIdChannel());
        stmt.setString(3, obj.getDateFollow());
        int rs = stmt.executeUpdate();
        if (rs == 0){
            throw new FollowChannelException(prop.getProperty("error.follow"));
        }
        return result;
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
}
