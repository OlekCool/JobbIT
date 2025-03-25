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
}

export default new AuthService();
