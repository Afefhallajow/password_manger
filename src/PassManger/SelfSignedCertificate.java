package PassManger;


import org.bouncycastle.jce.X509Principal;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.x509.X509V3CertificateGenerator;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.Date;


public class SelfSignedCertificate {


    private static final String CERTIFICATE_ALIAS = "YOUR_CERTIFICATE_NAME";
    private static final String CERTIFICATE_ALGORITHM = "RSA";
    private String CERTIFICATE_DN = "CN=cn, O=o, L=L, ST=il, C= c";
    private PublicKey publicKey;
    private PrivateKey privateKey;
    private static final String CERTIFICATE_NAME = "keystore.test";
    private static final int CERTIFICATE_BITS = 1024;




    public SelfSignedCertificate(String Common_Name,String Organization_NAME,String Location,String State,String Country,PublicKey publicKey,PrivateKey privateKey) {
        Security.addProvider(new BouncyCastleProvider());
        this.CERTIFICATE_DN="CN="+Common_Name+", O="+Organization_NAME+", L="+Location+", ST="+State+", C="+Country;
        this.publicKey=publicKey;
        this.privateKey=privateKey;
    }



    @SuppressWarnings("deprecation")
    public X509Certificate createCertificate() throws Exception{
        X509Certificate cert = null;
        //KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(CERTIFICATE_ALGORITHM);
        //keyPairGenerator.initialize(CERTIFICATE_BITS, new SecureRandom());
        //KeyPair keyPair = keyPairGenerator.generateKeyPair();

        // GENERATE THE X509 CERTIFICATE
        X509V3CertificateGenerator v3CertGen =  new X509V3CertificateGenerator();
        v3CertGen.setSerialNumber(BigInteger.valueOf(System.currentTimeMillis()));
        v3CertGen.setIssuerDN(new X509Principal(CERTIFICATE_DN));
        v3CertGen.setNotBefore(new Date(System.currentTimeMillis() - 1000L * 60 * 60 * 24));
        v3CertGen.setNotAfter(new Date(System.currentTimeMillis() + (1000L * 60 * 60 * 24 * 365*10)));
        v3CertGen.setSubjectDN(new X509Principal(CERTIFICATE_DN));
        v3CertGen.setPublicKey(publicKey);
        v3CertGen.setSignatureAlgorithm("SHA256WithRSAEncryption");
        cert = v3CertGen.generateX509Certificate(privateKey);
//        saveCert(cert,keyPair.getPrivate());
        return cert;
    }

    private void saveCert(X509Certificate cert, PrivateKey key) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(null, null);
        keyStore.setKeyEntry(CERTIFICATE_ALIAS, key, "YOUR_PASSWORD".toCharArray(),  new java.security.cert.Certificate[]{cert});
        File file = new File(".", CERTIFICATE_NAME);
        keyStore.store( new FileOutputStream(file), "YOUR_PASSWORD".toCharArray() );
    }


   /* public static void main(String[] args) throws Exception {

    }
*/

}
