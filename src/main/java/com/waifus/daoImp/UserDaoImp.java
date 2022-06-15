package com.waifus.daoImp;

import com.waifus.exceptions.UserNotFoundException;
import com.waifus.model.Channel;
import com.waifus.services.DBConnection;
import com.waifus.dao.GenericDao;
import com.waifus.exceptions.UserException;
import com.waifus.model.User;
import com.waifus.services.PropertiesService;

import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

public class UserDaoImp implements GenericDao<User> {
    private Connection connection;
    public static UserDaoImp instance = null;
    public static Properties prop;

    private UserDaoImp() throws SQLException, ClassNotFoundException {
        this.connection = DBConnection.getConnection();
        prop = PropertiesService.getProperties("config_es");
    }

    public static UserDaoImp getInstance() throws SQLException, ClassNotFoundException {
        if (instance == null){
            instance = new UserDaoImp();
        }
        return instance;
    }

    /**
     * Metodo que actualiza los datos del usuario
     * @param user
     * @return result
     * @throws SQLException
     * @throws UserException
     */
    @Override
    public boolean update(User user) throws SQLException, UserException {
        boolean result;
        String query = "update waifus.users set gender = ?, adult_content = ?, nickname = ?, admin = ?, name = ?, email = ?, birthday = ?, profile_photo = ?, country = ?, description = ?, karma = ?, theme = ?, activated = ?, banned = ? where id_user = ?;";
        PreparedStatement stmt = this.connection.prepareStatement(query);
        stmt.setString(1, user.getGender());
        stmt.setBoolean(2, user.isAdultContent());
        stmt.setString(3, user.getNickname());
        stmt.setBoolean(4, user.isAdmin());
        stmt.setString(5, user.getName());
        stmt.setString(6, user.getEmail());
        stmt.setString(7, user.getBirthday());
        stmt.setString(8, user.getProfilePhoto());
        stmt.setString(9, user.getCountry());
        stmt.setString(10, user.getDescription());
        stmt.setInt(11, user.getKarma());
        stmt.setString(12, user.getTheme());
        stmt.setBoolean(13, user.isActivated());
        stmt.setBoolean(14, user.isBanned());
        stmt.setInt(15, user.getIdUser());
        int rs = stmt.executeUpdate();
        if(rs>0){
            result = true;
        }else {
            result = false;
            throw new UserException(prop.getProperty("error.generic"));
        }
        return result;
    }

    @Override
    public boolean delete(User user) throws SQLException, UserException {
        boolean result;
        String query = "update waifus.users set banned=? where id_user=?;";
        PreparedStatement stmt = this.connection.prepareStatement(query);
        stmt.setBoolean(1, user.isBanned());
        stmt.setInt(2, user.getIdUser());
        int rs = stmt.executeUpdate();
        if(rs>0){
            result = true;
        }else {
            result = false;
            throw new UserException(prop.getProperty("error.generic"));
        }
        return result;
    }

    /**
     * Método que comprueba si el nombre de usuario o email introducidos en el registro son existentes o no, en caso afirmativo lanza excepciones,
     * en caso negativo lanza una query de inserción a la base de datos
     * @param user Usuario con los datos ingresados en el formulario de registro o null
     * @return booleano en referencia a si funciono el registro
     * @throws SQLException en caso de un error de base de datos
     * @throws UserException en caso de un error con relacion al usuario
     */
    @Override
    public User add(User user) throws SQLException, UserException, UserNotFoundException {
        User result;
        String query = "select id_user from waifus.users where email=? or nickname=?";
        PreparedStatement stmt = this.connection.prepareStatement(query);
        stmt.setString(1, user.getEmail());
        stmt.setString(2, user.getNickname());
        ResultSet rs = stmt.executeQuery();
        if (!rs.next()){
            query = "insert into waifus.users (email, nickname, name, password, birthday, adult_content, admin, activated, banned, karma, profile_photo) values (?,?,?,?,?,?,0,0,0,0,'profileDefault.png');";
            PreparedStatement stmt2 = this.connection.prepareStatement(query);
            stmt2.setString(1, user.getEmail());
            stmt2.setString(2, user.getNickname());
            stmt2.setString(3, user.getName());
            stmt2.setString(4, user.getPassword());
            stmt2.setString(5, user.getBirthday());
            stmt2.setBoolean(6, user.isAdultContent());
            int rs2 = stmt2.executeUpdate();
            if (rs2>0){
                query = "select id_user from waifus.users where email=?";
                stmt = this.connection.prepareStatement(query);
                stmt.setString(1, user.getEmail());
                rs = stmt.executeQuery();
                if (rs.next()){
                    result = this.get(rs.getInt("id_user"));
                }else {
                    result = null;
                    throw new UserException(prop.getProperty("error.invalidUser"));
                }
            }else {
                result = null;
                throw new UserException(prop.getProperty("error.invalidUser"));
            }
        }else {
            result = null;
            throw new UserException(prop.getProperty("error.invalidUser"));
        }
        return result;
    }

