package PassManger;
import javax.crypto.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Map;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.security.*;
import java.util.Base64;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class EncDecSymmetric
{
    // Symmetric encryption algorithms supported - AES, RC4, DES
    protected static String DEFAULT_ENCRYPTION_ALGORITHM = "AES";
    protected static int DEFAULT_ENCRYPTION_KEY_LENGTH = 256;
    protected Mac mac;
    protected SecretKey mSecretKey;
    protected String mEncryptionAlgorithm, mKeyEncryptionAlgorithm, mTransformation;
    protected int mEncryptionKeyLength, mKeyEncryptionKeyLength;
    protected PublicKey mPublicKey;
    protected PrivateKey mPrivateKey;
String session;
    protected  byte [] mac_array;
    EncDecSymmetric()
    {
        mSecretKey = null;
        mac=null;
        mEncryptionAlgorithm = EncDecSymmetric.DEFAULT_ENCRYPTION_ALGORITHM;
        mEncryptionKeyLength = EncDecSymmetric.DEFAULT_ENCRYPTION_KEY_LENGTH;
    }

    public static BigInteger keyToNumber(byte[] byteArray)
    {
        return new BigInteger(1, byteArray);
    }

    public SecretKey getSecretKey()
    {
        return mSecretKey;
    }

    public byte[] getSecretKeyAsByteArray()
    {
        return mSecretKey.getEncoded();
    }

    public String getEncodedPublicKey()
    {
        String encodedKey = Base64.getEncoder().encodeToString(mPublicKey.getEncoded());
        return encodedKey;
    }

    // get base64 encoded version of the key
    public String getEncodedSecretKey()
    {
        String encodedKey = Base64.getEncoder().encodeToString(mSecretKey.getEncoded());
        return encodedKey;
    }

    public void generateSymmetricKey(String password,String salt )
    {
        KeyGenerator generator;
        try {
            generator = KeyGenerator.getInstance(mEncryptionAlgorithm);
            generator.init(mEncryptionKeyLength);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 256);
            SecretKey secret = new SecretKeySpec(factory.generateSecret(spec)
                    .getEncoded(), "AES");

                    mSecretKey = secret;
             mac = Mac.getInstance("HmacSHA256");

            //Initializing the Mac object
            mac.init(mSecretKey);

        }
        catch (NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException e) {
            e.printStackTrace();
        }
    }

    public byte[] encryptText(String textToEncrypt)
    {
        byte[] byteCipherText = null;

        try {
            Cipher encCipher = Cipher.getInstance(mEncryptionAlgorithm);
            Security.setProperty("crypto.policy", "unlimited");
            encCipher.init(Cipher.ENCRYPT_MODE, mSecretKey);
            byteCipherText = encCipher.doFinal(textToEncrypt.getBytes());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }

        return byteCipherText;
    }

    public String decryptText(byte[] decryptedKey, byte[] encryptedText)
    {
        String decryptedPlainText = null;

        try {
            SecretKey originalKey = new SecretKeySpec(decryptedKey , 0, decryptedKey.length, mEncryptionAlgorithm);
            Cipher aesCipher2 = Cipher.getInstance(mEncryptionAlgorithm);
            aesCipher2.init(Cipher.DECRYPT_MODE, originalKey);
            byte[] bytePlainText = aesCipher2.doFinal(encryptedText);
            decryptedPlainText = new String(bytePlainText);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }

        return decryptedPlainText;
    }
    public static void fixKeyLength() {
        String errorString = "Failed manually overriding key-length permissions.";
        int newMaxKeyLength;
        try {
            if ((newMaxKeyLength = Cipher.getMaxAllowedKeyLength("AES")) < 256) {
                Class c = Class.forName("javax.crypto.CryptoAllPermissionCollection");
                Constructor con = c.getDeclaredConstructor();
                con.setAccessible(true);
                Object allPermissionCollection = con.newInstance();
                Field f = c.getDeclaredField("all_allowed");
                f.setAccessible(true);
                f.setBoolean(allPermissionCollection, true);

                c = Class.forName("javax.crypto.CryptoPermissions");
                con = c.getDeclaredConstructor();
                con.setAccessible(true);
                Object allPermissions = con.newInstance();
                f = c.getDeclaredField("perms");
                f.setAccessible(true);
                ((Map) f.get(allPermissions)).put("*", allPermissionCollection);

                c = Class.forName("javax.crypto.JceSecurityManager");
                f = c.getDeclaredField("defaultPolicy");
                f.setAccessible(true);
                Field mf = Field.class.getDeclaredField("modifiers");
                mf.setAccessible(true);
                mf.setInt(f, f.getModifiers() & ~Modifier.FINAL);
                f.set(null, allPermissions);

                newMaxKeyLength = Cipher.getMaxAllowedKeyLength("AES");
            }
        } catch (Exception e) {
            throw new RuntimeException(errorString, e);
        }
        if (newMaxKeyLength < 256)
            throw new RuntimeException(errorString); // hack failed
    }


    public Mac getMac() {
        return mac;
    }

    public byte[] getMac_array() {
        return mac_array;
    }

    public byte[] MakeMac(String password)
{// = new EncDecSymmetric();
    this.fixKeyLength();
    this.generateSymmetricKey(password,this.session);
    byte[] macResult = this.mac.doFinal();
this.mac_array=macResult;
    System.out.println("Mac result:");
    System.out.println(new String(macResult));

    return macResult;
}
public String ConvertToString(byte[] array)
{

    return Base64.getEncoder().encodeToString(array);
}
    public byte[] ConvertTobyte(String array)
    {

        return Base64.getDecoder().decode(array);
    }
