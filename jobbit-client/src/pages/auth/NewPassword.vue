<template>
  <div class="login-container">
    <form @submit.prevent="handleSubmit" class="login-box">
      <h2 class="login-title">Введіть новий пароль для {{ email }}:</h2>

      <div class="your-password">
        <label for="password">Ваш пароль</label>
        <input type="password" id="password" v-model="newPassword" />
      </div>

      <div class="your-password repeat">
        <label for="password">Повтор вашого паролю</label>
        <input type="password" id="repeat-password" v-model="repeatPassword" />
      </div>

      <p class="error">{{ errorChange }}</p>

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

const email = ref(localStorage.getItem("email"));
const newPassword = ref("");
const repeatPassword = ref("");
const errorChange = ref("");

const router = useRouter();

// Обробка кнопки для зміни пароля
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

.your-password {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.repeat {
  margin-bottom: 40px;
}

label {
  width: 120px;
  margin-right: 20px;
  text-align: left;
}

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

.change-button {
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

.change-button:hover {
  background-color: #3c963f;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

.error {
  color: red;
  font-size: 14px;
}
</style>
