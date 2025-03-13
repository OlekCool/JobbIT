<template>
  <div class="registration-container">
    <form @submit.prevent="handleSubmit" class="registration-box">
      <h2 class="registration-title">РЕЄСТРАЦІЯ</h2>

      <div class="form-group">
        <label for="login">Ваш логін</label>
        <input type="text" id="login" v-model="login" />
      </div>

      <div class="form-group">
        <label for="password">Ваш пароль</label>
        <input type="password" id="password" v-model="password" />
      </div>

      <div class="form-group">
        <label for="confirmPassword">Повтор паролю</label>
        <input type="password" id="confirmPassword" v-model="confirmPassword" />
      </div>

      <div class="radio-group">
        <label> <input type="radio" value="candidate" v-model="role" checked /> Кандидат </label>
        <label> <input type="radio" value="recruiter" v-model="role" /> Рекрутер </label>
      </div>

      <div class="buttons">
        <router-link to="/login" class="register-button" type="submit">РЕЄСТРАЦІЯ</router-link>
        <router-link to="/" class="back-button" type="button">Назад</router-link>
      </div>
    </form>
  </div>
</template>

<script>
export default {
  data() {
    return {
      login: "",
      password: "",
      confirmPassword: "",
      role: "",
    };
  },
  methods: {
    handleSubmit() {
      const data = {
        login: this.login,
        password: this.password,
        confirmPassword: this.confirmPassword,
        role: this.role,
      };
      console.log(data);
    },
  },
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
</style>
