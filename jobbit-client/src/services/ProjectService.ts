import axios from "axios";

const API_URL_PROJECT = "http://localhost:8080/api/candidate-dash/profile";

/**
 * Сервіс для роботи з проєктами кандидата
 */
class ProjectService {

    /**
     * Отримання усіх проєктів кандидата
     * @param userId ідентифікатор кандидата
     * @param token авторизаційний токен
     */
    async getProjectsByCandidateId(userId, token) {
        return axios.get(`${API_URL_PROJECT}/${userId}`, {
            headers: token ? { Authorization: `Bearer ${token}` } : {},
        });
    }

    /**
     * Отримання конкретного проєкту кандидата за ID
     * @param id ідентифікатор проєкту
     * @param userId ідентифікатор кандидата
     * @param token авторизаційний токен
     */
    async getProjectById(id, userId, token) {
        return axios.get(`${API_URL_PROJECT}/${userId}/projects/${id}`, {
            headers: token ? { Authorization: `Bearer ${token}` } : {},
        });
    }

    /**
     * Додавання нового проєкту кандидата
     * @param userId ідентифікатор кандидата
     * @param projectData об'єкт з даними проєкту (projName, projDescription, projGithubLink, photo)
     * @param token авторизаційний токен
     */
    async addProject(userId, projectData, token) {
        const formData = new FormData();
        formData.append('projName', projectData.name);
        formData.append('projDescription', projectData.description);
        formData.append('projGithubLink', projectData.link);
        if (projectData.photo) {
            formData.append('photo', projectData.photo);
        }

        return axios.post(`${API_URL_PROJECT}/${userId}/addproject`, formData, {
            headers: {
                'Content-Type': 'multipart/form-data',
                ...token ? { Authorization: `Bearer ${token}` } : {},
            },
        });
    }

    /**
     * Оновлення існуючого проєкту кандидата
     * @param id ідентифікатор проєкту
     * @param userId ідентифікатор кандидата
     * @param projectData об'єкт з даними проєкту (projName, projDescription, projGithubLink, photo)
     * @param token авторизаційний токен
     */
    async updateProject(id, userId, projectData, token) {
        const headers = {
            ...token ? { Authorization: `Bearer ${token}` } : {},
        };

        if (projectData.photo instanceof File) {
            const formData = new FormData();
            formData.append('projName', projectData.projName);
            formData.append('projDescription', projectData.projDescription);
            formData.append('projGithubLink', projectData.projGithubLink);
            formData.append('photo', projectData.photo);
            headers['Content-Type'] = 'multipart/form-data';

            return axios.post(`${API_URL_PROJECT}/${userId}/editproject/${id}`, formData, { headers });
        } else {
            const jsonData = {
                projName: projectData.projName,
                projDescription: projectData.projDescription,
                projGithubLink: projectData.projGithubLink,
            };
            headers['Content-Type'] = 'application/json';

            return axios.post(`${API_URL_PROJECT}/${userId}/editproject/${id}`, jsonData, { headers });
        }
    }

    /**
     * Видалення проєкту кандидата за ID
     * @param id ідентифікатор проєкту
     * @param userId ідентифікатор кандидата
     * @param token авторизаційний токен
     */
    async deleteProject(id, userId, token) {
        return axios.post(`${API_URL_PROJECT}/${userId}/projects/${id}`, null, { // Запит POST з пустим тілом
            headers: token ? { Authorization: `Bearer ${token}` } : {},
        });
    }
}

export default new ProjectService();