public String encode(String data,String key)
{this.fixKeyLength();
    this.generateSymmetricKey(key,this.session);
    byte[] secretKeyByteArray = this.getSecretKeyAsByteArray();
  //  System.out.println("secret key: '" + EncDecSymmetric.keyToNumber(secretKeyByteArray).toString() + "'" );


    //System.out.println("plainText: '" + data + "'");

    byte[] encryptedText = this.encryptText(data);
    String plainText = this.ConvertToString(encryptedText);

//    System.out.println("encrypted text: '" + plainText + "'" );
return plainText;
}
    public String decode(String data,String key)
    {this.fixKeyLength();
        this.generateSymmetricKey(key,this.session);
        byte[] secretKeyByteArray = this.getSecretKeyAsByteArray();
       // System.out.println("secret key: '" + EncDecSymmetric.keyToNumber(secretKeyByteArray).toString() + "'" );


       // System.out.println("plainText: '" + data + "'");

        byte[] encryptedText = this.ConvertTobyte(data);
        String plainText = this.ConvertToString(encryptedText);
        String decryptedText = this.decryptText(secretKeyByteArray, encryptedText);
       // System.out.println("decrypted text: '" + decryptedText + "'" );

        //System.out.println("encrypted text: '" + plainText + "'" );
        return decryptedText;
    }



    public static void main(String[] args) {
        EncDecSymmetric sed = new EncDecSymmetric();
        sed.fixKeyLength();
    /*    sed.generateSymmetricKey("cccc","afef2");
        byte[] secretKeyByteArray = sed.getSecretKeyAsByteArray();
        System.out.println("secret key: '" + EncDecSymmetric.keyToNumber(secretKeyByteArray).toString() + "'" );

        String plainText = "Hello World,";
        System.out.println("plainText: '" + plainText + "'");

        byte[] encryptedText = sed.encryptText(plainText);
        System.out.println("encrypted text: '" + EncDecSymmetric.keyToNumber(encryptedText).toString() + "'" );

        String decryptedText = sed.decryptText(secretKeyByteArray, encryptedText);
        System.out.println("decrypted text: '" + decryptedText + "'" );

        byte[] macResult = sed.mac.doFinal();

        System.out.println("Mac result:");
        System.out.println(new String(macResult));


     */
byte[] a= sed.MakeMac("aa");
byte[]t =sed.MakeMac("aa");
String aa=new String(t);
String bb =new String(a);
byte[] xx= aa.getBytes();
System.out.println(aa);
System.out.println(bb);
   System.out.println(new String(xx));
        String plainText = "Hello World,";
        System.out.println("plainText: '" + plainText + "'");

        byte[] encryptedText = sed.encryptText(plainText);
       String axz=Base64.getEncoder().encodeToString(encryptedText);
               //EncDecSymmetric.keyToNumber(encryptedText).toString() ;
        System.out.println("encrypted text: '" + EncDecSymmetric.keyToNumber(encryptedText).toString() + "'" );
System.out.println(axz);
        byte[] secretKeyByteArray = sed.getSecretKeyAsByteArray();
//byte[] sss=Base64.getDecoder().decode(axz);

   String decryptedText  =sed.decryptText(secretKeyByteArray,sed.ConvertTobyte(axz));
        System.out.println(decryptedText);
        if (aa.equals(bb))
    {
        System.out.println("1aaaaa");
    }
    }


}