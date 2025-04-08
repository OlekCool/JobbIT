<template>
  <div>
    <h2>Підтвердження акаунта</h2>
    <p v-if="loading">Перевіряємо ваш акаунт...</p>
    <p v-if="error">{{ errorMessage }}</p>
    <p v-if="verified">Акаунт успішно підтверджено!</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useRoute } from "vue-router";
import axios from "axios";

const loading = ref(true);
const verified = ref(false);
const error = ref(false);
const errorMessage = ref("");
const route = useRoute();

// Отримання токена
const { token } = route.query;

onMounted(async () => {
  if (!token) {
    error.value = true;
    errorMessage.value = "Токен не знайдено!";
    loading.value = false;
    return;
  }

  // Get-запит на перевірку верифікаційного токена
  try {
    const response = await axios.get(`http://localhost:8080/api/auth/verify?token=${token}`);
    verified.value = true;
    loading.value = false;
    console.log(response);
  } catch (err) {
    error.value = true;
    errorMessage.value = "Помилка підтвердження акаунта.";
    loading.value = false;
  }
});
</script>
