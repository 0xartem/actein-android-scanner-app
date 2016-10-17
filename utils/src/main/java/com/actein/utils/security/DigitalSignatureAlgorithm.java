package com.actein.utils.security;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public class DigitalSignatureAlgorithm
{
    public DigitalSignatureAlgorithm(byte[] rawPublicKey) throws DigitalSignatureException
    {
        try
        {
            PublicKey publicKey = KeyFactory.getInstance("RSA")
                                            .generatePublic(new X509EncodedKeySpec(rawPublicKey));

            mSignature = Signature.getInstance("SHA256withRSA");
            mSignature.initVerify(publicKey);
        }
        catch (NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException ex)
        {
            throw new DigitalSignatureException(ex.getMessage(), ex);
        }
    }

    public boolean verifyData(byte[] data, byte[] signature) throws DigitalSignatureException
    {
        try
        {
            mSignature.update(data);
            return mSignature.verify(signature);
        }
        catch (SignatureException ex)
        {
            throw new DigitalSignatureException(ex.getMessage(), ex);
        }
    }

    private Signature mSignature = null;
}
