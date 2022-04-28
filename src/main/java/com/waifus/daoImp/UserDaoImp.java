package com.waifus.daoImp;

import com.waifus.dao.GenericDao;
import com.waifus.model.User;

import java.util.ArrayList;

public class UserDaoImp implements GenericDao<User> {
    @Override
    public boolean update(User obj) {
        return false;
    }

    @Override
    public boolean delete(User obj) {
        return false;
    }

    @Override
    public boolean add(User obj) {
        return false;
    }

    @Override
    public ArrayList<User> getAll(int idx, int pag) {
        return null;
    }

    @Override
    public ArrayList<User> search(int idx, int pag, String term) {
        return null;
    }

    @Override
    public User get(int id) {
        return null;
    }

    public boolean logIn(){
        return false;
    }
}
