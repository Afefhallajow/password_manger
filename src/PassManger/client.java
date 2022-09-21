package PassManger;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.net.Socket;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Random;
import java.util.Scanner;

public class client {
    private Socket socket;
    Scanner scanner = null;
    Scanner in = null;
    ObjectInputStream objectInput;
    PrintWriter out = null;
    String Symkey = "";
    ObjectOutputStream op=null;
PrivateKey privateKey1;
    EncDecSymmetric symmetric = new EncDecSymmetric();
String SessionKey;
    PublicKey publicKey;
   // static MyPGP pgp =new MyPGP();
    static Scanner in1 = new Scanner(System.in);
    private User user = new User();
    RSA rsa;
    String mac = "";
Digital_Signature digital_signature=new Digital_Signature();


    public client() throws IOException, SQLException, ClassNotFoundException {
        this.socket = new Socket("127.0.0.1", 5900);
        ;
        scanner = new Scanner(System.in);
        in = new Scanner(this.socket.getInputStream());
        out = new PrintWriter(this.socket.getOutputStream(), true);
        objectInput=new ObjectInputStream(this.socket.getInputStream());
        op=new ObjectOutputStream(socket.getOutputStream());
    }

    public boolean Login() throws IOException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, ClassNotFoundException {

        boolean iflogin = false;
        String line = "";
        System.out.println("please enter name");

        String name = scanner.nextLine();
       byte[] aa= rsa.encrypt(this.publicKey,name);
        op.writeObject(aa);
        //out.println(name);
        System.out.println("please enter password");

                line = scanner.nextLine();
        byte[] aa1= rsa.encrypt(this.publicKey,line);
        op.writeObject(aa1);

       // out.println(line);




        String iftrue = in.next();
        //System.out.println(iftrue);
        if (iftrue.equals("done")) {
            iflogin = true;
            user.setUsername(name);
            user.setPassword(line);
//symmetric.MakeMac(user.getPassword());

            File a=new File("M:\\desktop\\"+name+".txt");
            FileInputStream file1 = new FileInputStream(a);

            ObjectInputStream objectInputStream = new ObjectInputStream(file1);
            privateKey1=(PrivateKey) objectInputStream.readObject();

            this.mac = in.next();
            System.out.println(this.mac);
            this.Symkey = this.user.getPassword();

        }
        System.out.println("close");
        return iflogin;


    }






    public String send_mac() {
        String Mac = this.mac;
        //System.out.println(Mac);
// symmetric.ConvertToString(  symmetric.getMac_array());
        out.println(Mac);
//out.println(user.getPassword());
        return Mac;
    }
    public boolean recive_request_mac() {
        boolean a=false;
        String Mac = this.mac;
        System.out.println("mac is : "+Mac);

        String temp= in.next();

        if(temp.equals("done"))
        {
            System.out.println("MAC is ok !!");
            a=true;}
        else {
            a=false;
            System.out.println("MAC is wrong !!");
        }
        return a ;
    }

    public String Edite() throws IOException {

        String line = "";
        line = scanner.nextLine();
        out.println(line);
        line = scanner.nextLine();
        out.println(line);

        System.out.println(in.nextLine());

        System.out.println("close");
        return "done";

    }

    public String Register() throws IOException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {



        RSA privateRsa=new RSA();

        privateRsa.generate();
        PublicKey publicKey1=privateRsa.publicKey;
        this.privateKey1= privateRsa.privateKey;
        op.writeObject(publicKey1);
        String line = "";
        System.out.println("please enter name");

        String name = scanner.nextLine();
        byte[] aa= rsa.encrypt(this.publicKey,name);
        op.writeObject(aa);
        //out.println(name);
        System.out.println("please enter password");

        line = scanner.nextLine();
        byte[] aa1= privateRsa.encrypt1(privateRsa.privateKey,line);
        op.writeObject(aa1);


        File a=new File("M:\\desktop\\"+name+".txt");

        if (!a.exists())
        {
            a.createNewFile();
        }
        FileOutputStream  file1=new FileOutputStream(a);
        ObjectOutputStream outputStream=new ObjectOutputStream(file1);
        outputStream.writeObject(this.privateKey1);

        System.out.println(in.next());

        System.out.println("close");
        return "done";
    }


