package PassManger;

import java.security.PublicKey;

public class CSR implements java.io.Serializable{
    private PublicKey publicKey;
    private String Common_Name;
    private String Organization_NAME;
    private String Location;
    private String State;
    private String Country;
    private String alg_name;


    public CSR(PublicKey publicKey, String common_Name, String organization_NAME, String location, String state, String country, String alg_name) {
        this.publicKey = publicKey;
        Common_Name = common_Name;
        Organization_NAME = organization_NAME;
        Location = location;
        State = state;
        Country = country;
        this.alg_name = alg_name;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public String getCommon_Name() {
        return Common_Name;
    }

    public String getOrganization_NAME() {
        return Organization_NAME;
    }

    public String getLocation() {
        return Location;
    }

    public String getState() {
        return State;
    }

    public String getCountry() {
        return Country;
    }

    public String getAlg_name() {
        return alg_name;
    }
}
