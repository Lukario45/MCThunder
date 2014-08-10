package net.mcthunder.auth.properties;

import net.mcthunder.auth.expection.SignatureValidateException;
import net.mcthunder.auth.util.Base64;

import java.security.PublicKey;
import java.security.Signature;

public class Property {
    private String name;
    private String value;
    private String signature;

    public Property(String value, String name) {
        this(value, name, null);
    }

    public Property(String name, String value, String signature) {
        this.name = name;
        this.value = value;
        this.signature = signature;
    }

    public String getName() {
        return this.name;
    }

    public String getValue() {
        return this.value;
    }

    public String getSignature() {
        return this.signature;
    }

    public boolean hasSignature() {
        return this.signature != null;
    }

    public boolean isSignatureValid(PublicKey key) throws SignatureValidateException {
        try {
            Signature sig = Signature.getInstance("SHA1withRSA");
            sig.initVerify(key);
            sig.update(this.value.getBytes());
            return sig.verify(Base64.decode(this.signature.getBytes("UTF-8")));
        } catch (Exception e) {
        }
        throw new SignatureValidateException("Could not validate property signature.");
    }
}