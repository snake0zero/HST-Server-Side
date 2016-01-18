package com.polycom.hst.wechat.encryption;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class AES {
	
	public static final String KEY_ENCRYPT = "admin_portal_key";
    public static final String CENTER_KEY="css_center_key_!";

    public static String decrypt(String encryptedValue) {
        return decrypt(encryptedValue,KEY_ENCRYPT);
    }
    public static String decryptForCenter(String encryptedValue) {
        return decrypt(encryptedValue,CENTER_KEY);
    }

    public static String decrypt(String encryptedValue,String enykey){
        try {
            String key = enykey;
            byte[] raw = key.getBytes("ASCII");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(key.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] encryptedBytes = Base64.decodeBase64(encryptedValue);
            byte[] originalBytes = cipher.doFinal(encryptedBytes);
            String originalString = new String(originalBytes, "UTF-8");
            return originalString;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }



    public static String encrypt(String oriValue){
        return encrypt(oriValue,KEY_ENCRYPT);

    }

    public static String encryptForCenter(String oriValue){
        return encrypt(oriValue,CENTER_KEY);

    }

    public static String encrypt(String oriValue,String enykey){
        String key = enykey;
        byte[] raw = key.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher;
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(key.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte[] encrypted = cipher.doFinal(oriValue.getBytes());
            return Base64.encodeBase64String(encrypted);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String args[]) throws Exception {
//		String encryptedValue = "2M7SMPBEdV+sZYU+OA8wIQ==";
//		String d = decrypt(encryptedValue);
//		System.out.println("decrypt:" + d);

//        String testEncript="test";
//        String e = encrypt(testEncript,AES.CENTER_KEY);
//        System.out.println(e);
//        String d=decrypt(e,AES.CENTER_KEY);
//        System.out.println(d);
//
//        String testEncriptC="test";
//        String ec = encryptForCenter(testEncript);
//        System.out.println(ec);
//        System.out.println(ec.length());
//        String dc=decryptForCenter(ec);
//        System.out.println(dc);
//
//
//        String testPwd="Polycom1231123111111111111111111";
//        String pwd = encryptForCenter(testPwd);
//        System.out.println(pwd);
//        System.out.println(pwd.length());
//        String dPwd=decryptForCenter(pwd);
//        System.out.println(dPwd);
//
//
//        String entrypt="TwvptugCbR9QCIL6Rqg3gg==";
//        System.out.println("#######"+entrypt.length());
//        String dE=decryptForCenter(entrypt);
//        System.out.println("########"+dE);
        String test="soak";
        String encrypt=AES.encryptForCenter(test);
        System.out.println(encrypt);
        
        System.out.println(decrypt("6aFuHPJhZY/pon22puZmhLTkUswVCrF+/HtDoHMDvJt1vfkYGwb1n6E6WW9faqQdUNo1V7aejqNxs6f2qiUnGZNgafrnfD7Jp0Zp7LvWgcb3Wkh5Xd0UoCPtXxdkKxEE","0123456789abcdef"));
      //aesencrypt  System.out.println(decrypt("U2FsdGVkX1/pDfbQ5HzKdr/oHX5DldBQrKaWJGUSz+M=","abcdefgabcdefgab"));
        System.out.println(aesencrypt("0123456789","0123456789abcdef"));
    }

    public static byte[] aesencrypt(String plainText, String encryptionKey) throws Exception {
    	String IV = "AAAAAAAAAAAAAAAA";
      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "SunJCE");
      SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
      cipher.init(Cipher.ENCRYPT_MODE, key,new IvParameterSpec(IV.getBytes("UTF-8")));
      return cipher.doFinal(plainText.getBytes("UTF-8"));
    }

    public static boolean notEncryptPwd(String password) {
        return (password.length()!=24&&password.length()!=44&&password.length()!=64);
    }
}
