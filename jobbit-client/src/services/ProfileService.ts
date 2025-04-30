import axios from "axios";

const API_URL_RECR = "http://localhost:8080/api/recruiter-dash/profile";
const API_URL_CAND = "http://localhost:8080/api/candidate-dash/profile";

/**
 * Сервіс для роботи з профілями (кандидата та рекрутера).
 */
class ProfileService {

    /**
     * Оновлення даних профілю кандидата.
     * @param {Object} profileData - Дані профілю.
     * @param userId id користувача
     * @param token токен JWT
     * @returns {Promise}
     */
    async updateProfileCandidate(profileData, userId, token) {
        return axios.post(`${API_URL_CAND}/${userId}/update`, profileData, {
            headers: token ? { Authorization: `Bearer ${token}` } : {}
        });
    }

    /**
     * Оновлення даних профілю рекрутера.
     * @param {Object} profileData - Дані профілю.
     * @param userId id користувача
     * @param token токен JWT
     * @returns {Promise}
     */
    async updateProfileRecruiter(profileData, userId, token) {
        return axios.post(`${API_URL_RECR}/${userId}/update`, profileData, {
            headers: token ? { Authorization: `Bearer ${token}` } : {}
        });
    }

    /**
     * Отримання профілю кандидата за певним userId.
     * @param {number} userId - Ідентифікатор користувача.
     * @param token токен JWT
     * @returns {Promise}
     */
    async getProfileCandidate(userId, token) {
        return axios.get(`${API_URL_CAND}/${userId}`, {
            headers: token ? { Authorization: `Bearer ${token}` } : {}
        });
    }

    /**
     * Отримання профілю рекрутера за певним userId.
     * @param {number} userId - Ідентифікатор користувача.
     * @param token токен JWT
     * @returns {Promise}
     */
    async getProfileRecruiter(userId, token) {
        return axios.get(`${API_URL_RECR}/${userId}`, {
            headers: token ? { Authorization: `Bearer ${token}` } : {}
        });
    }
}

export default new ProfileService();

