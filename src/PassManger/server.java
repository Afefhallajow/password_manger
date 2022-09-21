package PassManger;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.jws.soap.SOAPBinding;
import javax.net.ssl.SSLSession;
import java.io.*;
import java.net.Socket;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Scanner;

public class server implements Runnable {
    private Socket socket;
    private  String Username;
    PublicKey userpublicKey;
    private String SymetrecKey;
    Digital_Signature digital_signature=new Digital_Signature();

    private PrivateKey privateKey;
    private PublicKey publicKey;
    RSA rsa;
    X509Certificate cert_server;




    private EncDecSymmetric symmetric=new EncDecSymmetric();
    public server(Socket socket) throws NoSuchAlgorithmException {
        this.socket = socket;

     }
String mac="";


    public void Register(Scanner scanner,Scanner in ,PrintWriter out,ObjectInputStream            objectInput) throws IOException, ClassNotFoundException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, SQLException, InvalidKeySpecException {
    String line ="";
    User user = new User();
    PublicKey publicKey= (PublicKey) objectInput.readObject();
       String encode=Base64.getEncoder().encodeToString(publicKey.getEncoded());

        byte[] aa= (byte[]) objectInput.readObject();
        String name= rsa.decode(rsa.privateKey,aa);
        user.setUsername(    name);
        byte[] aa1= (byte[]) objectInput.readObject();
        //String Password= rsa.decode(rsa.privateKey,aa1);

        user.setPassword(   Base64.getEncoder().encodeToString(aa1));


        out.println("done");
        System.out.println("Register is done");
        user.Register(user.getUsername(),user.getPassword());
       user.savekey(encode,user.getUsername());


       // System.out.println(user.test().toString());


}
public boolean Login(Scanner scanner,Scanner in ,PrintWriter out,ObjectInputStream            objectInput) throws IOException, ClassNotFoundException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, SQLException, InvalidKeySpecException {
        User user=new User();
HashMap<String,String> passhash=user.test();
System.out.println("Done");
boolean a =false;

byte[] aa= (byte[]) objectInput.readObject();
        String name= rsa.decode(rsa.privateKey,aa);

        user.setUsername(    name);
        byte[] aa1= (byte[]) objectInput.readObject();
        String Password= rsa.decode(rsa.privateKey,aa1);
//String pass =user.symmetric.encode(Password,user.getKey());


        user.setPassword(Password);
        System.out.println(user.getUsername()+user.getPassword());
        if((passhash.get(user.getUsername())==null))
        {
            System.out.println("NotFound");
            out.println("Notfound");
        }
    if (passhash.get(user.getUsername())!=null) {
        if (passhash.get(user.getUsername()).equals(user.getPassword())) {
            a = true;
            this.Username=user.getUsername();
            this.SymetrecKey=user.getPassword();
            out.println("done");
            System.out.println("done");
            symmetric.mac_array= symmetric.MakeMac(SymetrecKey);
            this.mac=  EncDecSymmetric.keyToNumber(symmetric.mac_array).toString();
            out.println(mac);
            MysqlCon mysqlCon =new MysqlCon();
            String encode=  mysqlCon.getkeybyname(name);
            userpublicKey=rsa.generatepublic(encode);

        } else{
            out.println("failed");

        System.out.println("failed");
    }
    }
        return a;

    }
    ////////////////////////////
  public boolean AddOpject(Scanner scanner, Scanner in , PrintWriter out, ObjectInputStream objectInput) throws Exception {

      User user=new User();
      //  HashMap<String,String> passhash=user.test();
      Pass pass=new Pass();
      boolean a =false;
      user.setUsername(    this.Username);
      System.out.println(user.getUsername());
      pass.setUsername(user.getUsername());
      String password=in.next();
      String password1=     symmetric.decode(password,SymetrecKey);
      String valdite= this.receive_signture(password1,out,objectInput);

      if(valdite.equals("done")) {
      String address = in.next();
      String description = in.next();
      String address1 = symmetric.decode(address, SymetrecKey);
      String description1 = symmetric.decode(description, SymetrecKey);

      pass.setPassword(password1);
      pass.setAddress(address1);
      pass.setDescription(description1);
      pass.setUsername(this.Username);
      user.AddOpject(pass);
      System.out.println(pass.getPassword());
      System.out.println(pass.getAddress());
      System.out.println("done");

      System.out.println(user.GetOpject());
      return a;

      }else
      return false;
      }



public boolean send_password_array_name(Scanner scanner,Scanner in ,PrintWriter out) throws IOException, ClassNotFoundException, SQLException {     User user=new User();


    Pass pass=new Pass();
    boolean a =false;
    user.setUsername(this.Username);
    String[]  size=  user.GetPassArray();


    out.println(size.length/3);
    System.out.println(size.length);

    for (int i=0;i<size.length;i++)
    { //System.out.println("send aaaa"+size[i]);
        String aaa=size[i];
        // System.out.println(""+size[i]);
        //System.out.println(aaa+i);
        String decodaaa= symmetric.encode(aaa,SymetrecKey);
        out.println(decodaaa);

    }

    return true;
}

public boolean editeOpject(Scanner scanner ,Scanner in ,PrintWriter out, ObjectInputStream objectInput,int num) throws Exception {
    User user=new User();
//    ObjectInputStream objectInput=new ObjectInputStream(socket.getInputStream());

    user.setUsername(this.Username);
    ArrayList<Pass> passArrayList=user.GetPassArrayOpject();
    Pass pass=new Pass();
    pass=passArrayList.get(num);

   int id=pass.getId();
    passArrayList.remove(pass);
    String password=pass.getPassword();
    String address=pass.getAddress();
    String description=pass.getDescription();
    String password1=     symmetric.encode(password,SymetrecKey);
    String address1=     symmetric.encode(address,SymetrecKey);
    String description1=     symmetric.encode(description,SymetrecKey);
    out.println(password1);
    out.println(address1);
    out.println(description1);
    System.out.println("waiting");
     password=in.next();
    password1=     symmetric.decode(password,SymetrecKey);

    String iftrue=   this.receive_signture(password1,out,objectInput);
    if(iftrue.equals("done"))
    {address=in.next();
     description=in.next();
     address1=     symmetric.decode(address,SymetrecKey);
     description1=     symmetric.decode(description,SymetrecKey);
    String  password2=     user.symmetric.encode(password1,user.getKey());
    String  address2=   user . symmetric.encode(address1,user.getKey());
    String description2=   user . symmetric.encode(description1,user.getKey());

    pass.setPassword(password2);
    pass.setAddress(address2);
    pass.setDescription(description2);
    passArrayList.add(num,pass);
    System.out.println(pass.getId());
    user.editAddopject(pass);

    return true;
    }
    else
        return false;
    }


