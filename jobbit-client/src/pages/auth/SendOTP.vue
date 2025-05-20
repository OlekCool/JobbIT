<template>
  <div class="login-container">
    <form @submit.prevent="handleSubmit" class="login-box" aria-labelledby="otp-title-heading">
      <h2 class="login-title" id="otp-title-heading">Введіть 6-значний код, який прийшов на {{ email }}</h2>

      <div class="form-group">
        <label for="otpCode">Код:</label>
        <input type="text" id="otpCode" v-model="otpCode" maxlength="6" aria-required="true"
               aria-describedby="error-otp-message" aria-label="Введіть 6-значний код підтвердження" />
      </div>

      <p class="error" role="alert" aria-live="assertive" id="error-otp-message"> {{ errorOtp }}</p>

      <div class="buttons">
        <button to="/auth/newpassword" class="check-button" type="submit">Перевірити</button>
        <router-link to="/auth/forgotpassword" class="back-button" type="button" role="button">Назад</router-link>
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
  padding: 6vw 6vh;
  border: 0.3vh solid #ccc;
  border-radius: 4vh;
  width: 40vw;
}

.login-title {
  text-align: center;
  margin: 6vh auto;
  max-width: 80%;
  font-size: 1vw;
}

.form-group {
  display: flex;
  align-items: center;
  max-width: 80%;
  margin: 0 auto;
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
  margin-top: 4vh;
  display: flex;
  justify-content: space-around;
  text-decoration: none;
}

.check-button {
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

.check-button:hover {
  background-color: #3c963f;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

.back-button {
  margin-top: 2vw;
  background-color: #fff;
  border: none;
  cursor: pointer;
  text-decoration: none;
  color: #000;
  text-align: center;
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
