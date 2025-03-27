<template>
  <div class="login-container">
    <form @submit.prevent="handleSubmit" class="login-box">
      <h2 class="login-title">Введіть новий пароль для {{ email }}:</h2>

      <div class="your-password">
        <label for="password">Ваш пароль</label>
        <input type="password" id="password" v-model="password" />
      </div>

      <div class="your-password repeat">
        <label for="password">Повтор вашого паролю</label>
        <input type="password" id="repeat-password" v-model="newPassword" />
      </div>

      <p class="error">{{ error }}</p>

      <div class="buttons">
        <router-link to="/auth/login" class="change-button" type="button">Змінити пароль</router-link>
      </div>
    </form>
  </div>
</template>

<script setup>
import { ref } from "vue";
// import { useRouter } from "vue-router";
// import AuthService from "@/services/AuthService";

const email = "someemail@xxxxx.xxx";
const password = ref("");
const newPassword = ref("");
const error = ref("");

const handleSubmit = async () => {
  // do frontend-logic for changing password for account
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
