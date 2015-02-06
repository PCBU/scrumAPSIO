package DAO;

import model.User;
import service.getDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dikyx on 06/02/2015.
 */
public class UserDAO {

    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<User>();
        PreparedStatement ps;
        ResultSet rs;
        Connection db = getDB.getDB();

        ps = db.prepareCall("SELECT * FROM users");
        ps.execute();

        rs = ps.getResultSet();
        while (rs.next()) {
            users.add(new User(rs.getString("login"), rs.getString("password"), rs.getString("role")));
        }

        return users;
    }

    public void addUser(User user) throws SQLException {
        PreparedStatement ps;
        Connection db = getDB.getDB();

        ps = db.prepareStatement("INSERT INTO users VALUES (?, ?, ?, ?)");
        ps.setString(1, user.getLogin());
        ps.setString(2, user.getPassword());
        ps.setString(3, user.getRole());

        ps.executeUpdate();

        System.out.println("L'utilisateur" + user.getLogin() + "a été ajouté!");
    }
}
