package PassManger;

import java.io.Serializable;

public class Pass implements Serializable    {
private     String address;
private     String Username;
private     String Password;
private     String description;
private        int id;
private     String sharewith;

    public String getSharewith() {
        return sharewith;
    }

    public void setSharewith(String sharewith) {
        this.sharewith = sharewith;
    }

    public String getShareWith() {
        return ShareWith;
    }

    public void setShareWith(String shareWith) {
        ShareWith = shareWith;
    }

    private     String ShareWith;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Pass(String address, String username, String password, String description) {
        this.address = address;
       this.Username = username;
       this.Password = password;
        this.description = description;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Pass() {
    }
}

