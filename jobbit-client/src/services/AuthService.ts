import axios from "axios";

const API_URL = "http://localhost:8080/api/auth/";

/**
 * AuthService для обробки запитів аутентифікації.
 */
class AuthService {

  /**
   * Реєстрація нового користувача.
   * @param {string} email - Адреса електронної пошти користувача.
   * @param {string} password - Пароль користувача.
   * @param {string} role - Роль користувача (наприклад, 'admin', 'candidate', 'recruiter').
   * @returns {Promise} - Проміс, що містить відповідь від сервера.
   */
  async register(email, password, role) {
    return axios.post(
      API_URL + "register",
      {
        email,
        password,
        role,
      },
      { withCredentials: true }
    );
  }

  /**
   * Вхід користувача.
   * @param {string} email - Адреса електронної пошти користувача.
   * @param {string} password - Пароль користувача.
   * @returns {Promise} - Проміс, що містить відповідь від сервера.
   */
  async login(email, password) {
    return axios.post(
      API_URL + "login",
      {
        email,
        password,
      },
      { withCredentials: true }
    );
  }

  /**
   * Надсилання запиту для перевірки електронної пошти для відновлення пароля.
   * @param {string} email - Адреса електронної пошти користувача.
   * @returns {Promise} - Проміс, що містить відповідь від сервера.
   */
  async sendEmail(email) {
    return axios.post(`${API_URL}forgotpassword/verifyMail/${email}`);
  }

  /**
   * Надсилання OTP-коду на електронну пошту.
   * @param {string} otp - Код OTP для відновлення пароля.
   * @param {string} email - Адреса електронної пошти користувача.
   * @returns {Promise} - Проміс, що містить відповідь від сервера.
   */
  async sendOtpCode(otp, email) {
    return axios.post(`${API_URL}sendotp/${otp}/${email}`);
  }

  /**
   * Зміна пароля користувача.
   * @param {string} email - Адреса електронної пошти користувача.
   * @param {string} password - Новий пароль користувача.
   * @param {string} repeatPassword - Повтор новий пароль користувача для підтвердження.
   * @returns {Promise} - Проміс, що містить відповідь від сервера.
   */
  async changePassword(email, password, repeatPassword) {
    return axios.post(`${API_URL}newpassword/${email}`, {
      password,
      repeatPassword,
    });
  }
}

export default new AuthService();
