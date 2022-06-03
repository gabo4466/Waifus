package com.waifus.model;

import com.waifus.daoImp.MultimediaDaoImp;
import com.waifus.exceptions.MultimediaException;
import com.waifus.exceptions.MultimediaNotFoundException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

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

    public Multimedia add() throws SQLException, ClassNotFoundException, MultimediaException, IOException, MultimediaNotFoundException {
        return MultimediaDaoImp.getInstance().add(this);
    }
    public Multimedia get() throws SQLException, ClassNotFoundException, MultimediaNotFoundException {
        return MultimediaDaoImp.getInstance().get(this.idMultimedia);
    }

    public boolean update() throws SQLException, ClassNotFoundException, MultimediaException {
        return MultimediaDaoImp.getInstance().update(this);
    }

    public boolean delete() throws SQLException, ClassNotFoundException, MultimediaException {
        return MultimediaDaoImp.getInstance().delete(this);
    }

    public ArrayList<Multimedia> getall() throws SQLException, ClassNotFoundException {
        return MultimediaDaoImp.getInstance().getAll();
    }

    public ArrayList<Multimedia> search(int id) throws SQLException, ClassNotFoundException {
        return MultimediaDaoImp.getInstance().searchMultimedia(id);
    }
}
