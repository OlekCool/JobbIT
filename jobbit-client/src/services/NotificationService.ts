import axios from "axios";

const API_NOTIF = "http://localhost:8080/api/notifications";

/**
 * Сервіс для роботи зі сповіщеннями для кандидата щодо вакансій
 */
class NotificationService {

    /**
     * Видаляє сповіщення за його ID.
     * @param notId Ідентифікатор сповіщення для видалення.
     * @param token Токен авторизації.
     * @returns {Promise<void>} Проміс без даних у разі успішного видалення.
     * @throws {Error} Якщо виникає помилка під час запиту.
     */
    async deleteNotification(notId, token) {
        try {
            await axios.post(`${API_NOTIF}/${notId}`, {}, {
                headers: token ? { Authorization: `Bearer ${token}` } : {}
            });
        } catch (error) {
            console.error('Помилка при видаленні сповіщення:', error.response?.data || error.message);
            throw error;
        }
    }

    /**
     * Видаляє всі сповіщення для конкретного кандидата.
     * @param candidateId Ідентифікатор кандидата.
     * @param token Токен авторизації.
     * @returns {Promise<void>} Проміс без даних у разі успішного видалення.
     * @throws {Error} Якщо виникає помилка під час запиту.
     */
    async deleteAllNotificationsForCandidate(candidateId, token) {
        try {
            await axios.post(`${API_NOTIF}/candidate/${candidateId}/all`, {}, {
                headers: token ? { Authorization: `Bearer ${token}` } : {}
            });
        } catch (error) {
            console.error('Помилка при видаленні всіх сповіщень кандидата:', error.response?.data || error.message);
            throw error;
        }
    }
}

export default new NotificationService();
