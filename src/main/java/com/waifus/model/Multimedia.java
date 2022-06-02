package com.waifus.model;

import java.util.PriorityQueue;

public class Multimedia {
    private int idMultimedia;
    private String directory;
    private int thread;

    public Multimedia() {
    }

    public Multimedia(int idMultimedia, String directory, int thread) {
        this.idMultimedia = idMultimedia;
        this.directory = directory;
        this.thread = thread;
    }

    public int getIdMultimedia() {
        return idMultimedia;
    }

    public void setIdMultimedia(int idMultimedia) {
        this.idMultimedia = idMultimedia;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public int getThread() {
        return thread;
    }

    public void setThread(int thread) {
        this.thread = thread;
    }

    /*public Multimedia add() throws SQLException, ClassNotFoundException, ChannelException, IOException, ChannelNotFoundException {
        return MultimediaDaoImp.getInstance().add();
    }
    public Multimedia get() throws SQLException, ClassNotFoundException, ChannelNotFoundException {
        return MultimediaDaoImp.getInstance().get(this.idMultimedia);
    }

    public boolean update() throws SQLException, ClassNotFoundException, UserException {
        return MultimediaDaoImp.getInstance().update(this);
    }

    public boolean delete() throws SQLException, ClassNotFoundException, UserException {
        return MultimediaDaoImp.getInstance().delete(this);
    }

    public ArrayList<Multimedia> getall() throws SQLException, ClassNotFoundException, ChannelNotFoundException {
        return MultimediaDaoImp.getInstance().getAll();
    }

    public ArrayList<Multimedia> search(int idx, int pag, String term) throws SQLException, ClassNotFoundException {
        return MultimediaDaoImp.getInstance().search(idx, pag, term);
    }

    public int count(String term) throws SQLException, ClassNotFoundException {
        return MultimediaDaoImp.getInstance().count(term);
    }*/
}
