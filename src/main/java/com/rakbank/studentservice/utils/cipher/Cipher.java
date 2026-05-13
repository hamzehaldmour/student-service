package com.rakbank.studentservice.utils.cipher;

import java.math.BigInteger;
import java.util.Random;

public class Cipher
{
    private static final int keylen = 4;
    private byte[] key;

    public void randomKey()
    {
      Random r = new Random();
      key = new byte[keylen];
      r.nextBytes(key);
    }

    public Cipher()
    {
      this("mdm");
    }

    public Cipher(String passwd)
    {
            key = new byte[keylen];
            for(int i=0; i < keylen; i++)
                    key[i] = 0;
            byte[] p = passwd.getBytes();
            for(int i=0; i < p.length; i++)
                    key[i % keylen] ^= p[i];
    }


    public byte[] scramble(byte[] in)
    {
      byte[] out = new byte[in.length];
      for(int i = 0; i < in.length; i++)
            out[i] = (byte) (in[i] ^ key[i % keylen]);
      return out;
    }

    public static String hexEncode(byte[] in)
    {
      String out = "";
      for (int i = 0; i < in.length; i++)
      out = out +
              ((in[i]>=0 && in[i]<16) ? "0" : "") +
              Integer.toHexString((in[i]<0) ? 256 + in[i] : in[i]);
      return out;
    }

    public static byte[] hexDecode(String in)
    {
      byte[] out = new byte[in.length()/2];
      for (int i = 0; i < in.length()/2; i+=1)
            out[i] = (byte) Integer.parseInt(in.substring(2*i,2*i+2), 16);
      return out;
    }


    public String encrypt(String s)
    {
      return encrypt(s.getBytes());
    }

    public String encrypt(byte[] in){
        return hexEncode(scramble(in));
    }
    public String decrypt(String s)
    {
      return new String(scramble(hexDecode(s)));
    }

    /**
     * Compresses a digest to a long of length bits.
     */
    public static long compressDigest(byte[] digest, int length)
    {
       return compressDigest(new BigInteger(1, digest), length); 
    }

    /**
     * Compresses a digest to a long of length bits.
     * Assumes that digest is an unsigned integer, so the following is wrong:
     * compressDigest(BigInteger.valueOf((long) digest), length). To
     * compress long values use compressDigest(long, int).
     */
     
    private static long compressDigest(BigInteger digest, int length)
    {
        BigInteger mask = BigInteger.valueOf(0);
        mask = mask.setBit(length);
        mask = mask.subtract(BigInteger.valueOf(1));
        BigInteger result = BigInteger.valueOf(0);
        while (digest.signum() > 0)
        {
            result = result.xor(digest.and(mask));
            digest = digest.shiftRight(length);
        }
        return result.longValue();
    }

    /**
     * Compresses a digest to a long of length bits.
     */

    public static long compressDigest(long digest, int length)
    {
        long mask = (1 << length) - 1;
        long result = 0;
        while (digest != 0)
        {
            result = result ^ (digest & mask);
            digest = digest >>> length;
        }

        return result;
    }

public static void main(String[] args)
{

        if (args.length == 0) {
            System.err.println("Usage: java Cipher [-d] 'text'");
            System.exit(1);
        }

	Cipher e = new Cipher();
        if (args.length == 2 && "-d".equals(args[0]))
        {
            String y = e.decrypt(args[1]);
            System.out.println("Decrypted text: " + y);
        }
        else
        {
            String y = e.encrypt(args[0]);
            System.out.println("Encrypted text: " + y);
        }
}

}
