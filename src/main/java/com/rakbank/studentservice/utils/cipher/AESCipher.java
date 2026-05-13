package com.rakbank.studentservice.utils.cipher;

import com.atypon.literatum.customization.UrlUtil;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.ws.Holder;
import java.io.*;
import java.security.AlgorithmParameters;
import java.security.SecureRandom;
import java.util.Base64;

public class AESCipher {

    private static final int PASSWORD_ITERATIONS = 65536;
    private static final int KEY_LENGTH          = 256;

    private static SecureRandom secureRandom;
    private static boolean generateSecret=false;

    private static AESCipher defaultInstance;

    static {
        secureRandom = new SecureRandom();
        secureRandom.setSeed(secureRandom.generateSeed(16));
        defaultInstance = new AESCipher();
    }

    public static AESCipher getInstance() {
        return defaultInstance;
    }

    private String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int c = secureRandom.nextInt(62);
            if (c <= 9) {
                sb.append(c);
            } else if (c < 36) {
                sb.append((char) ('a' + c - 10));
            } else {
                sb.append((char) ('A' + c - 36));
            }
        }
        return sb.toString();
    }

    private SecretKeySpec getSecret(String keyFilename)
    {
        ObjectInputStream in;
        SecretKeySpec secret = null;
        try
        {
            // try to read the secret from the file
            in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(keyFilename)));
            secret = (SecretKeySpec) in.readObject();
            in.close();
        }
        catch (Exception e)
        {
            if(generateSecret) { // this should not be enabled, except when run manually to generate a new secret, otherwise the secrets will be out of sync between webservers
                // secret file is not found, generate a secret
                ObjectOutputStream out;
                try {
                    char[] pass = generateRandomString(16).toCharArray();
                    byte[] salt = generateRandomString(8).getBytes();
                    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
                    PBEKeySpec spec = new PBEKeySpec(pass, salt, PASSWORD_ITERATIONS, KEY_LENGTH);

                    SecretKey secretKey = factory.generateSecret(spec);
                    secret = new SecretKeySpec(secretKey.getEncoded(), "AES");

                    out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(keyFilename)));
                    out.writeObject(secret);
                    out.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            } else {
                throw new RuntimeException(keyFilename+" not found or invalid, unable to configure cipher");
            }
        }
        return secret;
    }

    private Cipher createCipher(boolean encryptMode, Holder<byte[]> ivByteHolder) throws Exception {
        byte[] ivBytes=null;

        if(ivByteHolder!=null && ivByteHolder.value!=null)
            ivBytes=ivByteHolder.value;

        if (!encryptMode && ivBytes == null) {
            throw new IllegalStateException("ivBytes is null");
        }

        String aesKeyFile = System.getProperty("acs.security.aesKey");
        if (aesKeyFile == null)
        {
            String acsHome = WebAppContext.getInstance().getSiteRootDir();
            aesKeyFile = acsHome + "/WEB-INF/certs/AesSecretKey.ser";
        }

        SecretKeySpec secret = getSecret(aesKeyFile);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        int mode = encryptMode ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE;

        if (ivBytes == null || mode==Cipher.ENCRYPT_MODE) {
            cipher.init(mode, secret);
            AlgorithmParameters params = cipher.getParameters();
            ivBytes = params.getParameterSpec(IvParameterSpec.class).getIV();
            ivByteHolder.value = ivBytes;
        } else {
            cipher.init(mode, secret, new IvParameterSpec(ivBytes));
        }

        return cipher;
    }

    public byte[] encrypt(String clearText) {
        if (clearText == null)
            return null;

        try {
            Holder<byte[]> ivBytesHolder = new Holder(null);

            Cipher cipher = createCipher(true, ivBytesHolder);
            byte[] ivBytes = ivBytesHolder.value;

            byte[] encryptedBytes = cipher.doFinal(clearText.getBytes("UTF-8"));

            String encryptedVal = Base64.getEncoder().encodeToString(encryptedBytes);
            String encryptedIv = Base64.getEncoder().encodeToString(ivBytes);

            String val = encryptedVal.substring(0, encryptedVal.length() - 2) + "." + encryptedIv.substring(0, encryptedIv.length() - 2);
            return Base64.getEncoder().encode(val.getBytes("UTF-8"));
        } catch (Exception e)
        {
            Log.warn(Log.LITERATUM, "Error in encryption: " + e.toString());
        }
        return null;
    }

    public String encryptToText(String clearText, boolean urlFriendly)
    {
        if (clearText == null)
            return null;

        String secret = new String(encrypt(clearText)).replaceAll("\\s", "");
        return urlFriendly ? UrlUtil.urlEncode(secret) : secret;
    }

    public String encryptToText(String clearText) {
        return encryptToText(clearText, false);
    }

    public String decrypt(byte[] secretBytes) {
        if (secretBytes == null)
            return null;

        try {
            byte[] ivBytes = null;
            String encodedText = new String(Base64.getDecoder().decode(secretBytes));
            if (encodedText.contains(".")) {
                String encodedIv = encodedText.substring(encodedText.indexOf(".") + 1);
                encodedText = encodedText.substring(0, encodedText.indexOf(".")) + "==";
                ivBytes = Base64.getDecoder().decode(encodedIv + "==");
            }

            Holder<byte[]> ivBytesHolder = new Holder(ivBytes);
            Cipher cipher = createCipher(false, ivBytesHolder);

            byte[] encodedBytes = Base64.getDecoder().decode(encodedText);
            return new String(cipher.doFinal(encodedBytes), "UTF-8");
        } catch (Exception e) {
            Log.warn(Log.LITERATUM, "Error in decryption: " + e.toString());
        }
        return "";
    }

    public String decryptFromText(String secretText, boolean urlFriendly) {
        if(urlFriendly)
            secretText = UrlUtil.urlDecode(secretText);
        return decrypt(secretText.getBytes());
    }

    public String decryptFromText(String secretText) {
        return decryptFromText(secretText, false);
    }

    public static void main(String args[]) {
        String test="ABCD12345abcd";
        try {
            AESCipher aesCipher=AESCipher.getInstance();
            String encryptedVal = aesCipher.encryptToText(test);
            System.out.println(encryptedVal);

            String decryptedVal = aesCipher.decryptFromText(encryptedVal);
            System.out.println(decryptedVal);

            encryptedVal = aesCipher.encryptToText(test);
            System.out.println(encryptedVal);

            decryptedVal = aesCipher.decryptFromText(encryptedVal);
            System.out.println(decryptedVal);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}