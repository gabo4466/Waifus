package com.waifus.model;

import com.waifus.daoImp.FollowChannelDaoImp;
import com.waifus.exceptions.FollowChannelException;

import java.sql.SQLException;

public class FollowChannel {
    private int idChannel;
    private int idUser;
    private String dateFollow;

    public FollowChannel(int idChannel, int idUser, String dateFollow) {
        this.idChannel = idChannel;
        this.idUser = idUser;
        this.dateFollow = dateFollow;
    }

    public int getIdChannel() {
        return idChannel;
    }

    public void setIdChannel(int idChannel) {
        this.idChannel = idChannel;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getDateFollow() {
        return dateFollow;
    }

    public void setDateFollow(String dateFollow) {
        this.dateFollow = dateFollow;
    }

    public void add() throws SQLException, ClassNotFoundException, FollowChannelException {
        FollowChannelDaoImp.getInstance().add(this);
    }
}
