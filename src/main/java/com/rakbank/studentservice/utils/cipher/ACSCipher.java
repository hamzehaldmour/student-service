package com.rakbank.studentservice.utils.cipher;

import com.atypon.literatum.customization.UrlUtil;
import com.atypon.literatum.util.crypt.DecodeBase64;
import com.atypon.literatum.util.crypt.EncodeBase64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.io.*;
import java.security.Key;
import java.security.Provider;
import java.security.Security;
import java.util.Base64;

/**
 * This is a helper class to encrypt and decrypt a string into another
 * string.
 * <br><br>
 *
 * The encryption and decryption algorithms use a single private key. The
 * default instance of this class will look for a file named 'Secret.ser'
 * in the root directory of the web server. If it is not found, it will
 * generate a new key and store it in that file. The has to remain the same
 * throughout the encryption and decryption processes in order to decrypt
 * a string that has been encrypted earlier. If web servers are run across
 * a cluster, the private key file has to be the same among all the web
 * servers in the cluster too to provide consistency.
 * <br><br>
 *
 * One thing to notice about this class is that it will truncate the
 * characters in a given string to the lower 8-bit before encryption. This
 * means that any internationalized characters could be lost.
 * <br><br>
 *
 * The encryption utilizes the 'DES' algorithm, which is supplied by the
 * Java Cryptography Extension (JCE) 1.2.1 packages. It uses the provider
 * (SunJCE) that came with JCE implemented by Sun Microsystem. Notice that
 * the libraries 'jce1_2_1.zip' & 'sunjce_provider.jar' from JCE 1.2.1 are
 * required to compile and run this class.
 * <br><br>
 *
 * This class also uses the Base64Encoder and Base64Decoder from Sun's jdk
 * to convert a byte streams into standard ascii format so that the encrypted
 * cookie can be stored in a browser.
 * <br><br>
 *
 * TODO: make the location and the name of the private key file configurable.
 */
public final class ACSCipher
{
    static private String providerName = null;

    static private ACSCipher defaultInstance;

    static
    {
        try {
            // TODO: should store the provider name in a properties file
            // and get the classname and instantiate the provider at runtime.

            // Add SunJCE to the list of providers
            Provider jce = Security.getProvider("SunJCE");
            providerName = jce.getName();  // set providerName to the name of SunJCE provider
            Security.addProvider(jce);
            defaultInstance = new ACSCipher();
        } catch(Exception e) {
            Log.fatal(Log.ACS, e);
        }
    }

    private Key desKey;

    /**
     * Construct a new instance with the default private key.
     * The default key is stored in a file named 'SecretKey.ser'
     * in the server root directory. However, if the system property
     * "acs.security.desKey" is found, it will try to load that key
     * file.
     */
    public ACSCipher()
    {
        String desKeyFile = System.getProperty("acs.security.desKey");
        if (desKeyFile == null)
        {
            String acsHome = WebAppContext.getInstance().getSiteRootDir();
            desKeyFile = acsHome + "/WEB-INF/certs/SecretKey.ser";
        }

        desKey = getKey(desKeyFile);
    }

    /**
     * Construct a new instance with a given key. The key
     * should stored as a serialized java object of the class
     * 'java.security.Key'. The filename is passed as a
     * parameter for the constructor to locate the key.
     *
     * @param keyFilename name of file containing the key
     */
    public ACSCipher(String keyFilename)
    {
        desKey = getKey(keyFilename);
    }


