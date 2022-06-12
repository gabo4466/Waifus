package com.waifus.daoImp;

import com.waifus.dao.GenericDao;
import com.waifus.exceptions.*;
import com.waifus.model.User;
import com.waifus.services.DBConnection;
import com.waifus.services.PropertiesService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

public class UserFollowsChannelDaoImp {

    private Connection connection;
    public static UserFollowsChannelDaoImp instance = null;
    public static Properties prop;

    private UserFollowsChannelDaoImp() throws SQLException, ClassNotFoundException {
        this.connection = DBConnection.getConnection();
        prop = PropertiesService.getProperties("config_es");
    }

    public static UserFollowsChannelDaoImp getInstance() throws SQLException, ClassNotFoundException {
        if (instance == null){
            instance = new UserFollowsChannelDaoImp();
        }
        return instance;
    }


}