    public String aaa() throws Exception {



        byte[] array= new byte[10];
        Random random=new Random();
        String salt=random.ints(97,122+1).limit(4).collect(StringBuilder::new,StringBuilder::appendCodePoint,StringBuilder::append).toString();
        this.SessionKey=salt;
        symmetric.session=this.SessionKey;
        System.out.println("session is : "+salt);

        rsa=new RSA();
        byte[] aa= rsa.encrypt(publicKey,salt);
        op.writeObject(aa);

        System.out.println("session share is done");
        System.out.println("=================================================================");
        String key = "";


        while (true) {

            System.out.println("\nHello to PassManger");
            System.out.println("1: Login");
            System.out.println("2: Register");

            System.out.println("3: Exit");



            key = scanner.nextLine();
            if (key.equals("1")) {
                System.out.println("**** login ****");

                out.println("login");
                boolean log = this.Login();
                System.out.println("-----------------------------------------------------------------");

                //System.out.println( in.next());
                if (log) {
                    while (true) {
                        System.out.println("\n1:addPasswordObject");
                        System.out.println("2:getallPassword");
                        System.out.println("3:Edit");
                        System.out.println("4:Getone");
                        System.out.println("5:send a request to share object ");
                        System.out.println("6:show share object requests");
                        System.out.println("7:show share object");
                        System.out.println("8:exit");



                        System.out.println("please enter the number you want");


                        String key1 = scanner.next();

                        if (key1.equals("1")) {

                            System.out.println("**** add ****");

                            out.println("add");
                            this.send_mac();
                            boolean a= this.recive_request_mac();
                          if(a) {
                              this.Add_pass_opject();
                          }else{
                              System.out.println("access dined");
                          }

                            System.out.println("-----------------------------------------------------------------");
                        }
                        if (key1.equals("2")) {

                            out.println("get");
                            System.out.println("**** get all ****");
                            this.send_mac();
                            boolean a= this.recive_request_mac();
                            if(a) {

                                int size = in.nextInt();
                                System.out.println(size);
                                this.getPassArray(size);
                            }
                            else {
                                System.out.println("access dined");

                            }
                            System.out.println("-----------------------------------------------------------------");
//

                        }
                        if (key1.equals("3")) {
                            out.println("edite");

                            System.out.println("**** edit ****");
                            this.send_mac();
                            boolean a= this.recive_request_mac();
                            if (a) {
                                int size = in.nextInt();
                                System.out.println(size);
                                this.getPassArray(size);
                                System.out.println("please enter the number of object you want to change");

                                int num = scanner.nextInt();
                                out.println(num);
                                System.out.println("accept");

                                this.EditOpject(num);
                                System.out.println("done");
                            }
                        else {
                            System.out.println("access dined");
                            }
                            System.out.println("-----------------------------------------------------------------");
                        }
                        if (key1.equals("4")) {
                            out.println("getone");
                            System.out.println("**** get one ****");

                            this.send_mac();
                            boolean a=   this.recive_request_mac();
                            if (a) {

//                                int size = in.nextInt();
//                                System.out.println(size);
//                                this.getPassArray(size);
                                System.out.println("please enter the number of object you want to get");
                                int num = scanner.nextInt();
                                out.println(num);
                                System.out.println("accept");
                                this.GetOne();
                            }
                            System.out.println("-----------------------------------------------------------------");
                        }

                        if (key1.equals("5")) {
                            System.out.println("**** share ****");

                            out.println("share");
                            // out.println("getone");
                            this.send_mac();
                            boolean a=   this.recive_request_mac();
                            if (a) {

                                int size = in.nextInt();
                                System.out.println(size);
                                this.getPassArray(size);
                                System.out.println("please enter the number of object you want to share");
                                int num = scanner.nextInt();
                                out.println(num);
                                System.out.println("accept");
                                this.share();
                            }
                            System.out.println("-----------------------------------------------------------------");
                        }
                        if (key1.equals("6")) {
                            out.println("showshare");
                            System.out.println("**** show share object ****");

                            this.send_mac();
                            boolean a=   this.recive_request_mac();
                            if (a) {
                                this.showsharewithrequest();
                            }
                            System.out.println("-----------------------------------------------------------------");
                        }
                        if (key1.equals("7")) {

                            System.out.println("**** show object ****");

                            out.println("showpass");

                            this.send_mac();
                            boolean a=   this.recive_request_mac();
                            if (a) {
                                this.showsharepass();
                            }
                            System.out.println("-----------------------------------------------------------------");
                        }

                        if (key1.equals("8")) {
                           // out.println("exit");

                            break;
                        }

                    }
                }
            }


            if (key.equals("2")) {
                System.out.println("**** Register ****");

                out.println("Register");
                this.Register();

                // String ss=in1.next();
                System.out.println("-----------------------------------------------------------------");
            }

            if (key.equals("3")) {

                out.println("exit");
                break;
                    // String ss=in1.next();

            }

        }

        return ";";
    }


