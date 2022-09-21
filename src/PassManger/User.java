package PassManger;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;


public class User implements Serializable {
private String salt="bassel";
private  String key="afef";
private int id;

    MysqlCon mysqlCon=new MysqlCon();
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    EncDecSymmetric symmetric=new EncDecSymmetric();
    private String username;
private HashMap<String,String> PasshashMap= new HashMap<>();
private ArrayList<Pass> PassArray= new ArrayList<>();
private String path1="D:\\desktop\\";
    public ArrayList<Pass> getPassArray() {
        return PassArray;
    }

    public void setPassArray(ArrayList<Pass> passArray) {
        PassArray = passArray;
    }

    public User() throws SQLException, ClassNotFoundException {
        path="D:\\desktop\\2.txt";
symmetric.session=this.salt;
    }

    private String password;
FileOutputStream file;
String path;
    public User(String username, String password) throws SQLException, ClassNotFoundException {
        this.username = username;
        this.password = password;
path="D:\\desktop\\2.txt";
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

public boolean Login(String username,String password) {
    if(this.getUsername()==username&&this.getPassword()==password) {
        return true;
    }
    else{

        return false;

    }
    }

  ///////////////////////////////////////////////////////to print in server

    public String GetOpject() throws IOException, ClassNotFoundException, SQLException {

     this.PassArray = mysqlCon.getpassbyusername(this.getUsername());
 for (int i=0;i<PassArray.size();i++)
     System.out.println("pass"+PassArray.get(i).getPassword().toString());
     return PassArray.toString();
   }
    public ArrayList<Pass> GetPassArrayOpject() throws IOException, ClassNotFoundException, SQLException {

            this.PassArray = mysqlCon.getpassbyusername(this.getUsername());
 ArrayList<Pass> arrayList=new ArrayList<>();
            for (int i=0;i<this.PassArray.size();i++) {            //System.out.println("pass"+PassArray.get(i).getPassword().toString());
Pass pass =new Pass();
pass.setId(this.PassArray.get(i).getId());
pass.setPassword(this.symmetric.decode(this.PassArray.get(i).getPassword(),this.getKey()));
                pass.setAddress(this.symmetric.decode(this.PassArray.get(i).getAddress(),this.getKey()));
                pass.setDescription(this.symmetric.decode(this.PassArray.get(i).getDescription(),this.getKey()));
arrayList.add(i,pass);


            }
            this.PassArray=arrayList;
            return this.PassArray;
    }

public String AddOpject(Pass pass1) throws IOException, ClassNotFoundException, SQLException {File a=new File("D:\\desktop\\"+this.getUsername()+".txt");
User user=new User(); //new Pass("ddd",this.username,"AQ","qqq")   ;
String pass=user.symmetric.encode(pass1.getPassword(),user.getKey());
    String dec=user.symmetric.encode(pass1.getDescription(),user.getKey());
    String addr=user.symmetric.encode(pass1.getAddress(),user.getKey());

    mysqlCon.savepass(pass,dec,addr,pass1.getUsername());
return "add Complete";
}

//    return "";

public String editAddopject(Pass pass) throws IOException, SQLException {
System.out.println("edite method");
        mysqlCon.updatepass(pass.getId(),pass.getId(),pass.getPassword(),pass.getDescription(),pass.getAddress());


        return "add new array";
    }




    public String[] GetPassArray() throws IOException, ClassNotFoundException, SQLException {
        this.PassArray =mysqlCon.getpassbyusername(this.getUsername());

        String[] namearray = new String[this.getPassArray().size()*3];

        int p=0,j=1,k=2;
        System.out.println("/////////////////////////////////");
        for (int i = 0; i < PassArray.size(); i++)
        {

            namearray[p]= this.symmetric.decode( PassArray.get(i).getPassword(),this.getKey());
            namearray[j]= this.symmetric.decode( PassArray.get(i).getAddress(),this.getKey());
            namearray[k]= this.symmetric.decode( PassArray.get(i).getDescription(),this.getKey());

            System.out.println("pass = "+ namearray[p]);
            System.out.println("add = "+ namearray[j]);
            System.out.println("des = "+ namearray[k]);
            System.out.println("----------------------------");

            p+=3;
            j+=3;
            k+=3;
        }
        System.out.println("/////////////////////////////////");

        return namearray;

    }

/////////////////////////////////////Register User//////////////////////////////////////////

    public User Register(String username,String pass ) throws IOException, ClassNotFoundException, SQLException {
        User newuser= new User(username,pass);

//String pass1= this.symmetric.encode(pass,this.getKey());
User user=        mysqlCon.getbyname(username);
//System.out.println(user.getUsername());
if (user == null)
        {
            mysqlCon.save(username,pass);

        }
        return newuser;
    }
    /////////////////////////////////////////////////////////////////////////////////////////////
public  HashMap<String,String> test() throws IOException, ClassNotFoundException, SQLException, InvalidKeySpecException, NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchPaddingException {
    User user = new User();

ArrayList<User>  arrayList= mysqlCon.findall();
int i=0;
while (i<arrayList.size()) {
    String encode=  mysqlCon.getkeybyname(arrayList.get(i).getUsername());
    System.out.println(arrayList.get(i).getUsername());
    if(!encode.equals(null)) {
        RSA rsa = new RSA();
        PublicKey publicKey = rsa.generatepublic(encode);
        String password1 = rsa.decode1(publicKey, Base64.getDecoder().decode(arrayList.get(i).getPassword()));


    PasshashMap .put(arrayList.get(i).getUsername(),password1)
;
i++;}}   return PasshashMap
;    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public  Pass EditOpjectpass(int num){
Pass pass=        PassArray.get(num);

    return pass;
}

/////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////public key save
    public String savekey(String encod,String username)
    {
        mysqlCon.savekey(encod,username);

        return "";    }




public String getpassword (String username,String password) throws SQLException, InvalidKeySpecException, NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchPaddingException {
  String encode=  mysqlCon.getkeybyname(username);
  RSA rsa =new RSA();
  PublicKey publicKey= rsa.generatepublic(encode);
    String password1=rsa.decode1(publicKey, Base64.getDecoder().decode(password));
return password1;
    }

////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////ShareWith/////////////////////
    public String savesharewith(Pass pass,String Sender,String Reciver) throws SQLException, ClassNotFoundException {

        mysqlCon.savesharewith(pass,Sender,Reciver);


        return "";

    }
    public ArrayList<sharewith> getsharewithbyname(String Reciver) throws SQLException, ClassNotFoundException {

       return mysqlCon.getsharewithbyname(Reciver);




    }
    public ArrayList<sharewith> getsharepassbyname(String Reciver) throws SQLException, ClassNotFoundException {

        return mysqlCon.getsharepassbyname(Reciver);




    }

    public String updatesharewith(int id,int accept) throws SQLException {

        return mysqlCon.updatesharewith(id,accept);




    }







public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {User aa=new User();
aa.setUsername("afef444444");
//aa.AddOpject();
aa.GetOpject();


}
}
