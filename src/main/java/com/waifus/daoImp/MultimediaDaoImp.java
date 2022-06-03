package com.waifus.daoImp;

import com.waifus.dao.GenericDao;
import com.waifus.exceptions.MultimediaException;
import com.waifus.exceptions.MultimediaNotFoundException;
import com.waifus.model.Multimedia;
import com.waifus.services.DBConnection;
import com.waifus.services.PropertiesService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
    public boolean update(Multimedia multimedia) throws SQLException, MultimediaException {
        boolean result;
        String query = "update waifus.multimedia set directory=? where id_multimedia=?;";
        PreparedStatement stmt = this.connection.prepareStatement(query);
        stmt.setString(1, multimedia.getDirectory());
        stmt.setInt(2, multimedia.getIdMultimedia());
        int rs = stmt.executeUpdate();
        if(rs>0){
            result = true;
        }else {
            result = false;
            throw new MultimediaException(prop.getProperty("error.generic"));
        }
        return result;
    }

    @Override
    public boolean delete(Multimedia multimedia) throws SQLException, MultimediaException {
        boolean result;
        String query = "delete from waifus.multimedia where id_multimedia=?;";
        PreparedStatement stmt = this.connection.prepareStatement(query);
        stmt.setInt(1, multimedia.getIdMultimedia());
        int rs = stmt.executeUpdate();
        if(rs>0){
            result = true;
        }else {
            result = false;
            throw new MultimediaException(prop.getProperty("error.generic"));
        }
        return result;
    }

    @Override
    public Multimedia add(Multimedia multimedia) throws MultimediaException, SQLException, MultimediaNotFoundException {
        Multimedia result;
        String query = "select id_multimedia from waifus.multimedia where directory=?";
        PreparedStatement stmt = this.connection.prepareStatement(query);
        stmt.setString(1, multimedia.getDirectory());
        ResultSet rs = stmt.executeQuery();
        if (!rs.next()){
            query = "insert into waifus.multimedia (directory, fk_thread) values (?,?);";
            PreparedStatement stmt2 = this.connection.prepareStatement(query);
            stmt2.setString(1, multimedia.getDirectory());
            stmt2.setInt(2, multimedia.getThread());
            int rs2 = stmt2.executeUpdate();
            if (rs2>0){
                query = "select id_multimedia from waifus.multimedia where directory=?;";
                stmt = this.connection.prepareStatement(query);
                stmt.setString(1, multimedia.getDirectory());
                rs = stmt.executeQuery();
                if (rs.next()){
                    result = this.get(rs.getInt("id_multimedia"));
                }else {
                    result = null;
                    throw new MultimediaException(prop.getProperty("error.generic"));
                }
            }else {
                result = null;
                throw new MultimediaException(prop.getProperty("error.generic"));
            }
        }else {
            result = null;
            throw new MultimediaException(prop.getProperty("error.existingMultimedia"));
        }
        return result;
    }

    @Override
    public ArrayList<Multimedia> getAll() throws SQLException {
        ArrayList<Multimedia> result = new ArrayList<Multimedia>();
        String query = "select id_multimedia, directory, fk_thread from waifus.multimedia;";
        PreparedStatement stmt = this.connection.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            result.add(new Multimedia(rs.getInt("id_multimedia"), rs.getString("directory"),
                    rs.getInt("fk_thread")));
        }
        return result;
    }

    @Override
    public ArrayList<Multimedia> search(int idx, int pag, String term) throws SQLException {
        return null;
    }

    @Override
    public Multimedia get(int id) throws SQLException, MultimediaNotFoundException {
        Multimedia result=null;
        String query = "select id_multimedia, directory, fk_thread from waifus.multimedia where id_multimedia=?";
        PreparedStatement stmt = this.connection.prepareStatement(query);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()){
            result = new Multimedia(rs.getInt("id_multimedia"), rs.getString("directory"),
                    rs.getInt("fk_thread"));
        }else{
            result = null;
            throw new MultimediaNotFoundException(prop.getProperty("error.generic"));
        }
        return result;
    }

    @Override
    public int count(String term) throws SQLException {
        return 0;
    }
}