	/**
	 *	@return key that is currently initialized.
	 */
	public Key getDefaultKey()
	{
		return this.desKey;
	}
	
	
    /**
     * @param keyFilename file containing the key
     * @return the private key from the given file.
     * If the file is not found, generate a new key with the given filename and
     * return the Key object to the caller. The serialized object
     * of the new DES key is stored in the file.
     */
    static protected Key getKey(String keyFilename)
    {
        ObjectInputStream in;
        Key key = null;
        try
        {
            // try to read the private key from the file
            in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(keyFilename)));
            key = (Key) in.readObject();
            in.close();
        }
        catch (Exception e)
        {
            // key file is not found, generate a new key
            ObjectOutputStream out;
            try
            {
                KeyGenerator keyGen = KeyGenerator.getInstance("DES", providerName);
                key = keyGen.generateKey();

                out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(keyFilename)));
                out.writeObject(key);
                out.close();
            }
            catch (Exception e2)
            {
                Log.error(Log.LITERATUM, "ACSCipher.getKey(): Failed to write generated key to the file - \"" + keyFilename + "\"");
                e2.printStackTrace();
            }
        }
        return key;
    }

    /**
     * @return an instance of ACSCipher that utilizes the default key file.
     */
    static public ACSCipher getInstance()
    {
        // init in static constructor
        return defaultInstance;
    }

    /**
     * Encrypt a string into an array of bytes. The string can only
     * contain 8-bit ascii characters. All higher bits in the
     * characters of the string will be truncated.
     *
     * @param clearText the string to be encrypted
     * @return the encrypted byte stream
     */
    public byte[] encrypt(String clearText)
    {
        if (clearText == null)
            return null;

        try
        {
            Cipher desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding", providerName);
            desCipher.init(Cipher.ENCRYPT_MODE, desKey);

            return desCipher.doFinal(clearText.getBytes());
        }
        catch (Exception e)
        {
            Log.warn(Log.LITERATUM, "Error in encryption: " + e.toString());
        }
        return null;
    }

    /**
     * Encrypt a string into an array of bytes and return the ascii String
     * representing by applying the base64 encoding method on the
     * encrypted byte stream.
     *
     * @param clearText the string to be encrypted
     * @param urlFriendly if true, the encrypted value will be a url friendly
     * @return the encrypted string
     */
    public String encryptToText(String clearText, boolean urlFriendly)
    {
        if (clearText == null)
            return null;

        String secret = Base64.getEncoder().encodeToString(encrypt(clearText));

        Lot.ACS.debug(" old encode encryption " + EncodeBase64.encode(encrypt(clearText)).replaceAll("\\s", "") +
                " new encode encryption " + secret);

        return urlFriendly ? UrlUtil.urlEncode(secret) : secret;
    }

    public String encryptToText(String clearText) {
        return encryptToText(clearText, false);
    }

    public static String encodeToBase64URL(String clearText) {
        byte[] bytes = clearText.getBytes();
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    public static String decodeBase64URL(String base64URL) {
        byte[] decodedBytes = Base64.getUrlDecoder().decode(base64URL);
        return new String(decodedBytes);
    }

    /**
     * Decrypt an encrypted array of bytes into the original text.
     *
     * @param secretBytes the encrypted byte stream
     * @return the decrypted string
     */
    public String decrypt(byte[] secretBytes)
    {
        if (secretBytes == null)
            return null;

        try
        {
            Cipher desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding", providerName);
            desCipher.init(Cipher.DECRYPT_MODE, desKey);

            return new String(desCipher.doFinal(secretBytes));
        }
        catch (Exception e)
        {
            if (!(e instanceof BadPaddingException)) {
                Log.warn(Log.LITERATUM, "Error in decryption: " + e.toString(), e);
            }
        }
        return "";
    }

    /**
     * Decrypt a string that has been encoded with base64 encoder into
     * the original text.
     *
     * @param secretText a String that has been encoded with base64 encoder
     * @param urlFriendly if true, we know that we encrypt this value using urlFriendly mode
     * @return the decrypted string
     */
    public String decryptFromText(String secretText, boolean urlFriendly)
    {
        secretText = (secretText == null) ? "" : secretText;
        if(urlFriendly){
            secretText = UrlUtil.urlDecode(secretText);
        }
        else{
            while (secretText.length() % 4 != 0){
                secretText += "=";
            }
        }
        String decrypted = decrypt(Base64.getDecoder().decode(secretText));

        try {
            Lot.ACS.debug("old decoder decryption " +
                    decrypt(DecodeBase64.decodeBuffer(secretText))  +
                    " new decoder decryption " + decrypted);
        } catch (UnsupportedEncodingException e) {
            Lot.ACS.debug("Error in decryption using old decoder: " + e.toString(), e);
        }

        return decrypted;
    }

    public String decryptFromText(String secretText) {
        return decryptFromText(secretText, false);
    }

}
