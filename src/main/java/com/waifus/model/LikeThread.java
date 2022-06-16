package com.waifus.model;

import com.waifus.daoImp.FollowChannelDaoImp;
import com.waifus.daoImp.LikeThreadDaoImp;
import com.waifus.exceptions.ChannelNotFoundException;
import com.waifus.exceptions.FollowChannelException;
import com.waifus.exceptions.LikeThreadException;

import java.sql.SQLException;
import java.util.ArrayList;

public class LikeThread {
    private int idThread;
    private int idUser;
    private String dateLike;

    public LikeThread(int idThread, int idUser, String dateFollow) {
        this.idThread = idThread;
        this.idUser = idUser;
        this.dateLike = dateFollow;
    }

    public LikeThread() {
    }

    public LikeThread(int idUser) {
        this.idUser = idUser;
    }

    public LikeThread(int idThread, int idUser) {
        this.idThread = idThread;
        this.idUser = idUser;
    }

    public int getIdThread() {
        return idThread;
    }

    public void setIdThread(int idThread) {
        this.idThread = idThread;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getDateLike() {
        return dateLike;
    }

    public void setDateLike(String dateLike) {
        this.dateLike = dateLike;
    }

    public void add() throws SQLException, ClassNotFoundException, FollowChannelException, LikeThreadException {
        LikeThreadDaoImp.getInstance().add(this);
    }

    public void delete() throws SQLException, ClassNotFoundException, FollowChannelException {
        LikeThreadDaoImp.getInstance().delete(this);
    }

    public void likes() throws SQLException, ClassNotFoundException, FollowChannelException {
        LikeThreadDaoImp.getInstance().likes(this);
    }


}