    public void Add_pass_opject() throws Exception {


        String line = "";
        System.out.println("please enter password");
        line = scanner.next();
        String line1 = symmetric.encode(line, Symkey);
        out.println(line1);
        String vald=        this.recive_signiture(line);

        if (vald.equals("done"))
        {

        System.out.println("please enter address");

        line = scanner.next();
        line1 = symmetric.encode(line, Symkey);

        out.println(line1);

        System.out.println("please enter Description");
        line = scanner.next();
        line1 = symmetric.encode(line, Symkey);

        out.println(line1);

//        System.out.println(in.nextLine());

        System.out.println("close");
        symmetric.decode(line1,Symkey);

    } else
{

    System.out.println("access denid");
}
    }


    public String[] getPassArray(int size) {

        String[] arr = new String[size];
        System.out.println("\n==============================");
        System.out.println("the list : \n");
        String daaa,aaa;
        int j=0;
        for (int i = 0; i < size; i++) {


            j=i+1;
            System.out.println("number << "+ j +" >>");

            aaa = in.next();
            daaa = symmetric.decode(aaa, Symkey);
            System.out.println("\tpassword is "+ daaa);


            aaa = in.next();
            daaa = symmetric.decode(aaa, Symkey);
            System.out.println("\taddress is "+ daaa);

            aaa = in.next();
            daaa = symmetric.decode(aaa, Symkey);
            System.out.println("\tdescrepation is "+ daaa);

            if(i!=size-1){System.out.println("-------------------------------");}
            // System.out.println("done");
            arr[i] = daaa;

        }
        System.out.println("==============================\n");
        return arr;


    }

    public void EditOpject(int num) throws Exception {
        String password = in.next();
        String address = in.next();
        String description = in.next();
        String password1 = symmetric.decode(password, Symkey);
        String address1 = symmetric.decode(address, Symkey);
        String description1 = symmetric.decode(description, Symkey);
        Pass pass = new Pass();
        pass.setPassword(password1);
        pass.setAddress(address1);
        pass.setDescription(description1);

        System.out.println("\n====================================");
        System.out.println("the current data : ");
        System.out.println("password is "+pass.getPassword());
        System.out.println("address is "+pass.getAddress());
        System.out.println("description is "+pass.getDescription());
        System.out.println("====================================\n");
        System.out.println("new value");

        String line = "";
        System.out.println("please enter password");
        line = scanner.next();
        String line1 = symmetric.encode(line, Symkey);
        out.println(line1);
        System.out.println(line1);
        String valdite = this.recive_signiture(line);

        if (valdite.equals("done")) {
            System.out.println("please enter address");

            line = scanner.next();
            line1 = symmetric.encode(line, Symkey);

            out.println(line1);

            System.out.println("please enter Description");
            line = scanner.next();
            line1 = symmetric.encode(line, Symkey);

            out.println(line1);
            System.out.println("done");

        } else {
System.out.println("acsses dined");
        }
    }
    public void GetOne()

{
    String password = in.next();
    String address = in.next();
    String description = in.next();
    String password1 = symmetric.decode(password, Symkey);
    String address1 = symmetric.decode(address, Symkey);
    String description1 = symmetric.decode(description, Symkey);
    Pass pass = new Pass();
    pass.setPassword(password1);
    pass.setAddress(address1);
    pass.setDescription(description1);


    System.out.println("===========================================");
    System.out.println("password is "+pass.getPassword());
    System.out.println("Address is "+pass.getAddress());
    System.out.println("Description is "+pass.getDescription());
    System.out.println("===========================================");


}
    public static void main(String[] args) throws Exception {
    client client =new client();
    boolean a = client.Certeficate();
    if(a) {
    client.aaa();
    }else {
    System.out.println("access denid");
    }
    }
 ///////////////////// request 5///////////////////////
////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    public void  share () throws Exception {
        System.out.println("please enter the name of user that you want to share with him ");
        String name= scanner.next();
        System.out.println(name);
        out.println(name);
        String a= this.recive_signiture(name);
        if(a.equals("done")) {
            System.out.println(in.next());
        }
        else {
            System.out.println("accses dined");
        }
    }



