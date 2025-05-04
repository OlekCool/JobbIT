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

    /**
     * Додає нову вакансію на бекенд.
     * @param recruiterId Ідентифікатор рекрутера, який додає вакансію.
     * @param vacancyData Дані нової вакансії.
     * @param token Токен авторизації.
     * @returns {Promise<Object>} Проміс з даними створеної вакансії.
     * @throws {Error} Якщо виникає помилка під час запиту.
     */
    async addNewVacancy(recruiterId, vacancyData, token) {
        try {
            const response = await axios.post(`${API_URL}/my-vacancies/${recruiterId}/add`, vacancyData, {
                headers: token ? { Authorization: `Bearer ${token}` } : {},
            });
            return response.data;
        } catch (error) {
            console.error('Помилка при додаванні нової вакансії:', error.response?.data || error.message);
            throw error;
        }
    }

    /**
     * Редагує існуючу вакансію на бекенді.
     * @param vacancyId Ідентифікатор вакансії, яку потрібно редагувати.
     * @param editedVacancy Дані оновленої вакансії.
     * @param token Токен авторизації.
     * @returns {Promise<Object>} Проміс з даними оновленої вакансії.
     * @throws {Error} Якщо виникає помилка під час запиту.
     */
    async updateVacancy(vacancyId, editedVacancy, token) {
        try {
            const headers = {
                ...token ? { Authorization: `Bearer ${token}` } : {},
            };

            const response = await axios.post(`${API_URL}/my-vacancies/editvacancy/${vacancyId}`,
                editedVacancy, { headers });
            return response.data;
        } catch (error) {
            console.error('Помилка при редагуванні вакансії:', error.response?.data || error.message);
            throw error;
        }
    }

    /**
     * Видаляє вакансію.
     * @param vacancyId Ідентифікатор вакансії, яку потрібно видалити.
     * @param token Токен авторизації.
     * @returns {Promise<void>} Проміс без даних у разі успішного видалення.
     * @throws {Error} Якщо виникає помилка під час запиту.
     */
    async deleteVacancy(vacancyId, token) {
        try {
            await axios.post(`${API_URL}/my-vacancies/${vacancyId}`, {}, {
                headers: token ? { Authorization: `Bearer ${token}` } : {},
            });
        } catch (error) {
            console.error('Помилка при видаленні вакансії:', error.response?.data || error.message);
            throw error;
        }
    }
}

export default new VacancyService();
