package DAO;

import model.User;
import service.getDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {



    public static List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<User>();
        PreparedStatement ps = null;
        ResultSet rs;
        Connection db = null;

        try {
            db = getDB.getDB();

            ps = db.prepareCall("SELECT * FROM users");
            ps.execute();

            rs = ps.getResultSet();
            while (rs.next()) {
                users.add(new User(rs.getString("login"), rs.getString("password"), rs.getString("role")));
            }
        } finally {

            if (ps != null) {
                ps.close();
            }

            if (db != null) {
                db.close();
            }
            return users;

        }
    }

    public static void addUser(User user) throws SQLException {
        PreparedStatement ps = null;
        Connection db = null;

        try {
            db = getDB.getDB();
            ps = db.prepareStatement("INSERT INTO users VALUES (?, ?, ?, ?)");
            ps.setString(1, user.getLogin());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getRole());

            ps.executeUpdate();

            System.out.println("L'utilisateur" + user.getLogin() + "a été ajouté!");

        } catch (SQLException e) {

            System.out.println(e.getMessage());

        } finally {

            if (ps != null) {
                ps.close();
            }

            if (db != null) {
                db.close();
            }

        }
    }

}
