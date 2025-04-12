<template>
  <div class="login-container">
    <form @submit.prevent="handleSubmit" class="login-box">
      <h2 class="login-title">Введіть вашу email-адресу для відновлення паролю</h2>

      <div class="form-group">
        <label for="email">Ваш email, куди прийде код:</label>
        <input type="text" id="email" v-model="email" />
      </div>

      <p class="error">{{ errorForgot }}</p>

      <div class="buttons">
        <button to="/auth/sendotp" class="sendemail-button" type="submit">Надіслати</button>
        <router-link to="/auth/login" class="back-button" type="button">Назад</router-link>
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
  padding: 40px 20px;
  border: 1px solid #ccc;
  border-radius: 8px;
  width: 40%;
}

.login-title {
  text-align: center;
  margin-bottom: 40px;
  font-size: 20px;
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

input {
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

.sendemail-button {
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

.sendemail-button:hover {
  background-color: #3c963f;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
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
  font-size: 14px;
}
</style>
