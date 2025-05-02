import axios from "axios";

const API_URL = "http://localhost:8080/api/vacancies";

/**
 * Сервіс для роботи з вакансіями
 */

class VacancyService {

    /**
     * Отримує список всіх вакансій з бекенду.
     * @returns {Promise<Array>} Проміс з масивом даних вакансій.
     * @throws {Error} Якщо виникає помилка під час запиту.
     */
    async getAllVacancies() {
        try {
            const response = await axios.get(`${API_URL}/all`);
            return response.data;
        } catch (error) {
            console.error("Помилка при отриманні вакансій:", error);
            throw error;
        }
    }
}

export default new VacancyService();
