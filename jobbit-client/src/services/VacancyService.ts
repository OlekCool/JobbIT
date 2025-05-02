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

    /**
     * Отримує список всіх вакансій рекрутера з бекенду, передаючи ID в тілі запиту.
     * @param recruiterId Ідентифікатор рекрутера.
     * @param token Токен авторизації.
     * @returns {Promise<Array>} Проміс з масивом даних вакансій рекрутера.
     * @throws {Error} Якщо виникає помилка під час запиту.
     */
    async getRecruiterVacancies(recruiterId, token) {
        try {
            const response = await axios.get(`${API_URL}/my-vacancies?recruiterId=${recruiterId}`, {
                headers: token ? { Authorization: `Bearer ${token}` } : {},
            });

            return response.data;
        } catch (error) {
            console.error('Помилка при отриманні вакансій рекрутера:', error.response?.data || error.message);
            throw error;
        }
    }
}

export default new VacancyService();
