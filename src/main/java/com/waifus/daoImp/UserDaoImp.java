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
            User userExists = new User(rs.getInt("id_user"), rs.getString("email"), rs.getBoolean("activated"),rs.getBoolean("banned"));
            System.out.println("CUENTA ACTIVADA: "+ userExists.isActivated());
            System.out.println("CUENTA BANEADA: "+ userExists.isBanned());
            System.out.println("CUENTA EMAIL: "+ userExists.getEmail());
            System.out.println("CUENTA ID: "+ userExists.getIdUser());


            //  VALIDAR SI ESTA BANEADO O ACTIVADO
            if (!userExists.isActivated()){
                result = null;
                throw new UserException("Cuenta desactivada");
            }else if (userExists.isBanned()){
                result = null;
                throw new UserException("Cuenta baneada");
            }else{
                query = "select id_user, email, gender, adult_content, nickname, admin, name, birthday, profile_photo, country, description, karma, theme from waifus.users where email=?";
                PreparedStatement stmt2 = this.connection.prepareStatement(query);
                stmt2.setString(1, userExists.getEmail());
                ResultSet rs2 = stmt2.executeQuery();
                if (rs2.next()){
                    result = new User(rs2.getInt("id_user"), rs2.getString("gender"),
                                    rs2.getBoolean("adult_content"), rs2.getString("nickname"),
                                    rs2.getBoolean("admin"), rs2.getString("name"),
                                    rs2.getString("email"), rs2.getDate("birthday"),
                                    rs2.getString("profile_photo"), rs2.getString("country"),
                                    rs2.getString("description"), rs2.getInt("karma"),
                                    rs2.getString("theme"));
                }else{
                    result = null;
                    throw new UserException("Contraseña o correo inválido");
                }
            }
        }else{
            result = null;
            throw new UserException("Contraseña o correo inválido");
        }
        return result;
    }
}
