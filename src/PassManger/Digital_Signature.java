package PassManger;// Java implementation for Generating
// and verifying the digital signature




// Imports

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.SimpleTypeHost;
import sun.rmi.runtime.NewThreadAction;

import javax.xml.bind.DatatypeConverter;
import java.security.*;
import java.util.Base64;
import java.util.Scanner;


public class Digital_Signature {



    // Signing Algorithm

    private   String SIGNING_ALGORITHM = "SHA256withRSA";

    private   String RSA = "RSA";

    private  Scanner sc;



    // Function to implement Digital signature

    // using SHA256 and RSA algorithm

    // by passing private key.

    public  byte[] Create_Digital_Signature(byte[] input, PrivateKey Key) throws Exception

    {

        Signature signature = Signature.getInstance(SIGNING_ALGORITHM);

        signature.initSign(Key);

        signature.update(input);

        return signature.sign();

    }



    // Generating the asymmetric key pair

    // using SecureRandom class

    // functions and RSA algorithm.

    public  KeyPair Generate_RSA_KeyPair()

            throws Exception

    {

        SecureRandom secureRandom = new SecureRandom();

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA);

        keyPairGenerator.initialize(2048, secureRandom);

        return keyPairGenerator.generateKeyPair();

    }



    // Function for Verification of the

    // digital signature by using the public key

    public String Verify_Digital_Signature(byte[] input, byte[] signatureToVerify, PublicKey key)

            throws Exception

    {

        Signature signature = Signature.getInstance(SIGNING_ALGORITHM);

        signature.initVerify(key);

        signature.update(input);

        boolean a= signature.verify(signatureToVerify);
System.out.println(a);
String a1= String.valueOf(a);
System.out.println("a1 = "+a1);
if (a1.equals("true")) {
   System.out.println("insid if");
    return "true";
}   else
    return "";
    }



    // Driver Code

    public static void main(String args[]) throws Exception

    {



        String input = "" + "";
Digital_Signature d=new Digital_Signature();
        KeyPair keyPair = d.Generate_RSA_KeyPair();

        PassManger.RSA rsa= new RSA();
        rsa.generate();
        PublicKey publicKey=rsa.generatepublic(Base64.getEncoder().encodeToString(rsa.publicKey.getEncoded()));
        // Function Call

        byte[] signature = d.Create_Digital_Signature(input.getBytes(),rsa.privateKey);



        System.out.println("Signature Value:\n " + DatatypeConverter.printHexBinary(signature));



        System.out.println("Verification: " + d.Verify_Digital_Signature(input.getBytes(), signature, publicKey));

 }}