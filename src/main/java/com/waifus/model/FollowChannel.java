package com.waifus.model;

import com.waifus.daoImp.FollowChannelDaoImp;
import com.waifus.exceptions.*;

import java.sql.SQLException;
import java.util.ArrayList;

public class FollowChannel {
    private int idChannel;
    private int idUser;
    private String dateFollow;

    public FollowChannel(int idChannel, int idUser, String dateFollow) {
        this.idChannel = idChannel;
        this.idUser = idUser;
        this.dateFollow = dateFollow;
    }

    public FollowChannel() {
    }

    public FollowChannel(int idUser) {
        this.idUser = idUser;
    }

    public FollowChannel(int idChannel, int idUser) {
        this.idChannel = idChannel;
        this.idUser = idUser;
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

    public void delete() throws SQLException, ClassNotFoundException, FollowChannelException {
        FollowChannelDaoImp.getInstance().delete(this);
    }

    public void follows() throws SQLException, ClassNotFoundException, FollowChannelException {
        FollowChannelDaoImp.getInstance().follows(this);
    }

    public ArrayList<Channel> getFChannels() throws SQLException, ClassNotFoundException, ChannelNotFoundException {
        ArrayList<FollowChannel> fChannels = FollowChannelDaoImp.getInstance().getFChannels(this);
        ArrayList<Channel> channels = new ArrayList<Channel>();
        for (FollowChannel fChannel:fChannels) {
            Channel channelAux = new Channel(fChannel.getIdChannel());
            channels.add(channelAux.get());
        }
        return channels;
    }

}
