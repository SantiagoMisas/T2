package com.example.t2.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;

@Getter
@Setter
@NoArgsConstructor
public class PasswordYV {

    String key = "Numero Tarjeta De LM";

        //Generar clave para encriptacion
    public SecretKeySpec createPassword(String key) {
        try {
            byte[] chain=key.getBytes("UTF-8");
            MessageDigest messageDigest= MessageDigest.getInstance("SHA-1");
            chain=messageDigest.digest(chain);
            chain= Arrays.copyOf(chain, 24);
            SecretKeySpec secretKeySpec=new SecretKeySpec(chain, "TripleDES");
            return secretKeySpec;
        }
     catch(Exception e){
            return null;
        }}
        //Encriptar contraseña
        public String encryptPassword(String pw){
            try{
                SecretKeySpec secretKeySpec=createPassword(key);
                Cipher cipher=Cipher.getInstance("AES");
                cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
                byte[] chain=pw.getBytes("UTF-8");
                byte[] encryptedChain=cipher.doFinal(chain);
                String encryptedChainBase64= Base64.getEncoder().encodeToString(encryptedChain);
                return encryptedChainBase64;
            }
            catch(Exception e){
               return "";
            }}
        //Desencriptar Contraseña
        public String decryptPassword(String pwd){
            try{
                SecretKeySpec secretKeySpec=createPassword(key);
                Cipher cipher=Cipher.getInstance("AES");
                cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
                byte[] chain=Base64.getDecoder().decode(pwd);
                byte[] decryptedPassword=cipher.doFinal(chain);
                String decryptedPasswordBase64=new String(decryptedPassword);
                    return decryptedPasswordBase64;
                }
            catch(Exception e){
                    return "";
                }}
}