public void showsharewithrequest()
{
    int size =   in.nextInt();
    System.out.println("===========================================");
    for (int i =0; i< size;i++) {
        int f = i + 1;
        System.out.println("the " + f + " request is ");
        System.out.println("the user send is" + in.next());

        System.out.println("the password  is" + in.next());
        }

        System.out.println("===========================================");

        System.out.println("choose what object you want ");
        int kk=scanner.nextInt();
        out.println(kk);

        System.out.println("what you want to do");
        System.out.println("1: accept");
        System.out.println("2: refuse ");
        int kk1=scanner.nextInt();
        out.println(kk1);
    System.out.println(in.next());






}

    public void showsharepass() {
        int size = in.nextInt();
        for (int i = 0; i < size; i++) {
            int f = i + 1;
            System.out.println("/////////////////////////////");

            System.out.println("the " + f + " request is ");
            System.out.println("the user send is " + in.next());

            System.out.println("the password  is " + in.next());
            System.out.println("the Description  is " + in.next());
            System.out.println("the address  is " + in.next());

            System.out.println("/////////////////////////////");


        }
    }
public  String recive_signiture(String message) throws Exception {

    System.out.println(" * create digital_signature *");
        byte[] signiture= digital_signature.Create_Digital_Signature(Base64.getDecoder().decode(message),privateKey1);
    op.writeObject(signiture);
    System.out.println(message);

    String valedate  = in.next();
    System.out.println(valedate);
    String a="ass";
String aa=Base64.getEncoder().encodeToString( rsa.encrypt1(privateKey1,a));
out.println(aa);

    System.out.println("signeture is "+valedate);

    return valedate;
}

public  String bassel_sig(String msg ) throws IOException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
    Sha sha=new Sha();
    String HashFunction="SHA-256";

    String value=sha.tohexastring(sha.getsha(msg,HashFunction));

    byte[] valuebyte=rsa.encrypt1(privateKey1,value);
    //  System.out.println("v= "+valuebyte);
    //2
///    out.println(HashFunction);
    //3
    op.writeObject(valuebyte);

    System.out.println("waiting");
//String vald="";
    String result= in.next();
    if(result.equals("true")){
        System.out.println(" you use a validate signature");
return "done";
    }
    else {
        System.out.println("something got wrong with signature");
return "acsses ";
    }

}


    public   boolean Certeficate() throws NoSuchAlgorithmException, IOException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchPaddingException, ClassNotFoundException, CertificateException, SignatureException, NoSuchProviderException {

        boolean a=false;
        X509Certificate cert=(X509Certificate) objectInput.readObject();


        ///////////////////////////////////////////////////////////////////////////////////

        System.out.println("got certificate");
        Socket socket_ca = new Socket("127.0.0.1", 8800);




        Scanner in_ca = new Scanner(socket_ca.getInputStream());
        PrintWriter out_ca = new PrintWriter(socket_ca.getOutputStream(), true);

        ObjectInputStream objectInput_ca=new ObjectInputStream(socket_ca.getInputStream());

        ObjectOutputStream op_ca=new ObjectOutputStream(socket_ca.getOutputStream());

        out_ca.println("get_public");


        PublicKey publicKey_ca = (PublicKey) objectInput_ca.readObject();

        System.out.println("verifying .... ");
        cert.verify(publicKey_ca);
        System.out.println("done !! ");

        if (cert.getSubjectDN().toString().contains("passmanger")){
            System.out.println("it's from pass manger");
        a=true;

        publicKey=cert.getPublicKey();
            System.out.println("certificate is ok !!!");
            System.out.println("=================================================================");
        }
        else {
            System.out.println("wrong cert !! ");
            a=false;
        }

            return a;
    }


}