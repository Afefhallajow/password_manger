package PassManger;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.Random;
import java.util.Scanner;

//import org.bouncycastle.asn1.x500.X500Name;

public class ca implements Runnable{



    private Socket socket;
    private  String Username;
    private String SymetrecKey;

    private PrivateKey privateKey;
    private PublicKey publicKey;
    private PublicKey server_publicKey;



    public ca(Socket socket,RSA rsa) throws NoSuchAlgorithmException {
        this.socket = socket;



        privateKey=rsa.privateKey;
        publicKey=rsa.publicKey;


    }








    public X509Certificate get_cert(CSR csr,PrivateKey privateKey) throws Exception {

        SelfSignedCertificate selfSignedCertificate=new SelfSignedCertificate(csr.getCommon_Name(),csr.getOrganization_NAME()
                                                                             ,csr.getLocation(),csr.getState(),csr.getCountry()
                                                                             ,csr.getPublicKey(),privateKey);

        X509Certificate cert=  selfSignedCertificate.createCertificate();

        return cert;
    }





    @Override
    public void run() {


        try {



        Scanner scanner = new Scanner(System.in);


        ObjectOutputStream op=new ObjectOutputStream(socket.getOutputStream());
        Scanner in = new Scanner(socket.getInputStream());
        ObjectInputStream objectInput=new ObjectInputStream(this.socket.getInputStream());

        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);



                String action = in.nextLine();
                System.out.println(action);
                if (action.equals("get_cert")) {

                  // PublicKey publicKey_server = (PublicKey) objectInput.readObject();

                    //1
                    op.writeObject(this.publicKey);


                    //////////////////////////////////receive csr -///////////////////////////////////
                    CSR csr = (CSR) objectInput.readObject();

                    ///////////////////////////////////////////////////////////////////////////////


                    ///////////////////////////////////check if it is passmanger //////////////////////

                    boolean the_true_site = false;
                    Random random = new Random();
                    int t = random.nextInt(3);
                    if (t == 0) {
                        out.println("the_name_of_your_manger");

                        if (in.nextLine().equals("afef")) {
                            the_true_site = true;
                        }
                    } else if (t == 1) {
                        out.println("the_name_of_street_that_company_is_located_on");

                        if (in.nextLine().equals("Almatar_street")) {
                            the_true_site = true;
                        }
                    } else {
                        out.println("the_company_work");

                        if (in.nextLine().equals("IT")) {
                            the_true_site = true;
                        }
                    }

                    if (the_true_site) {
                        System.out.println("check is done !!!");
                        out.println("true");
                        //////////////////////////////////////////////////////////////////////////////////
                        X509Certificate cert = get_cert(csr, this.privateKey);
                        //2

                        op.writeObject(cert);
                    }else {
                        System.out.println("failed !!!");

                        out.println("true");
                    }
                    } else if (action.equals("get_public")) {
                        op.writeObject(this.publicKey);
                    }




        } catch (IOException | ClassNotFoundException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    }



