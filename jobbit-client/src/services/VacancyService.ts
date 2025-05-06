import axios from "axios";

const API_COMMON = "http://localhost:8080/api/vacancies/all"
const API_RECR = "http://localhost:8080/api/vacancies/recr-vacancies";
const API_CAND = "http://localhost:8080/api/vacancies/cand-vacancies";

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
            const response = await axios.get(`${API_COMMON}`);
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
            const response = await axios.get(`${API_RECR}?recruiterId=${recruiterId}`, {
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
            const response = await axios.post(`${API_RECR}/${recruiterId}/add`, vacancyData, {
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

            const response = await axios.post(`${API_RECR}/editvacancy/${vacancyId}`,
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
            await axios.post(`${API_RECR}/${vacancyId}`, {}, {
                headers: token ? { Authorization: `Bearer ${token}` } : {},
            });
        } catch (error) {
            console.error('Помилка при видаленні вакансії:', error.response?.data || error.message);
            throw error;
        }
    }

    /**
     * Метод для перегляду збережених кандидатом вакансій
     * @param candidateId ідентифікатор кандидата
     * @param token токен авторизації
     */
    async watchSaved(candidateId, token) {
        try {
            const response = await axios.get(`${API_CAND}/saved?candidateId=${candidateId}`, {
                headers: token ? { Authorization: `Bearer ${token}` } : {},
            });
            return response.data;
        } catch (error) {
            console.error('Помилка при перегляді збережених вакансій:', error.responce?.data || error.message);
            throw error;
        }
    }

    /**
     * Метод для перегляду відгуків кандидатом на вакансії
     * @param candidateId ідентифікатор кандидата
     * @param token токен авторизації
     */
    async watchApplied(candidateId, token) {
        try {
            const response = await axios.get(`${API_CAND}/applied?candidateId=${candidateId}`, {
                headers: token ? { Authorization: `Bearer ${token}` } : {},
            });
            return response.data;
        } catch (error) {
            console.error('Помилка при перегляді відгукнутих вакансій:', error.responce?.data || error.message);
            throw error;
        }
    }

    /**
     * Отримання списку кандидатів, які подали заявку на вакансію
     * @param vacancyId ідентифікатор вакансії
     * @param token токен авторизації
     */
    async getAppliedCandidates(vacancyId, token) {
        try {
            const response = await axios.get(`${API_RECR}/${vacancyId}/applicants`, {
                headers: token ? { Authorization: `Bearer ${token}` } : {},
            });
            return response.data;
        } catch (error) {
            console.error('Помилка при перегляді кандидатів, що відгукнулися на вакансію:',
                error.responce?.data || error.message);
            throw error;
        }
    }

    /**
     * Додавання вакансії кандидатом у збережені
     * @param vacancyId ідентифікатор вакансії
     * @param candidateId ідентифікатор кандидата
     * @param token токен авторизації
     */
    async saveVacancy(vacancyId, candidateId, token) {
        try {
            return axios.post(`${API_CAND}/save/${vacancyId}?candidateId=${candidateId}`, {}, {
                headers: token ? { Authorization: `Bearer ${token}` } : {},
            });
        } catch (error) {
            console.error('Помилка при збереженні кандидатом вакансії:',
                error.responce?.data || error.message);
            throw error;
        }
    }

    /**
     * Подача кандидатури на вакансію
     * @param vacancyId ідентифікатор вакансії
     * @param candidateId ідентифікатор кандидата
     * @param token токен авторизації
     */
    async applyVacancy(vacancyId, candidateId, token) {
        try {
            return axios.post(`${API_CAND}/apply/${vacancyId}?candidateId=${candidateId}`, {}, {
                headers: token ? { Authorization: `Bearer ${token}` } : {},
            });
        } catch (error) {
            console.error('Помилка при поданні кандидатури на вакансію:',
                error.responce?.data || error.message);
            throw error;
        }
    }

    /**
     * Видалення вакансії кандидатом зі збережених
     * @param vacancyId ідентифікатор вакансії
     * @param candidateId ідентифікатор кандидата
     * @param token токен авторизації
     */
    async unSaveVacancy(vacancyId, candidateId, token) {
        try {
            return axios.post(`${API_CAND}/unsave/${vacancyId}?candidateId=${candidateId}`, {}, {
                headers: token ? { Authorization: `Bearer ${token}` } : {},
            });
        } catch (error) {
            console.error('Помилка при відміні збереження кандидатом вакансії:',
                error.responce?.data || error.message);
            throw error;
        }
    }

    /**
     * Відкликання відгуку кандидата на вакансію
     * @param vacancyId ідентифікатор вакансії
     * @param candidateId ідентифікатор кандидата
     * @param token токен авторизації
     */
    async unApplyVacancy(vacancyId, candidateId, token) {
        try {
            return axios.post(`${API_CAND}/unapply/${vacancyId}?candidateId=${candidateId}`, {}, {
                headers: token ? { Authorization: `Bearer ${token}` } : {},
            });
        } catch (error) {
            console.error('Помилка при відміні подання кандидатури на вакансію:',
                error.responce?.data || error.message);
            throw error;
        }
    }

    /**
     * Перевірка, чи вакансія збережена
     * @param vacancyId ідентифікатор вакансії
     * @param candidateId ідентифікатор кандидата
     * @param token токен авторизації
     */
    async isVacancySaved(vacancyId, candidateId, token) {
        try {
            const response = await axios.get(`${API_CAND}/is_saved/${vacancyId}?candidateId=${candidateId}`, {
                headers: token ? { Authorization: `Bearer ${token}` } : {},
            });
            return response.data;
        } catch (error) {
            console.error('Помилка при перевірці вакансії, чи збережена:',
                error.responce?.data || error.message);
            throw error;
        }
    }

    /**
     * Перевірка, чи кандидат відгукнувся на вакансію
     * @param vacancyId ідентифікатор вакансії
     * @param candidateId ідентифікатор кандидата
     * @param token токен авторизації
     */
    async isVacancyApplied(vacancyId, candidateId, token) {
        try {
            const response = await axios.get(`${API_CAND}/is_applied/${vacancyId}?candidateId=${candidateId}`, {
                headers: token ? { Authorization: `Bearer ${token}` } : {},
            });
            return response.data;
        } catch (error) {
            console.error('Помилка при відміні вакансії, чи подана кандидатура:',
                error.responce?.data || error.message);
            throw error;
        }
    }
}

export default new VacancyService();
