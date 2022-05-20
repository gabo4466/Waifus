package com.waifus.model;

import com.waifus.daoImp.UserDaoImp;
import com.waifus.exceptions.UserException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

public class User {
    private int idUser;
    private String gender;
    private boolean adultContent;
    private String nickname;
    private boolean admin;
    private String name;
    private String email;
    private String password;
    private String birthday;
    private String profilePhoto;
    private String country;
    private String description;
    private int karma;
    private String theme;
    private boolean activated;
    private boolean banned;
    private String repPass;

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

    public User(int idUser, String gender, boolean adultContent, String nickname, boolean admin, String name, String email, String birthday, String profilePhoto, String country, String description, int karma, String theme) {
        this.idUser = idUser;
        this.gender = gender;
        this.adultContent = adultContent;
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

    public User(int idUser, String gender, boolean adultContent, String nickname, boolean admin, String name, String email, String password, String birthday, String profilePhoto, String country, String description, int karma, String theme, boolean activated, boolean banned, String repPass) {
        this.idUser = idUser;
        this.gender = gender;
        this.adultContent = adultContent;
        this.nickname = nickname;
        this.admin = admin;
        this.name = name;
        this.email = email;
        this.password = password;
        this.birthday = birthday;
        this.profilePhoto = profilePhoto;
        this.country = country;
        this.description = description;
        this.karma = karma;
        this.theme = theme;
        this.activated = activated;
        this.banned = banned;
        this.repPass = repPass;
    }

    public User(String nickname, String name, String email, String password) {
        this.nickname = nickname;
        this.name = name;
        this.email = email;
        this.password = password;
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

    public boolean isAdultContent() {
        return adultContent;
    }

    public void setAdultContent(boolean adultContent) {
        this.adultContent = adultContent;
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

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
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

    public String getRepPass() {
        return repPass;
    }

    public void setRepPass(String repPass) {
        this.repPass = repPass;
    }

    /**
     * @see com.waifus.daoImp.UserDaoImp#logIn(User) 
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws UserException
     * @throws IOException
     */
    public User logIn() throws SQLException, ClassNotFoundException, UserException, IOException {
        return UserDaoImp.getInstance().logIn(this);
    }

    /**
     * @see com.waifus.daoImp.UserDaoImp#add(User) 
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws UserException
     * @throws IOException
     */
    public Boolean register() throws SQLException, ClassNotFoundException, UserException, IOException {
        return UserDaoImp.getInstance().add(this);
    }

}