    @Override
    public ArrayList<User> getAll() throws SQLException {
        ArrayList<User> result = new ArrayList<User>();
        String query = "select id_user, email, gender, adult_content, nickname, admin, name, birthday, profile_photo, country, description, karma, theme from waifus.users;";
        PreparedStatement stmt = this.connection.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            result.add(new User(rs.getInt("id_user"), rs.getString("gender"),
                    rs.getBoolean("adult_content"), rs.getString("nickname"),
                    rs.getBoolean("admin"), rs.getString("name"),
                    rs.getString("email"), rs.getString("birthday"),
                    rs.getString("profile_photo"), rs.getString("country"),
                    rs.getString("description"), rs.getInt("karma"),
                    rs.getString("theme")));
        }
        return result;
    }

    @Override
    public ArrayList<User> search(int idx, int pag, String term) throws SQLException {
        ArrayList<User> result = new ArrayList<User>();
        PreparedStatement stmt;
        String queryNoTerm = "select id_user, gender, nickname, name, profile_photo, country, description, karma from waifus.users;";
        String queryTerm = "select id_user, gender, nickname, name, profile_photo, country, description, karma from waifus.users where nickname like '%'+?+'%' and banned=0 and activated=1 limit ?,?;";
        if(term.equals("")){
            stmt = this.connection.prepareStatement(queryNoTerm);
            stmt.setInt(1, idx);
            stmt.setInt(2, idx+pag);
        }else {
            stmt = this.connection.prepareStatement(queryTerm);
            stmt.setString(1, term);
            stmt.setInt(2, idx);
            stmt.setInt(3, idx+pag);
        }
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            result.add(new User(rs.getInt("id_user"), rs.getString("gender"),
                    rs.getString("nickname"), rs.getString("name"),
                    rs.getString("profile_photo"), rs.getString("country"),
                    rs.getString("description"), rs.getInt("karma")));
        }
        return result;
    }

    /**
     * Metodo que pasa un id a la base de datos y retorna los datos solicitados de la base de datos del usuario al que pertenece el id
     * @param id
     * @return un objeto de tipo User que contiene los datos solicitados
     * @throws SQLException en caso de un error de base de datos
     * @throws UserNotFoundException en caso de un error al intentar encontrar al usuario solicitado
     */
    @Override
    public User get(int id) throws SQLException, UserNotFoundException {
        User result=null;
        String query = "select id_user, email, gender, adult_content, nickname, admin, name, birthday, profile_photo, country, description, karma, theme from waifus.users where id_user=?";
        PreparedStatement stmt2 = this.connection.prepareStatement(query);
        stmt2.setInt(1, id);
        ResultSet rs2 = stmt2.executeQuery();
        if (rs2.next()){
            result = new User(rs2.getInt("id_user"), rs2.getString("gender"),
                    rs2.getBoolean("adult_content"), rs2.getString("nickname"),
                    rs2.getBoolean("admin"), rs2.getString("name"),
                    rs2.getString("email"), rs2.getString("birthday"),
                    rs2.getString("profile_photo"), rs2.getString("country"),
                    rs2.getString("description"), rs2.getInt("karma"),
                    rs2.getString("theme"));
        }else{
            result = null;
            throw new UserNotFoundException(prop.getProperty("error.invalidUser"));
        }
        return result;
    }

    /**
     * Método que comprueba que el usuario exista en la base de datos y que los datos ingresados sean correctos, en caso de que todo sea correcto retorna el
     * usuario con todos los valores de la base de datos, por el contrario retorna null y lanza las excepciones correspondientes segun el error.
     * @param userNotLogged Usuario con los datos ingresados en el formulario de login o null
     * @return Usuario con los datos de la base de datos
     * @throws SQLException en caso de un error de base de datos
     * @throws UserException en caso de un error con relacion al usuario
     */
    public User logIn (User userNotLogged) throws SQLException, UserException, UserNotFoundException, ClassNotFoundException {
        User result;
        String query = "select id_user, email, banned, activated from waifus.users where email=? and password=?";
        PreparedStatement stmt = this.connection.prepareStatement(query);
        stmt.setString(1, userNotLogged.getEmail());
        stmt.setString(2, userNotLogged.getPassword());
        ResultSet rs = stmt.executeQuery();
        if (rs.next()){
            User userExists = new User(rs.getInt("id_user"), rs.getString("email"), rs.getBoolean("activated"),rs.getBoolean("banned"));

            if (userExists.isBanned()){
                result = null;
                throw new UserNotFoundException(prop.getProperty("error.bannedAccount"));
            }else if (!userExists.isActivated()){
                result = null;
                throw new UserException(prop.getProperty("error.notActiveAccount"));
            }else{
                result = userExists.get();
            }
        }else{
            result = null;
            throw new UserNotFoundException(prop.getProperty("error.invalidUser"));
        }
        return result;
    }

    public boolean emailCheck(User user) throws SQLException {
        boolean result;
        String query = "select id_user from waifus.users where email=?";
        PreparedStatement stmt = this.connection.prepareStatement(query);
        stmt.setString(1, user.getEmail());
        ResultSet rs = stmt.executeQuery();
        if (rs.next()){
            return false;
        }
        return true;
    }

    public boolean nicknameCheck(User user) throws SQLException {
        boolean result;
        String query = "select id_user from waifus.users where nickname=?";
        PreparedStatement stmt = this.connection.prepareStatement(query);
        stmt.setString(1, user.getNickname());
        ResultSet rs = stmt.executeQuery();
        if (rs.next()){
            return false;
        }
        return true;
    }

    public int getId(User user) throws SQLException {
        int result = 0;
        String query = "select id_user from waifus.users where email=?";
        PreparedStatement stmt = this.connection.prepareStatement(query);
        stmt.setString(1, user.getEmail());
        ResultSet rs = stmt.executeQuery();
        if (rs.next()){
            result = rs.getInt("id_user");
        }
        return result;
    }

    @Override
    public int count(String term) throws SQLException {
        int result = 0;
        PreparedStatement stmt;
        String queryNoTerm = "select count(*) as Quantity from waifus.users;";
        String queryTerm = "select count(*) as Quantity from waifus.users where nickname like '%'+?+'%' and banned=0 and activated=1;";
        if(term.equals("")){
            stmt = this.connection.prepareStatement(queryNoTerm);
        }else {
            stmt = this.connection.prepareStatement(queryTerm);
            stmt.setString(1, term);
        }
        ResultSet rs = stmt.executeQuery();
        if(rs.next()){
            result = rs.getInt("Quantity");
        }
        return result;
    }

    public ArrayList<User> mostKarmaUsers() throws SQLException {
        ArrayList<User> result = new ArrayList<User>();
        String query = "select id_user, email, gender, adult_content, nickname, admin, name, birthday, profile_photo, country, description, karma, theme from waifus.users order by karma desc limit 5;";
        PreparedStatement stmt = this.connection.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()){
            result.add(new User(rs.getInt("id_user"), rs.getString("gender"),
                    rs.getBoolean("adult_content"), rs.getString("nickname"),
                    rs.getBoolean("admin"), rs.getString("name"),
                    rs.getString("email"), rs.getString("birthday"),
                    rs.getString("profile_photo"), rs.getString("country"),
                    rs.getString("description"), rs.getInt("karma"),
                    rs.getString("theme")));
        }
        return result;
    }

    public boolean likeComment(int id) throws SQLException, UserException {
        boolean result;
        String query = "update waifus.users set karma = (karma + 1) where id_user = ?;";
        PreparedStatement stmt = this.connection.prepareStatement(query);
        stmt.setInt(1, id);
        int rs = stmt.executeUpdate();
        if (rs>0){
            result = true;
        }else {
            result = false;
            throw new UserException(prop.getProperty("error.generic"));
        }
        return result;
    }

    public boolean dislikeComment(int id) throws SQLException, UserException {
        boolean result;
        String query = "update waifus.users set karma = karma - 2 where id_user = ?;";
        PreparedStatement stmt = this.connection.prepareStatement(query);
        stmt.setInt(1, id);
        int rs = stmt.executeUpdate();
        if (rs>0){
            result = true;
        }else {
            result = false;
            throw new UserException(prop.getProperty("error.generic"));
        }
        return result;
    }

    public boolean likeThread(int id) throws SQLException, UserException {
        boolean result;
        String query = "update waifus.users set karma = karma + 1 where id_user = ?;";
        PreparedStatement stmt = this.connection.prepareStatement(query);
        stmt.setInt(1, id);
        int rs = stmt.executeUpdate();
        if (rs>0){
            result = true;
        }else {
            result = false;
            throw new UserException(prop.getProperty("error.generic"));
        }
        return result;
    }

    public boolean dislikeThread(int id) throws SQLException, UserException {
        boolean result;
        String query = "update waifus.users set karma = karma - 2 where id_user = ?;";
        PreparedStatement stmt = this.connection.prepareStatement(query);
        stmt.setInt(1, id);
        int rs = stmt.executeUpdate();
        if (rs>0){
            result = true;
        }else {
            result = false;
            throw new UserException(prop.getProperty("error.generic"));
        }
        return result;
    }
}
