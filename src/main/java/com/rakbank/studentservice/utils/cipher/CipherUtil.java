package com.rakbank.studentservice.utils.cipher;

import com.atypon.literatum.registry.Conf;

public class CipherUtil {
    private static AESCipher aesCipher;
    private static ACSCipher acsCipher;
    private static ENCRYPTION_ALG encryptionAlg;
    private static CipherUtil defaultInstance;

    public enum ENCRYPTION_ALG {
        AES,
        DES
    };

    static {
        aesCipher=AESCipher.getInstance();
        acsCipher=ACSCipher.getInstance();
        defaultInstance = new CipherUtil();
        String alg=Conf.getString("security/encryption-algorithm");
        if(alg!=null && alg.equalsIgnoreCase("AES")) {
            encryptionAlg=ENCRYPTION_ALG.AES;
        } else {
            encryptionAlg=ENCRYPTION_ALG.DES;
        }
    }

    public static CipherUtil getInstance() {
        return defaultInstance;
    }

    public static ENCRYPTION_ALG getEncryptionAlg() {
        return encryptionAlg;
    }

    public byte[] encrypt(String clearText) {
        if(encryptionAlg==ENCRYPTION_ALG.DES) {
            return acsCipher.encrypt(clearText);
        } else {
            return aesCipher.encrypt(clearText);
        }

    }

    public String encryptToText(String clearText, boolean urlFriendly) {
        if(encryptionAlg==ENCRYPTION_ALG.DES) {
            return acsCipher.encryptToText(clearText, urlFriendly);
        } else {
            return aesCipher.encryptToText(clearText, urlFriendly);
        }
    }

    public String encryptToText(String clearText) {
        if(encryptionAlg==ENCRYPTION_ALG.DES) {
            return acsCipher.encryptToText(clearText);
        } else {
            return aesCipher.encryptToText(clearText);
        }
    }

    public String encryptToBase64URLText(String clearText) {
        if(encryptionAlg==ENCRYPTION_ALG.DES) {
            return acsCipher.encodeToBase64URL(clearText);
        } else {
            return aesCipher.encryptToText(clearText);
        }
    }

    public String decryptFromBase64URLText(String secretText) {
        if(encryptionAlg==ENCRYPTION_ALG.DES) {
            return acsCipher.decodeBase64URL(secretText);
        } else {
            return aesCipher.decryptFromText(secretText, false);
        }
    }

    public String decrypt(byte[] secretBytes) {
        if(encryptionAlg==ENCRYPTION_ALG.DES) {
            return acsCipher.decrypt(secretBytes);
        } else {
            return aesCipher.decrypt(secretBytes);
        }
    }

    public String decryptFromText(String secretText, boolean urlFriendly) {
        if(encryptionAlg==ENCRYPTION_ALG.DES) {
            return acsCipher.decryptFromText(secretText, urlFriendly);
        } else {
            return aesCipher.decryptFromText(secretText, urlFriendly);
        }
    }

    public String decryptFromText(String secretText) {
        if(encryptionAlg==ENCRYPTION_ALG.DES) {
            return acsCipher.decryptFromText(secretText);
        } else {
            return aesCipher.decryptFromText(secretText);
        }
    }
}
