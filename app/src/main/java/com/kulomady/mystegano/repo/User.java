package com.kulomady.mystegano.repo;

/**
 * @author kulomady on 6/10/18.
 */

public class User {

    private String username;
    private String password;
    private String email;
    private String secreetKey;
    private String digitalKeyUrl;

    public User(String username,
                String password,
                String email, String secreetKey, String digitalKeyUrl) {

        this.username = username;
        this.password = password;
        this.email = email;
        this.secreetKey = secreetKey;
        this.digitalKeyUrl = digitalKeyUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSecreetKey() {
        return secreetKey;
    }

    public void setSecreetKey(String secreetKey) {
        this.secreetKey = secreetKey;
    }

    public String getDigitalKeyUrl() {
        return digitalKeyUrl;
    }

    public void setDigitalKeyUrl(String digitalKeyUrl) {
        this.digitalKeyUrl = digitalKeyUrl;
    }
}
