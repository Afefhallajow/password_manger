package PassManger;

public class sharewith {
Pass pass =new Pass();
String SenderUsername;
String ReciveUsername;
int id;

    public sharewith() {
    }

    public sharewith(int  pass_id, String password, String senderUsername, String reciveUsername, int id) {
        this.pass.setId(pass_id);
        this.pass.setPassword(password);
        SenderUsername = senderUsername;
        ReciveUsername = reciveUsername;
        this.id = id;
    }

    public Pass getPass() {
        return pass;
    }

    public void setPass(Pass pass) {
        this.pass = pass;
    }

    public String getSenderUsername() {
        return SenderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        SenderUsername = senderUsername;
    }

    public String getReciveUsername() {
        return ReciveUsername;
    }

    public void setReciveUsername(String reciveUsername) {
        ReciveUsername = reciveUsername;
    }
}
