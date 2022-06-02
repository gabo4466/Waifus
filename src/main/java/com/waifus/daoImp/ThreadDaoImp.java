package com.waifus.daoImp;

import com.waifus.dao.GenericDao;
import com.waifus.model.User;

import java.util.ArrayList;

public class ThreadDaoImp implements GenericDao<Thread> {

    @Override
    public boolean update(Thread obj) {
        return false;
    }

    @Override
    public boolean delete(Thread obj) {
        return false;
    }

    @Override
    public Thread add(Thread obj) {
        return null;
    }

    @Override
    public ArrayList<Thread> getAll() {
        return null;
    }

    @Override
    public ArrayList<Thread> search(int idx, int pag, String term) {
        return null;
    }

    @Override
    public Thread get(int id) {
        return null;
    }

    @Override
    public int count(String term) {
        return 0;
    }
}
