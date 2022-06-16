package com.waifus.model;

import com.waifus.daoImp.CommentDaoImp;
import com.waifus.exceptions.CommentException;

import java.sql.SQLException;
import java.util.ArrayList;

public class Comment {
    private int idComment;
    private String content;
    private String dateComment;
    private boolean deleted;
    private int user;
    private int thread;
    private int comment;

    public Comment() {
    }

    public Comment(int idComment, String content, String dateComment, boolean deleted, int user, int thread, int comment) {
        this.idComment = idComment;
        this.content = content;
        this.dateComment = dateComment;
        this.deleted = deleted;
        this.user = user;
        this.thread = thread;
        this.comment = comment;
    }

    public Comment(int idComment) {
        this.idComment = idComment;
    }

    public int getIdComment() {
        return idComment;
    }

    public void setIdComment(int idComment) {
        this.idComment = idComment;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDateComment() {
        return dateComment;
    }

    public void setDateComment(String dateComment) {
        this.dateComment = dateComment;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public int getThread() {
        return thread;
    }

    public void setThread(int thread) {
        this.thread = thread;
    }

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }

    public Comment get() throws SQLException, ClassNotFoundException, CommentException {
        return CommentDaoImp.getInstance().get(this.idComment);
    }

    public Comment add() throws SQLException, ClassNotFoundException, CommentException {
        return CommentDaoImp.getInstance().add(this);
    }

    public boolean delete() throws SQLException, ClassNotFoundException, CommentException{
        return CommentDaoImp.getInstance().delete(this);
    }

    public ArrayList<Comment> getall() throws SQLException, ClassNotFoundException {
        return CommentDaoImp.getInstance().getAll();
    }

    public int count(int thread) throws SQLException, ClassNotFoundException {
        return CommentDaoImp.getInstance().threadCommentsCount(thread);
    }

    public ArrayList<Comment> search(int idx, int pag, int thread) throws SQLException, ClassNotFoundException {
        return CommentDaoImp.getInstance().threadComments(idx, pag, thread);
    }

    public ArrayList<Comment> searchEmbedded(int idx, int pag, int thread, int comment) throws SQLException, ClassNotFoundException {
        return CommentDaoImp.getInstance().threadEmbeddedComment(idx, pag, thread, comment);
    }
}