    public boolean GetOne(Scanner scanner ,Scanner in ,PrintWriter out,int num) throws IOException, ClassNotFoundException, SQLException {
        User user=new User();
        user.setUsername(this.Username);
        ArrayList<Pass> passArrayList=user.GetPassArrayOpject();
        Pass pass=new Pass();
        pass=passArrayList.get(num);
        String password=pass.getPassword();
        String address=pass.getAddress();
        String description=pass.getDescription();
        String password1=     symmetric.encode(password,SymetrecKey);
        String address1=     symmetric.encode(address,SymetrecKey);
        String description1=     symmetric.encode(description,SymetrecKey);
        out.println(password1);
        out.println(address1);
        out.println(description1);

        return true;
    }
///////////////////////////////////////////////////////
    /////////////////////////////////////////////
    public boolean recive_mac(Scanner scanner , Scanner in ,PrintWriter out)
    {System.out.println("receive Mac please wait");
        boolean a=false;
    String Mac =       in.next();

    if (this.mac.equals(Mac))
    {
        System.out.println("Valid MAC");
        a=true;
        out.println("done");
    }
    else {
        System.out.println("Invalid MAC");
        out.println("access dined");
    }

    return a;
    }


    public boolean Send_request_mac(Scanner scanner , Scanner in ,PrintWriter out)
    {System.out.println("reciveMac please wait");
        boolean    a=false;
        String Mac =       in.next();
// String bassword =in.next();
        //String mac1= symmetric.ConvertToString(      symmetric.MakeMac(this.SymetrecKey));

        if (this.mac.equals(Mac))
        {

            a=true;
        }
        return a;
    }
   public void RSa()
   {}



    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        ObjectOutputStream op= null;

