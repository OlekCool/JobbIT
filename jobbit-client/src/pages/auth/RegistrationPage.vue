<template>
  <div class="registration-container">
    <form @submit.prevent="handleSubmit" class="registration-box">
      <h2 class="registration-title">РЕЄСТРАЦІЯ</h2>

      <div class="form-group">
        <label for="email">Ваш email</label>
        <input type="text" id="email" v-model="email" />
      </div>

      <p v-if="emailError" class="error">{{ emailError }}</p>

      <div class="form-group">
        <label for="password">Ваш пароль</label>
        <input type="password" id="password" v-model="password" />
      </div>

      <div class="form-group">
        <label for="confirmPassword">Повтор паролю</label>
        <input type="password" id="confirmPassword" v-model="confirmPassword" />
      </div>

      <p v-if="passwordError" class="error">{{ passwordError }}</p>

      <div class="radio-group">
        <label> <input type="radio" value="CANDIDATE" v-model="role" checked /> Кандидат </label>
        <label> <input type="radio" value="RECRUITER" v-model="role" /> Рекрутер </label>
      </div>

      <div class="buttons">
        <button class="register-button" type="submit">РЕЄСТРАЦІЯ</button>
        <router-link to="/" class="back-button" type="button">На головну</router-link>
      </div>
    </form>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";
import AuthService from "@/services/AuthService";

const email = ref("");
const password = ref("");
const confirmPassword = ref("");
const role = ref("CANDIDATE");

const emailError = ref("");
const passwordError = ref("");

const router = useRouter();

const validatePasswords = () => {
  emailError.value = "";
  passwordError.value = "";

  if (password.value !== confirmPassword.value) {
    passwordError.value = "Паролі не співпадають!";
    return false;
  }

  const emailPattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/g;
  const passwordPattern = /(?=.*[0-9])(?=.*[!@#$%^&*])(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z!@#$%^&*]{8,}/g;

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

const handleSubmit = async () => {
  if (!validatePasswords()) return;

  try {
    const response = await AuthService.register(email.value, password.value, role.value);

    if (response.status === 200) {
      console.log("Success registration frontend", response.data);
      router.push("/auth/login");
    } else {
      console.error("Unexpected response:", response);
    }
  } catch (error) {
    console.error("Помилка реєстрації", error.response?.data || error.message);
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
  padding: 40px 20px;
  border: 1px solid #ccc;
  border-radius: 8px;
  width: 40%;
}

.registration-title {
  text-align: center;
  margin-bottom: 40px;
}

.form-group {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
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

.radio-group {
  display: flex;
  justify-content: space-around;
}

.buttons {
  margin-top: 40px;
  display: flex;
  justify-content: space-around;
}

.register-button,
.back-button {
  width: 20%;
  padding: 10px;
  border: none;
  border-radius: 3px;
  background-color: #4caf50;
  color: rgb(0, 0, 0);
  cursor: pointer;
  margin-bottom: 10px;
  text-decoration: none;
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
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

.error {
  color: red;
  font-size: 14px;
}
</style>
