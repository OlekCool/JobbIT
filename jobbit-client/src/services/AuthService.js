import axios from "axios";

const API_URL = "http://localhost:8080/auth/";

class AuthService {
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

  async sendEmail(email) {
    return axios.post(`${API_URL}forgotpassword/verifyMail/${email}`);
  }

  async sendOtpCode(otp, email) {
    return axios.post(`${API_URL}sendotp/${otp}/${email}`);
  }

  async changePassword(email, password, repeatPassword) {
    return axios.post(`${API_URL}newpassword/${email}`, {
      password,
      repeatPassword,
    });
  }
}

export default new AuthService();
