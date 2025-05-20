<template>
  <div class="registration-container">
    <form @submit.prevent="handleSubmit" class="registration-box" aria-labelledby="register-title-heading">
      <h2 class="registration-title" id="register-title-heading">РЕЄСТРАЦІЯ</h2>

      <div class="form-group">
        <label for="email">Ваш email</label>
        <input type="text" id="email" v-model="email" aria-required="true" />
      </div>

      <p v-if="emailError" class="error" role="alert" aria-live="assertive">{{ emailError }}</p>

      <div class="form-group">
        <label for="password">Ваш пароль</label>
        <input type="password" id="password" v-model="password" aria-required="true" />
      </div>

      <div class="form-group">
        <label for="confirmPassword">Повтор паролю</label>
        <input type="password" id="confirmPassword" v-model="confirmPassword" aria-required="true" />
      </div>

      <p v-if="passwordError" class="error" role="alert" aria-live="assertive">{{ passwordError }}</p>

      <div class="radio-group" role="radiogroup" aria-labelledby="role-selection-heading">
        <h3 id="role-selection-heading" class="visually-hidden">Оберіть вашу роль</h3>
        <label> <input type="radio" value="CANDIDATE" v-model="role" checked /> Кандидат </label>
        <label> <input type="radio" value="RECRUITER" v-model="role" /> Рекрутер </label>
      </div>

      <div class="buttons">
        <button class="register-button" type="submit">РЕЄСТРАЦІЯ</button>
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
 * Сторінка реєстрації користувача.
 *
 * @component
 * @description Компонент реєстрації користувача з валідацією введених даних (email та пароль).
 */
const email = ref("");
const password = ref("");
const confirmPassword = ref("");
const role = ref("CANDIDATE");

const emailError = ref("");
const passwordError = ref("");

const router = useRouter();

/**
 * Валідація введених даних користувача (email та пароль).
 * Перевіряє, чи збігаються паролі та відповідають введені значення шаблонам.
 *
 * @returns {boolean} Повертає true, якщо дані валідні, інакше false.
 */
const validateCredentials = () => {
  emailError.value = "";
  passwordError.value = "";

  if (password.value !== confirmPassword.value) {
    passwordError.value = "Паролі не співпадають!";
    return false;
  }

  const emailPattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/gu;
  const passwordPattern = /(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}/gu;

  if (!emailPattern.test(email.value)) {
    emailError.value = "Email має бути коректним";
    return false;
  } else if (!passwordPattern.test(password.value)) {
    passwordError.value =
      "Пароль має містити хоча б 8 символів, хоча б одну велику та малу літеру, цифру і спец.символ (!@#$%^&*)";
    return false;
  }

  passwordError.value = "";
  return true;
};

/**
 * Обробка події при натисканні на кнопку реєстрації.
 * Виконується валідація даних та надсилання запиту на реєстрацію.
 *
 * @async
 * @returns {Promise<void>} Повертає проміс, який виконується після обробки запиту. Якщо успішно, то користувача
 * переносить на сторінку авторизацію
 */
const handleSubmit = async () => {
  if (!validateCredentials()) {
    return;
  }

  try {
    const response = await AuthService.register(email.value, password.value, role.value);

    if (response.status === HttpStatusCode.Ok) {
      console.log("Success registration frontend", response.data); // eslint-disable-line no-console
      alert("На вашу пошту надіслано лінк для підтвердження акаунта.");
      await router.push("/auth/login");
    } else {
      console.error("Unexpected response:", response); // eslint-disable-line no-console
    }
  } catch (error) {
    emailError.value = "Такий email вже зареєстровано";
  }
};
</script>

<style scoped>
.registration-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 97vh;
  background-color: #f0f0f0;
}

.registration-box {
  background-color: white;
  padding: 6vw 6vh;
  border: 0.3vh solid #ccc;
  border-radius: 4vh;
  width: 40vw;
}

.registration-title {
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

.radio-group {
  display: flex;
  flex-direction: column;
  margin: 8vh auto;
  width: 30vw;
}

.radio-group label {
  margin: 0 auto;
}

.visually-hidden {
  position: absolute;
  width: 1px;
  height: 1px;
  margin: -1px;
  padding: 0;
  overflow: hidden;
  clip: rect(0, 0, 0, 0);
  border: 0;
}

.buttons {
  margin-top: 2vw;
  display: flex;
  justify-content: space-around;
}

.register-button,
.back-button {
  width: 20%;
  padding: 2vh;
  border: none;
  border-radius: 3px;
  background-color: #4caf50;
  color: rgb(0, 0, 0);
  cursor: pointer;
  margin-bottom: 10px;
  text-decoration: none;
}

.register-button {
  cursor: pointer;
  border-radius: 0.8vh;
  border: 0.2vh solid rgb(48, 48, 48);
  transition: background-color 0.3s ease, box-shadow 0.3s ease;
  text-decoration: none;
  font-size: 1vw;
}

.back-button {
  background-color: #fff;
  color: black;
}

.back-button:hover {
  text-decoration: underline;
}

.register-button:hover {
  background-color: #3c963f;
  box-shadow: 0 1vh 2vh rgba(0, 0, 0, 0.2);
}

.error {
  color: red;
  margin-top: 8vh;
  font-size: 3vh;
}
</style>
