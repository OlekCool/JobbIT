<template>
  <div class="login-container">
    <form @submit.prevent="handleSubmit" class="login-box" aria-labelledby="login-title-heading">
      <h2 class="login-title" id="login-title-heading">АВТОРИЗАЦІЯ</h2>

      <div class="form-group">
        <label for="email">Ваш email</label>
        <input type="text" id="email" v-model="email" aria-required="true" />
      </div>

      <div class="form-group form-group-last">
        <label for="password">Ваш пароль</label>
        <input type="password" id="password" v-model="password" aria-required="true" />
      </div>

      <p v-if="loginError" class="error" role="alert" aria-live="assertive">{{ loginError }}</p>

      <button class="login-button" type="submit">АВТОРИЗАЦІЯ</button>

      <div class="buttons">
        <router-link to="/auth/forgotpassword" class="forgot-button" type="button" role="button">Забули пароль?</router-link>
        <router-link to="/" class="back-button" type="button" role="button">На головну</router-link>
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
      console.log("Токен отримано:", response.data.token)
      localStorage.setItem("authToken", response.data.token);
      localStorage.setItem("userRole", response.data.role);
      localStorage.setItem("userProfile", JSON.stringify(response.data.profile));
      localStorage.setItem("userId", JSON.stringify(response.data.profile.id));
      console.log("Логін успішний", response.data); // eslint-disable-line no-console

      if (response.data.role === "CANDIDATE") {
        await router.push("/candidate-dash/search");
      } else if (response.data.role === "RECRUITER") {
        await router.push("/recruiter-dash/my-vacancies");
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
  padding: 6vw 6vh;
  border: 0.3vh solid #ccc;
  border-radius: 4vh;
  width: 40vw;
}

.login-title {
  text-align: center;
  margin-bottom: 6vh;
}

.form-group {
  display: flex;
  align-items: center;
  margin-bottom: 2vh;
}

label {
  width: 10vw;
  margin-right: 4vw;
  text-align: left;
}

input[type="text"],
input[type="password"] {
  flex: 1;
  padding: 1vh;
  border: 0.4vh solid #ccc;
  border-radius: 1vh;
}

.buttons {
  margin-top: 4vh;
  display: flex;
  justify-content: space-around;
  text-decoration: none;
}

.login-button {
  margin-top: 5vh;
  width: 10vw;
  padding: 2vh;
  background-color: #4caf50;
  color: rgb(0, 0, 0);
  cursor: pointer;
  border-radius: 0.8vh;
  border: 0.2vh solid rgb(48, 48, 48);
  transition: background-color 0.3s ease, box-shadow 0.3s ease;
  text-decoration: none;
  font-size: 1vw;
}

.login-button:hover {
  background-color: #3c963f;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

.back-button,
.forgot-button {
  margin-top: 2vw;
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
  margin-top: 8vh;
  font-size: 3vh;
}
</style>
