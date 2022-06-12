package com.waifus.daoImp;

import com.waifus.dao.GenericDao;
import com.waifus.exceptions.*;
import com.waifus.model.Channel;
import com.waifus.model.Comment;
import com.waifus.services.DBConnection;
import com.waifus.services.PropertiesService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

public class CommentDaoImp implements GenericDao<Comment> {
    private Connection connection;
    public static CommentDaoImp instance = null;
    public static Properties prop;

    private CommentDaoImp() throws SQLException, ClassNotFoundException {
        this.connection = DBConnection.getConnection();
        prop = PropertiesService.getProperties("config_es");
    }

    public static CommentDaoImp getInstance() throws SQLException, ClassNotFoundException {
        if (instance == null){
            instance = new CommentDaoImp();
        }
        return instance;
    }

    @Override
    public boolean update(Comment obj) throws SQLException, Exception {
        return false;
    }

    @Override
    public boolean delete(Comment comment) throws SQLException, CommentException {
        boolean result;
        String query = "update waifus.comments set deleted=? where id_comment=?;";
        PreparedStatement stmt = this.connection.prepareStatement(query);
        stmt.setBoolean(1, comment.isDeleted());
        stmt.setInt(2, comment.getIdComment());
        int rs = stmt.executeUpdate();
        if(rs>0){
            result = true;
        }else {
            result = false;
            throw new CommentException(prop.getProperty("error.generic"));
        }
        return result;
    }

    @Override
    public Comment add(Comment comment) throws SQLException, CommentException {
        Comment result;
        String query = "insert into waifus.comments (content, date_comment, deleted, fk_user, fk_thread, fk_comment) values (?,?,0,?,?,?);";
        PreparedStatement stmt = this.connection.prepareStatement(query);
        stmt.setString(1, comment.getContent());
        stmt.setString(2, comment.getDateComment());
        stmt.setInt(3, comment.getUser());
        stmt.setInt(4, comment.getThread());
        stmt.setInt(5, comment.getComment());
        int rs = stmt.executeUpdate();
        if(rs>0){
            query = "select id_comment from waifus.comments where deleted=0 and date_comment=? and fk_thread=? and fk_user=? and fk_comment=?;";
            stmt = this.connection.prepareStatement(query);
            stmt.setString(1, comment.getDateComment());
            stmt.setInt(2, comment.getThread());
            stmt.setInt(3, comment.getUser());
            stmt.setInt(4, comment.getComment());
            ResultSet rs2 = stmt.executeQuery();
            if(!rs2.next()){
                result = this.get(rs2.getInt("id_comment"));
            }else {
                result = null;
                throw new CommentException(prop.getProperty("error.generic"));
            }
        }else {
            result = null;
            throw new CommentException(prop.getProperty("error.generic"));
        }
        return result;
    }

    @Override
    public ArrayList<Comment> getAll() throws SQLException {
        ArrayList<Comment> result = new ArrayList<Comment>();
        String query = "select * from waifus.comments;";
        PreparedStatement stmt = this.connection.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            result.add(new Comment(rs.getInt("id_comment"), rs.getString("content"),
                    rs.getString("date_comment"), rs.getBoolean("deleted"),
                    rs.getInt("fk_user"), rs.getInt("fk_thread"), rs.getInt("fk_comment")));
        }
        return result;
    }

    @Override
    public ArrayList<Comment> search(int idx, int pag, String term) throws SQLException {
        return null;
    }

    @Override
    public Comment get(int id) throws SQLException, CommentException {
        Comment result;
        String query = "select * from waifus.comments where id_comment=?;";
        PreparedStatement stmt = this.connection.prepareStatement(query);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        if(!rs.next()){
            result = new Comment(rs.getInt("id_comment"), rs.getString("content"),
                    rs.getString("date_comment"), rs.getBoolean("deleted"),
                    rs.getInt("fk_user"), rs.getInt("fk_thread"), rs.getInt("fk_comment"));
        }else {
            result = null;
            throw new CommentException(prop.getProperty("error.generic"));
        }
        return result;
    }

    @Override
    public int count(String term) throws SQLException {
        return 0;
    }

    public ArrayList<Comment> threadComments(int idx, int pag, int thread) throws SQLException {
        ArrayList<Comment> result = new ArrayList<Comment>();
        idx -= 1;
        PreparedStatement stmt;
        String query = "select * from waifus.comments where deleted=0 and fk_thread=? limit ?,?;";
        stmt = this.connection.prepareStatement(query);
        stmt.setInt(1, thread);
        stmt.setInt(2, idx);
        stmt.setInt(3, pag);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            result.add(new Comment(rs.getInt("id_comment"), rs.getString("content"),
                    rs.getString("date_comment"), rs.getBoolean("deleted"),
                    rs.getInt("fk_user"), rs.getInt("fk_thread"), rs.getInt("fk_comment")));
        }
        return result;
    }

    public int threadCommentsCount(int thread) throws SQLException {
        int result = 0;
        PreparedStatement stmt;
        String queryNoTerm = "select count(*) as Quantity from waifus.comments where deleted=0 and fk_thread=?;";
        stmt = this.connection.prepareStatement(queryNoTerm);
        stmt.setInt(1, thread);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()){
            result = rs.getInt("Quantity");
        }
        return result;
    }
}
