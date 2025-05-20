<template>
  <div class="login-container">
    <form @submit.prevent="handleSubmit" class="login-box" aria-labelledby="new-password-title">
      <h2 class="login-title" id="new-password-title">Введіть новий пароль для {{ email }}:</h2>

      <div class="your-password">
        <label for="password">Ваш пароль</label>
        <input type="password" id="password" v-model="newPassword" aria-required="true"
               aria-describedby="error-change-message" />
      </div>

      <div class="your-password repeat">
        <label for="password">Повтор вашого паролю</label>
        <input type="password" id="repeat-password" v-model="repeatPassword" aria-required="true"
               aria-describedby="error-change-message" />
      </div>

      <p class="error" role="alert" aria-live="assertive" id="error-change-message">{{ errorChange }}</p>

      <div class="buttons">
        <button class="change-button" type="submit">Змінити пароль</button>
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
 * Сторінка для зміни пароля користувача.
 *
 * @component
 * @description Цей компонент дозволяє користувачу ввести новий пароль і підтвердити його для завершення
 * процесу зміни пароля.
 */
const email = ref(localStorage.getItem("email"));
const newPassword = ref("");
const repeatPassword = ref("");
const errorChange = ref("");

const router = useRouter();

/**
 * Обробка події на надсилання форми з новим паролем. Валідація введеного пароля
 *
 * @async
 * @function
 * @returns {Promise<void>} Повертає проміс, що виконується після перевірки та зміни пароля. Якщо успішно - користувача
 * переносить на сторінку авторизації
 */
const handleSubmit = async () => {
  const passwordPattern = /(?=.*[0-9])(?=.*[!@#$%^&*])(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z!@#$%^&*]{8,}/gu;

  if (!passwordPattern.test(newPassword.value)) {
    errorChange.value = "Пароль має містити хоча б 8 символів, хоча б одну велику та малу літеру, цифру і спец.символ";
    console.error("Помилка валідації нового паролю", errorChange.value); // eslint-disable-line no-console
    return;
  }

  if (newPassword.value !== repeatPassword.value) {
    errorChange.value = "Пароль і повтор паролю не співпали";
    console.error("Помилка валідації нового паролю", errorChange.value); // eslint-disable-line no-console
    return;
  }

  // Запит на зміну пароля
  try {
    const response = await AuthService.changePassword(email.value, newPassword.value, repeatPassword.value);

    if (response.status === HttpStatusCode.Ok) {
      console.log("Successful change of password", response.data); // eslint-disable-line no-console
      await router.push("/auth/login");
    }
  } catch (error) {
    errorChange.value = "Помилка на стороні серверу";
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
  margin-bottom: 6vh;
  font-size: 1.2vw;
}

.your-password {
  display: flex;
  align-items: center;
  margin-bottom: 2vh;
}

.repeat {
  margin-bottom: 6vh;
}

label {
  width: 10vw;
  margin-right: 4vw;
  text-align: left;
  font-size: 1vw;
}

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

.change-button {
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

.change-button:hover {
  background-color: #3c963f;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

.error {
  color: red;
  margin-top: 8vh;
  font-size: 3vh;
}
</style>
