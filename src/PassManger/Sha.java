package PassManger;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Sha {

    public byte[] getsha(String data,String HashFunction) throws NoSuchAlgorithmException {
        MessageDigest md =MessageDigest.getInstance(HashFunction);
        return md.digest(data.getBytes(StandardCharsets.UTF_8));
    }

    public String tohexastring(byte[] hash){
        BigInteger number=new BigInteger(1,hash);
        StringBuilder hexstring=new StringBuilder(number.toString(16));

        while (hexstring.length()<32){
            hexstring.insert(0,'0');
        }
        return hexstring.toString();
    }

}
