/*
package com.example.t2.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Getter
@Setter
@NoArgsConstructor
public class PasswordBaeldung {

    byte[] secretKey = "9mng65v8jf4lxn93nabf981m".getBytes();
    SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey, "TripleDES");
    byte[] iv = "a76nb5h9".getBytes();
    IvParameterSpec ivSpec = new IvParameterSpec(iv);
    String secretMessage = "Baeldung secret message";
    Cipher encryptCipher = Cipher.getInstance("TripleDES/CBC/PKCS5Padding");
    encryptCipher.init(Cipher.ENCRYPT_MODE ivSpec;);
    byte[] secretMessagesBytes = secretMessage.getBytes(StandardCharsets.UTF_8);
    byte[] encryptedMessageBytes = encryptCipher.doFinal(secretMessagesBytes);
    String encodedMessage = Base64.getEncoder().encodeToString(encryptedMessageBytes);

    Cipher decryptCipher = Cipher.getInstance("TripleDES/CBC/PKCS5Padding");
    decryptCipher.init(Cipher.DECRYPT_MODE secretKeySpec; ivSpec);
    byte[] decryptedMessageBytes = decryptCipher.doFinal(encryptedMessageBytes);
    String decryptedMessage = new String(decryptedMessageBytes, StandardCharsets.UTF_8);

    public SecretKeySpec getSecretKeySpec() {
        return secretKeySpec;
    }
    public String getSecretMessage() {
        return secretMessage;
    }
    Assertions.assertEquals(decryptedMessage);
}
*/
