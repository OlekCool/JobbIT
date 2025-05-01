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

    /**
     * Метод сервісу з завантаження резюме кандидата
     * @param formData дані, в яких міститься файл
     * @param userId ідентифікатор користувача
     * @param token jwt-токен
     * @param contentType тип контенту, який надсилається на бекенд
     */
    async uploadCvCandidate(formData, userId, token, contentType) {
        const headers = {
            ...token ? { Authorization: `Bearer ${token}` } : {},
            'Content-Type': contentType,
        };

        try {
            const response = await axios.post(`${API_URL_CAND}/${userId}/uploadcv`, formData, { headers }); // Окремий ендпоінт для завантаження CV
            return response.data;
        } catch (error) {
            console.error("Помилка при завантаженні CV", error);
            throw error;
        }
    }

    /**
     * Метод для завантаження на сервер фото кандидата
     * @param formData дані, в яких міститься файл
     * @param userId ідентифікатор користувача
     * @param token jwt-токен
     * @param contentType тип контенту, який надсилається на бекенд
     */
    async uploadProfilePhotoCandidate(formData, userId, token, contentType) {
        const headers = {
            ...token ? { Authorization: `Bearer ${token}` } : {},
            'Content-Type': contentType,
        };

        try {
            const response = await axios.post(`${API_URL_CAND}/${userId}/uploadphoto`, formData, { headers });
            return response.data;
        } catch (error) {
            console.error("Помилка при завантаженні фото профілю", error);
            throw error;
        }
    }

    /**
     * Метод для завантаження на сервер фото рекрутера
     * @param formData дані, в яких міститься файл
     * @param userId ідентифікатор користувача
     * @param token jwt-токен
     * @param contentType тип контенту, який надсилається на бекенд
     */
    async uploadProfilePhotoRecruiter(formData, userId, token, contentType) {
        const headers = {
            ...token ? { Authorization: `Bearer ${token}` } : {},
            'Content-Type': contentType,
        };

        try {
            const response = await axios.post(`${API_URL_RECR}/${userId}/uploadphoto`, formData, { headers });
            return response.data;
        } catch (error) {
            console.error("Помилка при завантаженні фото профілю", error);
            throw error;
        }
    }

    async getRecruiterProfilePhoto(userId, authToken) {
        try {
            const response = await fetch(`${API_URL_RECR}/${userId}/photo`, {
                headers: {
                    'Authorization': `Bearer ${authToken}`,
                },
            });
            return await response.text();
        } catch (error) {
            console.error("Failed to fetch recruiter profile photo:", error);
            throw error;
        }
    }

    async getCandidateProfilePhoto(userId, authToken) {
        try {
            const response = await fetch(`${API_URL_CAND}/${userId}/photo`, {
                headers: {
                    'Authorization': `Bearer ${authToken}`,
                },
            });
            return await response.text();
        } catch (error) {
            console.error("Failed to fetch candidate profile photo:", error);
            throw error;
        }
    }
}

export default new ProfileService();

