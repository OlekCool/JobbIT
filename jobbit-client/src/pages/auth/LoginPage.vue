<template>
  <div class="login-container">
    <form @submit.prevent="handleSubmit" class="login-box">
      <h2 class="login-title">АВТОРИЗАЦІЯ</h2>

      <div class="form-group">
        <label for="email">Ваш email</label>
        <input type="text" id="email" v-model="email" />
      </div>

      <div class="form-group form-group-last">
        <label for="password">Ваш пароль</label>
        <input type="password" id="password" v-model="password" />
      </div>

      <p v-if="loginError" class="error">{{ loginError }}</p>

      <button class="login-button" type="submit">АВТОРИЗАЦІЯ</button>

      <div class="buttons">
        <router-link to="/auth/forgotpassword" class="forgot-button" type="button">Забули пароль?</router-link>
        <router-link to="/" class="back-button" type="button">На головну</router-link>
      </div>
    </form>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";
import AuthService from "@/services/AuthService.ts";
import { HttpStatusCode } from "axios";

/**
 * Сторінка авторизації користувача.
 *
 * @component
 * @description Компонент авторизації користувача з валідацією введених даних (email та пароль).
 */
const email = ref("");
const password = ref("");
const loginError = ref("");
const router = useRouter();

/**
 * Обробка події при натисканні на кнопку авторизації.
 * Виконується валідація даних та надсилання запиту на реєстрацію.
 *
 * @async
 * @returns {Promise<void>} Повертає проміс, який виконується після обробки запиту. Якщо успішно, то користувача
 * переносить на сторінку кандидата/рекрутера в залежності вір ролі зареєстрованого користувача.
 */
const handleSubmit = async () => {
  try {
    const response = await AuthService.login(email.value, password.value);

    if (response.status === HttpStatusCode.Ok) {
      localStorage.setItem("authToken", response.data.token);
      localStorage.setItem("userRole", response.data.role);
      console.log("Логін успішний", response.data); // eslint-disable-line no-console

      if (response.data.role === "CANDIDATE") {
        await router.push("/candidate-dash");
      } else if (response.data.role === "RECRUITER") {
        await router.push("/recruiter-dash");
      }
    }
  } catch (error) {
    loginError.value = "Невірний email або пароль";
    console.error("Помилка авторизації", error.response?.data || error.message); // eslint-disable-line no-console
  }
};
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 97vh;
  background-color: #f0f0f0;
}

.login-box {
  background-color: white;
  padding: 40px 20px;
  border: 1px solid #ccc;
  border-radius: 8px;
  width: 40%;
}

.login-title {
  text-align: center;
  margin-bottom: 40px;
}

.form-group {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.form-group-last {
  margin-bottom: 40px;
}

label {
  width: 120px;
  margin-right: 20px;
  text-align: left;
}

input[type="text"],
input[type="password"] {
  flex: 1;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 3px;
}

.buttons {
  margin-top: 10px;
  display: flex;
  justify-content: space-around;
  text-decoration: none;
}

.login-button {
  margin: 25px;
  width: 20%;
  padding: 10px;
  border: none;
  border-radius: 3px;
  background-color: #4caf50;
  color: rgb(0, 0, 0);
  cursor: pointer;
  text-decoration: none;
}

.login-button:hover {
  background-color: #3c963f;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

.back-button,
.forgot-button {
  margin-top: 20px;
  background-color: #fff;
  border: none;
  cursor: pointer;
  text-decoration: none;
}

.forgot-button {
  color: #2937ff;
}

.back-button {
  color: #000;
}

.back-button:hover,
.forgot-button:hover {
  text-decoration: underline;
}

.error {
  color: red;
  font-size: 14px;
}
</style>