        rsa =new RSA();
        try {
            rsa.generate();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        this.publicKey=rsa.publicKey;




        try {
            op = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scanner in = null;
        try {
            in = new Scanner(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        ObjectInputStream            objectInput= null;
        try {
            objectInput = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        PrintWriter out = null;
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
//boo
        boolean nn= false;
        try {


            nn = this.get_certificat(scanner,in,out,op,objectInput);


        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        }
        if(nn)

        {
        try {

            System.out.println("Run");
     //boolean a= this.Login(scanner,in,out);

        byte[] aa= (byte[]) objectInput.readObject();

        String session= rsa.decode(rsa.privateKey,aa);
        symmetric.session=session;

            System.out.println("session after decode is : "+session);
            String key="";
            while (true)

          {key="";
          key=in.nextLine();
          if(key.equals("login")) {
              System.out.println(" **** Login hello ****");

              boolean a = this.Login(scanner, in, out,objectInput);
              System.out.println("service done");
              System.out.println("========================================");

          }


              if(key.equals("Register"))
              {
                  System.out.println("**** Register new user ****");
                  this.Register(scanner,in,out,objectInput);
                  System.out.println("service done");
                  System.out.println("========================================");

              }

          if (key.equals("add") ) {

             boolean a=   this.recive_mac(scanner,in,out);
            if (a) {
                System.out.println("**** AddObject ****");

             boolean a1 = this.AddOpject(scanner, in, out,objectInput);

                System.out.println("========================================");
            }
          }
          if (key.equals("getone") ) {

                 boolean a=   this.recive_mac(scanner,in,out);

                 if (a) {

                System.out.println("**** Get one ****");

               // boolean      a1 = this.send_password_array_name(scanner, in, out);
//              String aasdasf=in.next();
                int num=in.nextInt();
                System.out.println("accept");

                this.GetOne(scanner,in,out,num-1);
          }
              System.out.println("========================================");

          }


          if (key.equals("get")) {
              System.out.println("**** Getall ****");
              boolean a1=   this.recive_mac(scanner,in,out);
              if (a1) {
                  boolean a = this.send_password_array_name(scanner, in, out);
              }
              System.out.println("========================================");

          }

          if (key.equals("edite")) {
              System.out.println(" ***** edit **** ");
              boolean a1=   this.recive_mac(scanner,in,out);
              if (a1) {
                  boolean a = this.send_password_array_name(scanner, in, out);
                  //String aaasdacxz=in.next();
                  int num = in.nextInt();
                  System.out.println("accept");

                  this.editeOpject(scanner, in, out, objectInput,num-1);
                  System.out.println("done");
              }

              System.out.println("========================================");
          }


          if(key.equals("exit"))
          {System.out.println("exit");
              break;}


          if (key.equals("share")) {
          System.out.println("*** share request ***");
          boolean a1=   this.recive_mac(scanner,in,out);
          if (a1) {
              boolean a = this.send_password_array_name(scanner, in, out);
              //String aaasdacxz=in.next();
              int num = in.nextInt();

              System.out.println(num);
              System.out.println("accept");
              this.share(scanner, in, out, objectInput,num-1);
              System.out.println("done");
          }
              System.out.println("========================================");
          }

          if (key.equals("showshare")) {
              System.out.println("***show share requests ***");

              boolean a1=   this.recive_mac(scanner,in,out);
              if (a1) {
                  this.showshareRequest(scanner, in, out,0);
                  System.out.println("done");
              }
              System.out.println("========================================");
          }

          if (key.equals("showpass")) {
              System.out.println("***show share obiects ***");
              boolean a1=   this.recive_mac(scanner,in,out);
              if (a1) {
                  this.showsharepassword(scanner, in, out);
                  System.out.println("done");
              }
              System.out.println("========================================");
          }


      }
        }
     catch(
             IOException | ClassNotFoundException | NoSuchAlgorithmException e)

    {
        e.printStackTrace();
    } catch (Exception e) {
            e.printStackTrace();
        }

        try {
    socket.close();
} catch (IOException e) {
    e.printStackTrace();
}

    }
    else {
        System.out.println("acsses denid");
}




    }



////////////////////////////////////////////////////////////////////////////////////
    /////////////////////// 5 share opject///////////////////////////
    public void share(Scanner scanner ,Scanner in ,PrintWriter out,ObjectInputStream objectInpu,int num) throws Exception {
        String nam=in.next();
    String aa=    this.receive_signture(nam,out,objectInpu);

if(aa.equals("done")) {
    User user = new User();
    user.setUsername(this.Username);
    ArrayList<Pass> passArrayList = user.GetPassArrayOpject();
    Pass pass = new Pass();
    pass = passArrayList.get(num);
    int id = pass.getId();
    String password = pass.getPassword();
    String address = pass.getAddress();
    String description = pass.getDescription();
    System.out.println("please enter the name of user you want to share with");
    System.out.println("done");
    /// ///////////////// update the pass share with to name of the the user /////////////////
    user.savesharewith(pass, this.Username, nam);
    out.println("done");

}else
{
    System.out.println("acsses denid" );
}

    }

    public void showshareRequest(Scanner scanner ,Scanner in ,PrintWriter out,int num) throws SQLException, ClassNotFoundException, IOException {
        User user=new User();
        String username =this.Username;
user.setUsername(username);
        ///////////////////////////////////////////////////
////////// get all request/////////
        ArrayList<sharewith> list=    user.getsharewithbyname(username);
out.println(list.size());
        for (int i =0; i< list.size();i++)
{int f=i+1;
    System.out.println("the "+f+" request is ");
System.out.println("the user send is");
    System.out.println("the password  is");
    out.println(list.get(i).getSenderUsername());
out.println(list.get(i).pass.getPassword());


}


/////////////////////////////////////////////////
        /////////////////////accept//////////////
System.out.println("choose what opject you want ");
        int num1=in.nextInt();
sharewith sharewith= list.get(num1-1);
 System.out.println("what you wantt to do");
        int ifaccept =in.nextInt();

        if(ifaccept == 1 )
        {
///////update in database////////
       user.updatesharewith(sharewith.id,1);

        }
        else {
            user.updatesharewith(sharewith.id, 0);
        }

    out.println("done");

    }
 void showsharepassword(Scanner scanner ,Scanner in ,PrintWriter out) throws SQLException, ClassNotFoundException {
        User user=new User();

     user.setUsername(this.Username);
ArrayList<sharewith> list =user.getsharepassbyname(this.Username);
     out.println(list.size());
     for (int i =0; i< list.size();i++)
     {int f=i+1;
         System.out.println("the  " +f +"  request is ");
         System.out.println("the user send is ");
         System.out.println("the password  is ");
         out.println(list.get(i).getSenderUsername());
         out.println(list.get(i).pass.getPassword());
         out.println(list.get(i).pass.getDescription());
         out.println(list.get(i).pass.getAddress());


     }




 }

    public String receive_signture(String message,PrintWriter out,ObjectInputStream      objectInput) throws Exception {

        byte[] signiture= (byte[]) objectInput.readObject();
System.out.println(signiture.toString());
        System.out.println(message);
       // i.write(signiture);
          digital_signature.Verify_Digital_Signature(Base64.getDecoder().decode(message),signiture,userpublicKey);
String val=        digital_signature.Verify_Digital_Signature(Base64.getDecoder().decode(message),signiture,userpublicKey);
        String valedate="";
System.out.println("val = " + val);
        if ( val.equals("true"))
        {
            System.out.println("aaaaaa");
    valedate ="done";
}

        else{
    valedate="no";}
out.println(valedate);
Scanner in =new Scanner(socket.getInputStream());
String nn=in.next();
String aa= rsa.decode1(userpublicKey,Base64.getDecoder().decode(nn));
System.out.println(aa);
return valedate;

    }
public String bassel_sig(String msg ,Scanner in ,PrintWriter out,ObjectInputStream      objectInput) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, IOException, ClassNotFoundException {
    Sha sha=new Sha();

    //1

    //2
//    String HashFunction=in.next();

    String ValueServer=sha.tohexastring(sha.getsha(msg,"SHA-256"));

     System.out.println(ValueServer);

    //3

    byte[] signiture= (byte[]) objectInput.readObject();

//    byte[] ByteValueClient= (byte[]) objectInput.readObject();

    String ValueClient= rsa.decode1(userpublicKey,signiture);

    //   System.out.println(ValueClient);


    if (ValueClient.equals(ValueServer)){
        System.out.println(" you use a validate signature");
        out.println("true");
return "done";
    }
    else {
        System.out.println("something got wrong with signature");
        out.println("false");
return "acsses";
    }


}

    public boolean get_certificat(Scanner scanner , Scanner in ,PrintWriter out,ObjectOutputStream op,ObjectInputStream objectInput) throws IOException, NoSuchAlgorithmException, ClassNotFoundException, CertificateException, NoSuchProviderException, InvalidKeyException, SignatureException {
        boolean a=false;



        ////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

        Socket socket_ca = new Socket("127.0.0.1", 8800);


        ObjectOutputStream op_ca=new ObjectOutputStream(socket_ca.getOutputStream());
        Scanner in_ca = new Scanner(socket_ca.getInputStream());
        ObjectInputStream objectInput_ca=new ObjectInputStream(socket_ca.getInputStream());

        PrintWriter out_ca = new PrintWriter(socket_ca.getOutputStream(), true);



        out_ca.println("get_cert");

//        System.out.println(publicKey);
//        op_ca.writeObject(publicKey);



        //1
        PublicKey publicKey_ca=(PublicKey)objectInput_ca.readObject();

        //////////////////////////////////////////////////send csr /////////////////////////////////
        CSR csr=new CSR(publicKey,"passmanger","BA","MiddleEast","READY","Syria","AES");

        op_ca.writeObject(csr);



        //////////////////////////////////////////////////////////////////////////////////////////////////
        ///////////////////////////////////////////////check if it is passmanger ////////////////////////

        String question=in_ca.next();
        if(question.equals("the_name_of_your_manger"))
        {
            out_ca.println("afef");
        }
        else if(question.equals("the_name_of_street_that_company_is_located_on"))
        {
            out_ca.println("Almatar_street");
        }
        else if(question.equals("the_company_work"))
        {
            out_ca.println("IT");
        }

        String result=in_ca.next();
        if(result.equals("true")) {
            ////////////////////////////////////////////////////////////////////////////////////////////

            System.out.println("check is done !!!");
            //2
            X509Certificate cert = (X509Certificate) objectInput_ca.readObject();


            cert_server=cert;

            System.out.println(cert);


            System.out.println("verifying .... ");
            cert.verify(publicKey_ca);
            System.out.println("done !! ");


            /////////////////////////////////////////////////////
            System.out.println("trying client");


            op.writeObject(cert);

            a=true;
        }
        else {
            System.out.println("wrong");
        }



        return a;

    }







    }
