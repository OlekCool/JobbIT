import axios from "axios";

const API_URL = "http://localhost:8080/api/candidate-dash/profile";

/**
 * Сервіс для роботи з профілем кандидата.
 */
class CandProfileService {
    constructor() {
        axios.interceptors.request.use(
            (config) => {
                const token = localStorage.getItem('authToken');
                if (token) {
                    config.headers.Authorization = `Bearer ${token}`;
                }
                return config;
            },
            (error) => {
                return Promise.reject(error);
            }
        );
    }

    /**
     * Оновлення даних профілю кандидата.
     * @param {Object} profileData - Дані профілю.
     * @returns {Promise}
     */
    async updateProfile(profileData) {
        return axios.post(`${API_URL}/update`, profileData);
    }

    /**
     * Отримання профілю кандидата за певним userId.
     * @param {number} userId - Ідентифікатор користувача.
     * @returns {Promise}
     */
    async getProfile(userId) {
        return axios.get(`${API_URL}/${userId}`);
    }
}

export default new CandProfileService();

