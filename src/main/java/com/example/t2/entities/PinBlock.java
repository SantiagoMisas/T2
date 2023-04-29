package com.example.t2.entities;

import lombok.Getter;
import lombok.Setter;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

@Getter
@Setter
public class PinBlock {

            // String a cifrar
            String stringACifrar = "1234567890123456";

            // Clave de 24 bytes (192 bits)
            byte[] clave = "0123456789ABCDEFFEDCBA9876543210".getBytes("ASCII");

            // Padding de la clave
            SecretKeySpec claveSpec = new SecretKeySpec(clave, "DESede");

            // Generar bloque de PIN
            byte[] pinBlock = generarPinBlock(stringACifrar, claveSpec);

            // Mostrar resultado en hexadecimal
            System.out.print("PIN block: " + bytesToHex(pinBlock);

    public PinBlock() throws Exception {
    }

    /**
         * Genera un bloque de PIN utilizando el protocolo ANSI X9.8
         * @param stringACifrar String que se desea cifrar
         * @param clave Clave para cifrado 3DES
         * @return Bloque de PIN cifrado
         * @throws Exception
         */
        public static byte[] generarPinBlock(String stringACifrar, SecretKeySpec clave) throws Exception {

            // Obtener datos necesarios para el protocolo ANSI X9.8
            byte[] bcdString = toBcd(stringACifrar);
            byte[] bcdF = toBcd("FFFFFFFFFFFFFF");
            byte[] bcd0 = toBcd("00000000000000");
            byte[] bcdD = toBcd("0000" + Integer.toString(bcdString.length));
            byte[] bcdFE = new byte[8];
            System.arraycopy(bcdF, 0, bcdFE, 0, 6);
            System.arraycopy(bcdD, 0, bcdFE, 6, 2);

            // Concatenar datos para formar el mensaje a cifrar
            byte[] mensaje = new byte[16];
            System.arraycopy(bcdFE, 0, mensaje, 0, 8);
            System.arraycopy(bcdString, 0, mensaje, 8, bcdString.length);
            System.arraycopy(bcd0, 0, mensaje, 8 + bcdString.length, 8 - bcdString.length);

            // Cifrar mensaje utilizando 3DES/ECB/NoPadding
            Cipher cipher = Cipher.getInstance("DESede/ECB/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, clave);
            byte[] cifrado = cipher.doFinal(mensaje);

            return cifrado;
        }

        /**
         * Convierte una cadena de caracteres en BCD
         * @param cadena Cadena de caracteres a convertir
         * @return Arreglo de bytes con la representación BCD de la cadena
         */
        public static byte[] toBcd(String cadena) {

            byte[] resultado = new byte[(cadena.length() + 1) / 2];
            int digito = 0;
            int posByte = 0;

            for (int i = 0; i < cadena.length(); i++) {
                digito = digito << 4;
                char c = cadena.charAt(i);
                if (c >= '0' && c <= '9') {
                    digito |= (c -                (c - 'A') + 10);
                } else if (c >= 'a' && c <= 'f') {
                    digito |= (c - 'a') + 10;
                }
                if ((i % 2) == 1) {
                    resultado[posByte++] = (byte) digito;
                    digito = 0;
                }
            }

            if ((cadena.length() % 2) == 1) {
                resultado[posByte++] = (byte) (digito << 4);
            }

            return resultado;
        }

    /**
     * Convierte un arreglo de bytes en una cadena de caracteres en hexadecimal
     * @param bytes Arreglo de bytes a convertir
     * @return Cadena de caracteres con la representación hexadecimal de los bytes
     */
    public static String bytesToHex(byte[] bytes) {

        final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];

        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i] & 0xFF;
            hexChars[i * 2] = HEX_ARRAY[v >>> 4];
            hexChars[i * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }

        return new String(hexChars);
    }
}



