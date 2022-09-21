package PassManger;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.Charset;
import java.security.*;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Random;

public class RSA {
    PublicKey publicKey;
    PrivateKey privateKey;
    public byte[] encrypt(PublicKey publicKey, String encodString) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher encryptionCipher = Cipher.getInstance("RSA");
        encryptionCipher.init(Cipher.ENCRYPT_MODE, publicKey);
        String message = "world";
        byte[] encryptedMessage =
                encryptionCipher.doFinal(encodString.getBytes());
        String encryption =
                Base64.getEncoder().encodeToString(encryptedMessage);

        return encryptedMessage;
    }
    public byte[] encrypt1(PrivateKey publicKey, String encodString) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher encryptionCipher = Cipher.getInstance("RSA");
        encryptionCipher.init(Cipher.ENCRYPT_MODE, publicKey);
        String message = "world";
        byte[] encryptedMessage =
                encryptionCipher.doFinal(encodString.getBytes());
        String encryption =
                Base64.getEncoder().encodeToString(encryptedMessage);

        return encryptedMessage;
    }

    public String decode(PrivateKey privateKey, byte[] encryptedMessage) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher decryptionCipher = Cipher.getInstance("RSA");
        decryptionCipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedMessage =
                decryptionCipher.doFinal(encryptedMessage);
        String decryption = new String(decryptedMessage);
        System.out.println("decrypted message = " + decryption);


        return decryption;
    }


    public String decode1(PublicKey privateKey, byte[] encryptedMessage) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher decryptionCipher = Cipher.getInstance("RSA");
        decryptionCipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedMessage =
                decryptionCipher.doFinal(encryptedMessage);
        String decryption = new String(decryptedMessage);
        System.out.println("decrypted message = " + decryption);


        return decryption;
    }
public void generate() throws NoSuchAlgorithmException {
    KeyPairGenerator keyPairGenerator =
            KeyPairGenerator.getInstance("RSA");
    SecureRandom secureRandom = new SecureRandom();

    keyPairGenerator.initialize(2048, secureRandom);
    KeyPair pair = keyPairGenerator.generateKeyPair();

     this.publicKey = pair.getPublic();

    String publicKeyString =
            Base64.getEncoder().encodeToString(this.publicKey.getEncoded());

    System.out.println("public key = " + publicKeyString);

     this.privateKey = pair.getPrivate();
    String privateKeyString =
            Base64.getEncoder().encodeToString(this.privateKey.getEncoded());

    System.out.println("private key = " + privateKeyString);

    }



    public PublicKey generatepublic(String encode ) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory=KeyFactory.getInstance("RSA");

        X509EncodedKeySpec keySpec=new X509EncodedKeySpec(Base64.getDecoder().decode(encode));
        RSAPublicKey publicKey1;
        publicKey1=(RSAPublicKey) keyFactory.generatePublic(keySpec);

        return publicKey1;

    }

    public static void main(String[] args) throws NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException {


RSA rsa=new RSA();
rsa.generate();
PublicKey publicKey=rsa.publicKey;
KeyFactory keyFactory=KeyFactory.getInstance("RSA");
byte[] sss=publicKey.getEncoded();
String aaa = Base64.getEncoder().encodeToString(sss);

        X509EncodedKeySpec keySpec=new X509EncodedKeySpec(Base64.getDecoder().decode(aaa));
        RSAPublicKey publicKey1;
        publicKey1=(RSAPublicKey) keyFactory.generatePublic(keySpec);
byte[] aa= rsa.encrypt1(rsa.privateKey,"afefhallajow");
rsa.decode1(publicKey1,aa);







}



}


