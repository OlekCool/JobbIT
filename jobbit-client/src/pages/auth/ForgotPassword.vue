<template>
  <div class="login-container">
    <form @submit.prevent="handleSubmit" class="login-box" aria-labelledby="forgot-password-title">
      <h2 class="login-title" id="forgot-password-title">Введіть вашу email-адресу для відновлення паролю</h2>

      <div class="form-group">
        <label for="email">Ваш email, куди прийде код:</label>
        <input type="text" id="email" v-model="email" aria-required="true" />
      </div>

      <p class="error" role="alert" aria-live="assertive">{{ errorForgot }}</p>

      <div class="buttons">
        <button to="/auth/sendotp" class="sendemail-button" type="submit">Надіслати</button>
        <router-link to="/auth/login" class="back-button" type="button" role="button">Назад</router-link>
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
 * Сторінка для відновлення пароля через email.
 *
 * @component
 * @description Цей компонент дозволяє користувачу ввести свій email для отримання OTP (One-Time Password) коду
 * для відновлення пароля.
 */
const email = ref("");
const errorForgot = ref("");

const router = useRouter();

/**
 * Обробка події на надислання форми.
 *
 * @async
 * @function
 * @returns {Promise<void>} Повертає проміс, що виконується після запиту на надсилання OTP коду на email користувача.
 * Якщо успішно, то користувача переносить на сторінку введення OTP-коду.
 */
const handleSubmit = async () => {
  try {
    const response = await AuthService.sendEmail(email.value);

    if (response.status === HttpStatusCode.Ok) {
      localStorage.setItem("email", email.value);
      console.log("OTP code was sent to email", response.data); // eslint-disable-line no-console
      await router.push("/auth/sendotp");
    }
  } catch (error) {
    errorForgot.value = "Email повинен бути валідним та існувати в базі користувачів";
    console.error("Помилка надсилання OTP коду", error.response?.data || error.message); // eslint-disable-line no-console
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

input {
  flex: 1;
  padding: 1vh;
  border: 0.4vh solid #ccc;
  border-radius: 1vh;
}

.buttons {
  margin-top: 4vw;
  display: flex;
  justify-content: space-around;
}

.sendemail-button {
  width: 20%;
  padding: 10px;
  background-color: #4caf50;
  color: rgb(0, 0, 0);
  cursor: pointer;
  margin-bottom: 10px;
  border-radius: 0.8vh;
  border: 0.2vh solid rgb(48, 48, 48);
  transition: background-color 0.3s ease, box-shadow 0.3s ease;
  text-decoration: none;
  font-size: 1vw;
}

.sendemail-button:hover {
  background-color: #3c963f;
  box-shadow: 0 1vh 2vh rgba(0, 0, 0, 0.2);
}

.back-button {
  background-color: #fff;
  border: none;
  cursor: pointer;
  text-decoration: none;
  color: #000;
  align-content: center;
}

.back-button:hover {
  text-decoration: underline;
}

.error {
  color: red;
  margin-top: 8vh;
  font-size: 3vh;
}
</style>
