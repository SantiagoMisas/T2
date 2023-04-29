package com.example.t2.entities;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.PBEKeySpec;
import java.security.spec.KeySpec;
import java.util.Arrays;

@Getter
@Setter
public class PasswordAI {
    private static final String UNICODE_FORMAT = "UTF8";
    public static final String DESEDE_ENCRYPTION_SCHEME = "DESede";
    private KeySpec ks;
    private SecretKeyFactory skf;
    private Cipher cipher;
    byte[] arrayBytes;
    private String password;
    private String passwordScheme;
    SecretKey key;

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

    public PasswordAI() throws Exception {
        password = "ContraseñaPorEnviar12";
        passwordScheme = DESEDE_ENCRYPTION_SCHEME;
        arrayBytes = password.getBytes(UNICODE_FORMAT);
        ks = new DESedeKeySpec(arrayBytes);
        skf = SecretKeyFactory.getInstance(passwordScheme);
        cipher = Cipher.getInstance(passwordScheme);
        key = skf.generateSecret(ks);
    }

    public String encrypt(String unencryptedString) {
        String encryptedString = null;
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] plainText = unencryptedString.getBytes(UNICODE_FORMAT);
            byte[] encryptedText = cipher.doFinal(plainText);
            encryptedString = new String(Base64.encodeBase64(encryptedText));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encryptedString;
    }

    public String decrypt(String encryptedString) {
        String decryptedText=null;
        try {
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] encryptedText = Base64.decodeBase64(encryptedString.getBytes());
            byte[] plainText = cipher.doFinal(encryptedText);
            decryptedText= new String(plainText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decryptedText;
    }

    public String encryptPassword(String password) {
        String encryptedPassword = encrypt(password);
        return encryptedPassword;
    }

    public String decryptPassword(String encryptedPassword) {
        String decryptedPassword = decrypt(encryptedPassword);
        return decryptedPassword;
    }

    public static byte[] deriveKey(String password) throws Exception {
        int iterations = 1000;
        int keySize = 24;
        byte[] salt = "someRandomSalt".getBytes();

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, keySize * 8);
        SecretKey key = factory.generateSecret(spec);

        return key.getEncoded();
    }

    public static String encryptCardNumber(String cardNumber, boolean useLifeMilesBin) throws Exception {
        if (useLifeMilesBin) {
            cardNumber = "6375230000000000" + cardNumber.substring(0, 16);
        }
        cardNumber = passwordFormat(cardNumber);
        PasswordAI passwordAI = new PasswordAI();
        String encryptedValue = passwordAI.encrypt(cardNumber);
        byte[] encryptedBytes = Base64.decodeBase64(encryptedValue.getBytes());
        byte[] pinBlock = new byte[8];
        for (int i = 0; i < 8; i++) {
            pinBlock[i] = (byte) (encryptedBytes[i] ^ 0x06);
        }
        byte[] encryptedWithoutPinBlock = Arrays.copyOfRange(encryptedBytes, 8, encryptedBytes.length);
        byte[] encryptedWithPinBlock = new byte[encryptedWithoutPinBlock.length + 8];
        System.arraycopy(pinBlock, 0, encryptedWithPinBlock, 0, 8);
        System.arraycopy(encryptedWithoutPinBlock, 0, encryptedWithPinBlock, 8, encryptedWithoutPinBlock.length);
        String encryptedCardNumber = new String(Base64.encodeBase64(encryptedWithPinBlock));
        return encryptedCardNumber;
    }
    public static String decryptCardNumber(String encryptedCardNumber, boolean useLifeMilesBin) throws Exception {
        byte[] encryptedWithPinBlock = Base64.decodeBase64(encryptedCardNumber.getBytes());
        byte[] pinBlock = Arrays.copyOfRange(encryptedWithPinBlock, 0, 8);
        byte[] encryptedWithoutPinBlock = Arrays.copyOfRange(encryptedWithPinBlock, 8, encryptedWithPinBlock.length);
        byte[] decryptedWithoutPinBlock = new byte[encryptedWithoutPinBlock.length];
        for (int i = 0; i < encryptedWithoutPinBlock.length; i++) {
            decryptedWithoutPinBlock[i] = (byte) (encryptedWithoutPinBlock[i] ^ 0x06);
        }
        byte[] decryptedBytes = new byte[decryptedWithoutPinBlock.length + 8];
        System.arraycopy(pinBlock, 0, decryptedBytes, 0, 8);
        System.arraycopy(decryptedWithoutPinBlock, 0, decryptedBytes, 8, decryptedWithoutPinBlock.length);
        String decryptedValue = new String(Base64.encodeBase64(decryptedBytes));
        if (useLifeMilesBin) {
            decryptedValue = decryptedValue.substring(16);
        }
        return decryptedValue;
    }}
