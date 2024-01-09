package application;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static application.Decryptor.decrypt;

/**
 * Класс, представляющий пользователя.
 * Содержит методы для получения логина и пароля пользователя, используя дешифрование.
 * Использует класс Decryptor для дешифрования зашифрованных логина и пароля.
 */
public final class User {
    private String login = "6NLYHzpd3yToU6288d3TrqxuysNMfgCXysas4b+vRrQ=";
    private String password = "XkKGUwAPok8EWrdzJf7gwEmXddsIq9PIA8AUv1xhy+A=";

    /**
     * Получает дешифрованный логин пользователя.
     *
     * @return Дешифрованный логин пользователя.
     * @throws NoSuchPaddingException     Если запрошенный padding не поддерживается.
     * @throws NoSuchAlgorithmException   Если указанный алгоритм дешифрования не поддерживается.
     * @throws IllegalBlockSizeException   Если длина данных некорректна для алгоритма дешифрования.
     * @throws BadPaddingException         Если возникает ошибка в процессе дешифрования.
     * @throws InvalidKeyException        Если указанный ключ недействителен для дешифрования.
     */
    public String getLogin() throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException,
            BadPaddingException, InvalidKeyException {
        return decrypt(login);
    }

    /**
     * Получает дешифрованный пароль пользователя.
     *
     * @return Дешифрованный пароль пользователя.
     * @throws NoSuchPaddingException     Если запрошенный padding не поддерживается.
     * @throws NoSuchAlgorithmException   Если указанный алгоритм дешифрования не поддерживается.
     * @throws IllegalBlockSizeException   Если длина данных некорректна для алгоритма дешифрования.
     * @throws BadPaddingException         Если возникает ошибка в процессе дешифрования.
     * @throws InvalidKeyException        Если указанный ключ недействителен для дешифрования.
     */
    public String getPassword() throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException,
            BadPaddingException, InvalidKeyException {
        return decrypt(password);
    }
}
