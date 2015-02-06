package model;

/**
 * Created by Dikyx on 06/02/2015.
 */
public class User {

    private String login;
    private String password;
    private String role;

    public User(String login, String password, String role){
        this.login=login;
        this.password=password;
        this.role=role;
    }

    public String getLogin() {
        return this.login;
    }

    public String getPassword() {
        return this.password;
    }

    public String getRole() {
        return role;
    }
}
