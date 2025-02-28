<template>
  <div v-if="show">
    <h2>Реєстрація</h2>
    <form @submit.prevent="register">
      <input type="text" v-model="username" placeholder="Логін" required />
      <input type="email" v-model="email" placeholder="Email" required />
      <input type="password" v-model="password" placeholder="Пароль" required />
      <select v-model="userType">
        <option value="RECRUITER">Рекрутер</option>
        <option value="CANDIDATE">Кандидат</option>
      </select>
      <button type="submit">Зареєструватися</button>
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
      email: "",
      password: "",
      userType: "",
      show: true,
    };
  },
  methods: {
    async register() {
      try {
        await AuthService.register(this.username, this.email, this.password, this.userType);
        this.$emit("close");
      } catch (error) {
        console.error("Registration failed", error);
      }
    },
  },
};
</script>
