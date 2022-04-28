package com.waifus.daoImp;

import com.waifus.DBConnection;
import com.waifus.dao.GenericDao;
import com.waifus.exceptions.UserException;
import com.waifus.model.User;

import java.sql.*;
import java.util.ArrayList;

public class UserDaoImp implements GenericDao<User> {
    private Connection connection;

    public UserDaoImp() throws SQLException, ClassNotFoundException {
        this.connection = DBConnection.getConnection();
    }

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

    public User logIn (User userNotLogged) throws SQLException, UserException {
        User result;
        String query = "select id_user, email, banned, activated from waifus.users where email=? and password=?";
        PreparedStatement stmt = this.connection.prepareStatement(query);
        stmt.setString(1, userNotLogged.getEmail());
        stmt.setString(2, userNotLogged.getPassword());
        ResultSet rs = stmt.executeQuery();
        if (rs.next()){
            User userExists = new User(rs.getInt("id_user"), rs.getString("email"), rs.getBoolean("banned"), rs.getBoolean("activated"));
            result = userExists;
            //  VALIDAR SI ESTA BANEADO O ACTIVADO
        }else{
            result = null;
            throw new UserException("Contraseña o correo inválido");
        }
        return result;
    }
}
