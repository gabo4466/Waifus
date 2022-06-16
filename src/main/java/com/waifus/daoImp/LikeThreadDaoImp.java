package com.waifus.daoImp;

import com.waifus.dao.GenericDao;
import com.waifus.exceptions.ChannelNotFoundException;
import com.waifus.exceptions.FollowChannelException;
import com.waifus.exceptions.LikeThreadException;
import com.waifus.model.FollowChannel;
import com.waifus.model.LikeThread;
import com.waifus.services.DBConnection;
import com.waifus.services.PropertiesService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

public class LikeThreadDaoImp implements GenericDao<LikeThread> {

    private Connection connection;
    public static LikeThreadDaoImp instance = null;
    public static Properties prop;

    private LikeThreadDaoImp() throws SQLException, ClassNotFoundException {
        this.connection = DBConnection.getConnection();
        prop = PropertiesService.getProperties("config_es");
    }

    public static LikeThreadDaoImp getInstance() throws SQLException, ClassNotFoundException {
        if (instance == null){
            instance = new LikeThreadDaoImp();
        }
        return instance;
    }

    @Override
    public boolean update(LikeThread obj) throws SQLException, Exception {
        return false;
    }

    @Override
    public boolean delete(LikeThread obj) throws SQLException, FollowChannelException {
        boolean result = false;
        String query = "delete from waifus.users_likes_threads where id_user = ? and id_thread = ?";
        PreparedStatement stmt = this.connection.prepareStatement(query);
        stmt.setInt(1, obj.getIdUser());
        stmt.setInt(2, obj.getIdThread());
        int rs = stmt.executeUpdate();
        if (rs > 0){
            result = true;
        }
        return result;
    }

    @Override
    public LikeThread add(LikeThread obj) throws SQLException, LikeThreadException {
        String query = "select date_likes from waifus.users_likes_threads where id_thread = ? and id_user = ?";
        PreparedStatement stmt = this.connection.prepareStatement(query);
        stmt.setInt(1, obj.getIdThread());
        stmt.setInt(2, obj.getIdUser());
        ResultSet rs = stmt.executeQuery();
        if (rs.next()){
            throw new LikeThreadException(prop.getProperty("error.follow"));
        }else{
            FollowChannel result = null;
            query = "insert into waifus.users_likes_threads (id_user, id_thread, date_likes) values (?,?,?);";
            stmt = this.connection.prepareStatement(query);
            stmt.setInt(1, obj.getIdUser());
            stmt.setInt(2, obj.getIdThread());
            stmt.setString(3, obj.getDateLike());
            int rss = stmt.executeUpdate();
            if (rss == 0){
                throw new SQLException(prop.getProperty("error.db"));
            }

        }

        return null;
    }

    @Override
    public ArrayList<LikeThread> getAll() throws SQLException, ChannelNotFoundException {
        return null;
    }

    @Override
    public ArrayList<LikeThread> search(int idx, int pag, String term) throws SQLException {
        return null;
    }

    @Override
    public LikeThread get(int id) throws SQLException, Exception {
        return null;
    }

    @Override
    public int count(String term) throws SQLException {
        return 0;
    }

    public void likes(LikeThread obj) throws FollowChannelException, SQLException {
        String query = "select date_likes from waifus.users_likes_threads where id_thread = ? and id_user = ?";
        PreparedStatement stmt = this.connection.prepareStatement(query);
        stmt.setInt(1, obj.getIdThread());
        stmt.setInt(2, obj.getIdUser());
        ResultSet rs = stmt.executeQuery();
        if (!rs.next()){
            throw new FollowChannelException(prop.getProperty("error.follow"));
        }
    }


}
