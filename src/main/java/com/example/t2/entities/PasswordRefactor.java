package com.example.t2.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;

@Getter
@Setter
@NoArgsConstructor
public class PasswordRefactor {


    public SecretKeySpec createPassword(String key) {
        try {
            key=format2(key);
            byte[] chain=key.getBytes("UTF-8");
            MessageDigest messageDigest= MessageDigest.getInstance("SHA-1");
            chain=messageDigest.digest(chain);
            chain= Arrays.copyOf(chain, 16);
            SecretKeySpec secretKeySpec=new SecretKeySpec(chain, "DESede");
            return secretKeySpec;
        }
        catch(Exception e){
            return null;
        }
    }

    public String encryptPassword(String pw){
        try{
            SecretKeySpec secretKeySpec=createPassword(pw);
            Cipher cipher=Cipher.getInstance("DESede/CBC/NoPadding");
            IvParameterSpec iv = new IvParameterSpec(new byte[8]);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, iv);
            byte[] chain=pw.getBytes("UTF-8");
            byte[] encryptedChain=cipher.doFinal(chain);
            String encryptedChainBase64= Base64.getEncoder().encodeToString(encryptedChain);

            System.out.println(encryptedChainBase64);
            return encryptedChainBase64;
        }
        catch(Exception e){
            return "";
        }
    }

    public String decryptPassword(String pwd){
        try{
            SecretKeySpec secretKeySpec=createPassword(pwd);
            Cipher cipher=Cipher.getInstance("DESede/CBC/NoPadding");
            IvParameterSpec iv = new IvParameterSpec(new byte[8]);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, iv);
            byte[] chain=Base64.getDecoder().decode(pwd);
            byte[] decryptedPassword=cipher.doFinal(chain);
            String decryptedPasswordBase64=new String(decryptedPassword, "UTF-8");
            System.out.println(decryptedPasswordBase64);
            return decryptedPasswordBase64;
        }
        catch(Exception e){
            return "";
        }
    }

    public static String passwordFormat(String pw) {
        int longitud = pw.length();
        if (longitud < 24) {
            return String.format("%s%0" + (24 - longitud) + "d", pw, 0);
        } else {
            return pw;
        }
    }

    public static String format2(String pw) {
        int length = pw.length();
        int paddingLength = 24 - length;
        StringBuilder sb = new StringBuilder(pw);
        for (int i = 0; i < paddingLength; i++) {
            sb.append('\0');
        }
        return sb.toString();
    }




}
