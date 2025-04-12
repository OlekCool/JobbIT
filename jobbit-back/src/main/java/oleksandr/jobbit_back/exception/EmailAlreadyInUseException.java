package oleksandr.jobbit_back.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Виняток {@code EmailAlreadyInUseException} викидається, коли при реєстрації або оновленні
 * користувача виявляється, що вказана електронна пошта вже використовується іншим користувачем у системі.
 * Анотація {@code @ResponseStatus(HttpStatus.FORBIDDEN)} вказує, що при виникненні цього винятку
 * клієнту буде повернено HTTP-статус 403 (FORBIDDEN).
 *
 * @author Oleksandr Borovyk
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class EmailAlreadyInUseException extends RuntimeException {

    /**
     * Конструктор для створення винятку {@code EmailAlreadyInUseException} з детальним повідомленням.
     *
     * @param message Повідомлення, що описує причину виникнення винятку (наприклад, "Електронна пошта вже використовується").
     */
    public EmailAlreadyInUseException(String message) {
        super(message);
    }
}
