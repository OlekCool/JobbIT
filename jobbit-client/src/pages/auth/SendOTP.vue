<template>
  <div class="login-container">
    <form @submit.prevent="handleSubmit" class="login-box">
      <h2 class="login-title">Введіть 6-значний код, який прийшов на {{ email }}</h2>

      <div class="form-group">
        <label for="email">Код:</label>
        <input type="text" id="email" v-model="otpCode" />
      </div>

      <p class="error">{{ errorOtp }}</p>

      <div class="buttons">
        <button to="/auth/newpassword" class="check-button" type="submit">Перевірити</button>
        <router-link to="/auth/forgotpassword" class="back-button" type="button">Назад</router-link>
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
 * Сторінка для введення OTP (One-Time Password) коду для відновлення пароля.
 *
 * @component
 * @description Цей компонент дозволяє користувачу ввести OTP код, який надійшов на їх email
 * для підтвердження і відновлення пароля.
 */
const email = ref(localStorage.getItem("email"));
const otpCode = ref("");
const errorOtp = ref("");

const router = useRouter();

/**
 * Обробка події на надсилання форми з OTP кодом. OTP-код валідується на наявність тільки цифр
 *
 * @async
 * @function
 * @returns {Promise<void>} Повертає проміс, що виконується після перевірки OTP коду. Якщо успішно - то
 * користувача переносить на сторінку вводу нового пароля.
 */
const handleSubmit = async () => {
  if (!(/^\d+$/u).test(otpCode.value)) {
    errorOtp.value = "OTP повинен містити тільки цифри!";
    console.error("Помилка формату OTP:", errorOtp.value); // eslint-disable-line no-console
    return;
  }

  try {
    const otpInt = Number(otpCode.value);
    const response = await AuthService.sendOtpCode(otpInt, email.value);

    if (response.status === HttpStatusCode.Ok) {
      console.log("Successful checking OTP code", response.data); // eslint-disable-line no-console
      await router.push("/auth/newpassword");
    }
  } catch (error) {
    errorOtp.value = "Невірний OTP, або ж строк коду закінчився (10 хвилин)";
    console.error("Помилка обробки OTP коду", error.response?.data || error.message); // eslint-disable-line no-console
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

.check-button {
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

.check-button:hover {
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
