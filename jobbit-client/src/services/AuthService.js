import axios from "axios";

const API_URL = "http://localhost:8080/auth/";

class AuthService {
  register(email, password, role) {
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

  login(email, password) {
    return axios.post(
      API_URL + "login",
      {
        email,
        password,
      },
      { withCredentials: true }
    );
  }

  sendEmail(email) {
    return axios.post(API_URL + "sendemail", {
      email,
    });
  }

  sendOtpCode(email, otpCode) {
    return axios.post(API_URL + "sendotpcode", {
      email,
      otpCode,
    });
  }

  changePassword(email, password, repeatPassword) {
    return axios.put(API_URL + "changepassword", {
      email,
      password,
      repeatPassword,
    });
  }
}

export default new AuthService();
