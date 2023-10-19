package org.example.login;

public class Authentication {

    private final String LOGIN = "admin";
    private final String PASSWORD = "admin";

    public boolean authentication(String login, String password) {
        if (login.equals(LOGIN) && password.equals(PASSWORD)) {
            return true;
        } else {
            return false;
        }
    }
}
