package application;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Утилитарный класс для шифрования и дешифрования данных с использованием алгоритма AES.
 * Использует заданный секретный ключ для шифрования и дешифрования.
 */
public class Decryptor {
    private static final String secretKey = "H7f8_kL*efUgI^d7";

    /**
     * Шифрует переданные данные с использованием алгоритма AES.
     *
     * @param data Данные для шифрования.
     * @return Зашифрованные данные в виде строки.
     * @throws NoSuchPaddingException     Если запрошенный padding не поддерживается.
     * @throws NoSuchAlgorithmException   Если указанный алгоритм шифрования не поддерживается.
     * @throws IllegalBlockSizeException   Если длина данных некорректна для алгоритма шифрования.
     * @throws BadPaddingException         Если возникает ошибка в процессе шифрования.
     * @throws InvalidKeyException        Если указанный ключ недействителен для шифрования.
     */
    public static String encrypt(String data) throws NoSuchPaddingException, NoSuchAlgorithmException,
            IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        SecretKey key = new SecretKeySpec(WorkingWithFiles.class.getSimpleName().getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);

        byte[] encryptedBytes = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    /**
     * Дешифрует переданные зашифрованные данные с использованием алгоритма AES.
     *
     * @param encryptedData Зашифрованные данные в виде строки.
     * @return Расшифрованные данные.
     * @throws NoSuchPaddingException     Если запрошенный padding не поддерживается.
     * @throws NoSuchAlgorithmException   Если указанный алгоритм дешифрования не поддерживается.
     * @throws IllegalBlockSizeException   Если длина данных некорректна для алгоритма дешифрования.
     * @throws BadPaddingException         Если возникает ошибка в процессе дешифрования.
     * @throws InvalidKeyException        Если указанный ключ недействителен для дешифрования.
     */
    public static String decrypt(String encryptedData) throws NoSuchPaddingException, NoSuchAlgorithmException,
            IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        SecretKey key = new SecretKeySpec(WorkingWithFiles.class.getSimpleName().getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);

        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        return new String(decryptedBytes);
    }
}
