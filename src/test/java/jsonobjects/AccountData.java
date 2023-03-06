package jsonobjects;

import com.google.gson.GsonBuilder;

public class AccountData {
    private String login;
    private String password;

    public AccountData(String login,String password) {
        this.login = login;
        this.password = password;
    }
    public AccountData(){}


    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String toString() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }

}
