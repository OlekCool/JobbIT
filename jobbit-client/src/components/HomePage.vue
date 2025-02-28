<template>
    <div>
        <button @click="showRegistrationForm">Реєстрація</button>
        <button @click="showLoginForm">Авторизація</button>
        <RegistrationForm v-if="showRegistration" @close="showRegistration = false" />
        <LoginForm v-if="showLogin" @close="showLogin = false" />
    </div>
</template>

<script>
import RegistrationForm from '@/components/RegistrationForm.vue';
import LoginForm from '@/components/LoginForm.vue';

export default {
  components: {
    RegistrationForm,
    LoginForm,
  },
  data() {
    return {
      showRegistration: false,
      showLogin: false,
    };
  },
  methods: {
    showRegistrationForm() {
      this.showRegistration = true;
    },
    showLoginForm() {
      this.showLogin = true;
    },
  },
};
</script>