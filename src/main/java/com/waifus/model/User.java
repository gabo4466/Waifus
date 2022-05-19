package com.waifus.model;

import com.waifus.daoImp.UserDaoImp;
import com.waifus.exceptions.UserException;

import java.sql.SQLException;
import java.util.Date;

public class User {
    private int idUser;
    private String gender;
    private boolean adult_content;
    private String nickname;
    private boolean admin;
    private String name;
    private String email;
    private String password;
    private Date birthday;
    private String profilePhoto;
    private String country;
    private String description;
    private int karma;
    private String theme;
    private boolean activated;
    private boolean banned;



    /**
     * Constructor que se utiliza para el login
     * @param email
     * @param password
     */
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(int idUser, String email, boolean activated, boolean banned) {
        this.idUser = idUser;
        this.email = email;
        this.activated = activated;
        this.banned = banned;
    }

    public User(int idUser, String gender, boolean adult_content, String nickname, boolean admin, String name, String email, Date birthday, String profilePhoto, String country, String description, int karma, String theme) {
        this.idUser = idUser;
        this.gender = gender;
        this.adult_content = adult_content;
        this.nickname = nickname;
        this.admin = admin;
        this.name = name;
        this.email = email;
        this.birthday = birthday;
        this.profilePhoto = profilePhoto;
        this.country = country;
        this.description = description;
        this.karma = karma;
        this.theme = theme;
    }

    public User(int idUser) {
        this.idUser = idUser;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isAdult_content() {
        return adult_content;
    }

    public void setAdult_content(boolean adult_content) {
        this.adult_content = adult_content;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getKarma() {
        return karma;
    }

    public void setKarma(int karma) {
        this.karma = karma;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public boolean isBanned() {
        return banned;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }

    public User logIn() throws SQLException, ClassNotFoundException, UserException {
        return UserDaoImp.getInstance().logIn(this);
    }

    public User get() throws SQLException, ClassNotFoundException {
        return UserDaoImp.getInstance().get(this.idUser);
    }
}
