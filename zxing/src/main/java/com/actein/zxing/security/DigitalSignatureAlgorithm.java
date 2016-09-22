package com.actein.zxing.security;

import android.util.Base64;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public class DigitalSignatureAlgorithm {

    public DigitalSignatureAlgorithm(String publicKeyBase64) throws DigitalSignatureException {
        try {
            byte[] rawPublicKey = Base64.decode(publicKeyBase64, Base64.DEFAULT);
            PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(
                    new X509EncodedKeySpec(rawPublicKey)
            );

            mSignature = Signature.getInstance("SHA256withRSA");
            mSignature.initVerify(publicKey);
        }
        catch (NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException ex) {
            throw new DigitalSignatureException(ex);
        }
    }

    public boolean verifyData(byte[] data, byte[] signature) throws DigitalSignatureException {
        try {
            mSignature.update(data);
            return mSignature.verify(signature);
        }
        catch (SignatureException ex) {
            throw new DigitalSignatureException(ex);
        }
    }

    private Signature mSignature = null;
}
