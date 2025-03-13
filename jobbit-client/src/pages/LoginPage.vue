<template>
  <div class="login-container">
    <form @submit.prevent="handleSubmit" class="login-box">
      <h2 class="login-title">АВТОРИЗАЦІЯ</h2>

      <div class="form-group">
        <label for="login">Ваш логін</label>
        <input type="text" id="login" v-model="login" />
      </div>

      <div class="form-group form-group-last">
        <label for="password">Ваш пароль</label>
        <input type="password" id="password" v-model="password" />
      </div>

      <router-link to="/" class="login-button" type="submit">АВТОРИЗАЦІЯ</router-link>

      <div class="buttons">
        <button class="forgot-button" type="button">Забули пароль?</button>
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
    };
  },
  methods: {
    handleSubmit() {
      const data = {
        login: this.login,
        password: this.password,
      };
      console.log(data);
    },
  },
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
}

.form-group {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.form-group-last {
  margin-bottom: 40px;
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

.buttons {
  margin-top: 10px;
  display: flex;
  justify-content: space-around;
  text-decoration: none;
}

.login-button {
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

.login-button:hover {
  background-color: #3c963f;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

.back-button,
.forgot-button {
  margin-top: 20px;
  background-color: #fff;
  border: none;
  cursor: pointer;
  text-decoration: none;
}

.forgot-button {
  color: #2937ff;
}

.back-button {
  color: #000;
}

.back-button:hover,
.forgot-button:hover {
  text-decoration: underline;
}
</style>
