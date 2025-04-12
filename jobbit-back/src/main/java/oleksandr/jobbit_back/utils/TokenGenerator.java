package oleksandr.jobbit_back.utils;

import java.util.UUID;

/**
 * Утилітний клас {@code TokenGenerator} надає методи для генерації різних типів токенів.
 * Наразі містить метод для генерації унікального випадкового токена у форматі UUID (Universally Unique Identifier).
 *
 * @author Oleksandr Borovyk
 */
public class TokenGenerator {

    /**
     * Генерує унікальний випадковий токен у форматі UUID (Universally Unique Identifier).
     * UUID є 128-бітним ідентифікатором, який має дуже низьку ймовірність колізії, що робить його
     * придатним для використання в якості унікальних ідентифікаторів, наприклад, для токенів верифікації.
     *
     * @return Згенерований унікальний токен у вигляді рядка.
     */
    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
}
