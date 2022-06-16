package com.waifus.model;

import com.waifus.daoImp.ThreadDaoImp;
import com.waifus.exceptions.ThreadException;
import com.waifus.exceptions.ThreadNotFoundException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class Thread {
    private int idThread;
    private String dateThread;
    private String name;
    private String content;
    private boolean deleted;
    private int user;
    private int channel;

    public Thread() {
    }

    public Thread(int idThread, String dateThread, String name, String content, boolean deleted, int user, int channel) {
        this.idThread = idThread;
        this.dateThread = dateThread;
        this.name = name;
        this.content = content;
        this.deleted = deleted;
        this.user = user;
        this.channel = channel;
    }

    public Thread(int idThread, String dateThread, String name, String content, int user, int channel) {
        this.idThread = idThread;
        this.dateThread = dateThread;
        this.name = name;
        this.content = content;
        this.user = user;
        this.channel = channel;
    }

    public Thread(int idThread) {
        this.idThread = idThread;
    }

    public int getIdThread() {
        return idThread;
    }

    public void setIdThread(int idThread) {
        this.idThread = idThread;
    }

    public String getDateThread() {
        return dateThread;
    }

    public void setDateThread(String dateThread) {
        this.dateThread = dateThread;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public Thread add() throws SQLException, ClassNotFoundException, IOException, ThreadNotFoundException, ThreadException {
        return ThreadDaoImp.getInstance().add(this);
    }
    public Thread get() throws SQLException, ClassNotFoundException, ThreadNotFoundException {
        return ThreadDaoImp.getInstance().get(this.idThread);
    }

    public boolean update() throws SQLException, ClassNotFoundException, ThreadException {
        return ThreadDaoImp.getInstance().update(this);
    }

    public boolean delete() throws SQLException, ClassNotFoundException, ThreadException {
        return ThreadDaoImp.getInstance().delete(this);
    }

    public ArrayList<Thread> getall() throws SQLException, ClassNotFoundException {
        return ThreadDaoImp.getInstance().getAll();
    }

    public ArrayList<Thread> search(int idx, int pag, String term) throws SQLException, ClassNotFoundException {
        return ThreadDaoImp.getInstance().search(idx, pag, term, this);
    }

    public int count(String term) throws SQLException, ClassNotFoundException {
        return ThreadDaoImp.getInstance().count(term, this);
    }
}
