<template>
  <nav role="navigation" class="navbar">
    <div class="profile-section" @click="goToCandidateProfile" style="cursor: pointer">
      <h1 class="candName"> {{ userProfile ? `${userProfile.firstName} ${userProfile.lastName}` : 'Вітаємо!' }} </h1>

      <div class="profile-photo">
        <img src="../assets/userPhotos/userDemo.png" alt="User Photo" />
      </div>
    </div>

    <div class="nav-buttons-section">
      <button>Пошук</button>
      <button>Збережене</button>
      <button>Відгуки</button>
      <button>Повідомлення</button>
    </div>

    <div class="logout-section">
      <button @click="logout">Вийти</button>
    </div>
  </nav>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from "vue-router";
import candProfileService from "@/services/CandProfileService.ts";
import { HttpStatusCode } from "axios";

const router = useRouter();
const userProfile = ref(JSON.parse(localStorage.getItem('userProfile')));

/**
 * Метод для здійснення виходу з профілю
 */
const logout = () => {
  localStorage.removeItem('authToken');
  localStorage.removeItem('userRole');
  localStorage.removeItem('userProfile');

  router.push("/");
};

/**
 * Метод для обробки події переходу до сторінки даних профілю кандидата
 * @returns {Promise<void>} проміс
 */
const goToCandidateProfile = async () => {
  try {
    const response = await candProfileService.getProfile(localStorage.getItem('userId'));

    if (response.status === HttpStatusCode.Ok) {
      console.log("Відображення успішне", response.data);
      await router.push("/candidate-dash/profile/" + localStorage.getItem('userId'));
    } else {
      console.error("Помилка відображення даних профіля", HttpStatusCode);
    }
  } catch (error) {
    console.error("Помилка відображення даних профіля", error.response?.data || error.message); // eslint-disable-line no-console
  }
};
</script>

<style scoped>
.candName {
  margin-right: 10px;
  font-size: 14px;
}

.navbar {
  display: flex;
  align-items: center;
  border-bottom: 2px solid #ccc;
  min-height: 100px;
  width: 100%;
  background-color: #f8f8f8;
}

.profile-section {
  flex-basis: 20%;
  display: flex;
  justify-content: center;
  align-items: center;
  border-right: 2px solid #ccc;
}

.profile-photo {
  width: 80px;
  height: 80px;
  background: #eee;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.profile-photo img {
  max-height: 60px;
  max-width: 60px;
  object-fit: contain;
}

/* Секція з кнопками */
.nav-buttons-section {
  flex-grow: 1;
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 30px;
  border-right: 2px solid #ccc;
}

.nav-buttons-section button {
  padding: 10px 20px;
  font-size: 16px;
  background-color: #fff;
  border: 1px solid #ccc;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.2s;
}

.nav-buttons-section button:hover {
  background-color: #e0e0e0;
}

/* Кнопка Logout */
.logout-section {
  padding: 0 30px;
  display: flex;
  align-items: center;
}

.logout-section button {
  padding: 10px 20px;
  font-size: 16px;
  background-color: #ffdddd;
  border: 1px solid #ccc;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.2s;
}

.logout-section button:hover {
  background-color: #ffbbbb;
}
</style>
