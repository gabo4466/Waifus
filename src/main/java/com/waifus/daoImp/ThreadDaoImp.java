package com.waifus.daoImp;

import com.waifus.dao.GenericDao;

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
    public boolean add(Thread obj) {
        return false;
    }

    @Override
    public ArrayList<Thread> getAll(int idx, int pag) {
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
}