import axios from "axios";

const API_URL_RECR = "http://localhost:8080/api/recruiter-dash/profile";
const API_URL_CAND = "http://localhost:8080/api/candidate-dash/profile";

/**
 * Сервіс для роботи з профілями (кандидата та рекрутера).
 */
class ProfileService {
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
    async updateProfileCandidate(profileData) {
        return axios.post(`${API_URL_CAND}/update`, profileData);
    }

    /**
     * Оновлення даних профілю рекрутера.
     * @param {Object} profileData - Дані профілю.
     * @returns {Promise}
     */
    async updateProfileRecruiter(profileData) {
        return axios.post(`${API_URL_RECR}/update`, profileData);
    }

    /**
     * Отримання профілю кандидата за певним userId.
     * @param {number} userId - Ідентифікатор користувача.
     * @returns {Promise}
     */
    async getProfileCandidate(userId) {
        return axios.get(`${API_URL_CAND}/${userId}`);
    }

    /**
     * Отримання профілю рекрутера за певним userId.
     * @param {number} userId - Ідентифікатор користувача.
     * @returns {Promise}
     */
    async getProfileRecruiter(userId) {
        return axios.get(`${API_URL_RECR}/${userId}`);
    }
}

export default new ProfileService();

