package oleksandr.jobbit_back.utils;

/**
 * Record {@code ChangePassword} використовується для передачі даних
 * при зміні пароля користувача. Містить новий пароль та його підтвердження для валідації.
 * Records у Java є компактним способом оголошення класів даних, які є незмінними (immutable)
 * та автоматично надають методи для доступу до полів, {@code equals()}, {@code hashCode()} та {@code toString()}.
 *
 * @param password Новий пароль користувача.
 * @param repeatPassword Повторне введення нового пароля для підтвердження.
 * @author Oleksandr Borovyk
 */
public record ChangePassword(String password, String repeatPassword) {
}
