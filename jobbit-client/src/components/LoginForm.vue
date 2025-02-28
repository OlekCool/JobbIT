<template>
  <div v-if="show">
    <h2>Авторизація</h2>
    <form @submit.prevent="login">
      <input type="text" v-model="username" placeholder="Логін" required />
      <input type="password" v-model="password" placeholder="Пароль" required />
      <button type="submit">Увійти</button>
      <button @click="$emit('close')">Закрити</button>
    </form>
  </div>
</template>

<script>
import AuthService from "@/services/AuthService";

export default {
  data() {
    return {
      username: "",
      password: "",
      show: true,
    };
  },
  methods: {
    async login() {
      try {
        await AuthService.login(this.username, this.password);
        this.$emit("close");
      } catch (error) {
        console.error("Login failed", error);
      }
    },
  },
};
</script>
