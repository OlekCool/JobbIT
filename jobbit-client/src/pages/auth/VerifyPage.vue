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

/**
 * Сторінка підтвердження акаунта користувача.
 *
 * @component
 * @description Цей компонент перевіряє верифікаційний токен і виводить результат (підтверджено чи помилка).
 */
const loading = ref(true);
const verified = ref(false);
const error = ref(false);
const errorMessage = ref("");
const route = useRoute();

const { token } = route.query;

/**
 * Отримання верифікаційного токена з URL параметрів і виконання запиту до сервера для підтвердження акаунта.
 *
 * @async
 * @function
 * @returns {Promise<void>} Повертає проміс, що виконується після обробки результату запиту.
 */
onMounted(async () => {
  if (!token) {
    error.value = true;
    errorMessage.value = "Токен не знайдено!";
    loading.value = false;
    return;
  }

  try {
    const response = await axios.get(`http://localhost:8080/api/auth/verify?token=${token}`);
    verified.value = true;
    loading.value = false;
    console.log(response); // eslint-disable-line no-console
  } catch (err) {
    error.value = true;
    errorMessage.value = "Помилка підтвердження акаунта.";
    loading.value = false;
  }
});
</script>
