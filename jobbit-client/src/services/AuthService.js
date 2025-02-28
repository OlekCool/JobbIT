import axios from 'axios';

const API_URL = 'http://localhost:8080/auth/';

class AuthService {
  register(username, email, password, userType) {
    return axios.post(API_URL + 'register', {
      username,
      email,
      password,
      userType,
    });
  }

  login(username, password) {
    return axios.post(API_URL + 'login', {
      username,
      password,
    });
  }
}

export default new AuthService();