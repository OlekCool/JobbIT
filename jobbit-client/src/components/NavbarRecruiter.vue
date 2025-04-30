<template>
  <nav role="navigation" class="navbar">
    <div class="profile-section" @click="goToRecruiterProfile" style="cursor: pointer">
      <h1 class="candName">
        {{ props.recruiterProfile?.firstName && props.recruiterProfile?.lastName
          ? `${props.recruiterProfile.firstName} ${props.recruiterProfile.lastName}`
          : 'Ім\'я Прізвище' }}
      </h1>

      <div class="profile-photo">
        <img src="../../../files/userPhotos/userDemo.png" alt="User Photo" />
      </div>
    </div>

    <div class="nav-buttons-section">
      <button @click="goToVacancies">Мої вакансії</button>
    </div>

    <div class="logout-section">
      <button @click="logout">Вийти</button>
    </div>
  </nav>
</template>

<script setup>
import { ref, defineProps } from 'vue';
import { useRouter } from "vue-router";
import recrProfileService from "@/services/ProfileService.ts";
import { HttpStatusCode } from "axios";
import { defineEmits } from 'vue';

const router = useRouter();
const userId = ref(JSON.parse(localStorage.getItem('userId')));
const emit = defineEmits(['show-profile']);
const props = defineProps({
  recruiterProfile: Object
});

/**
 * Метод для здійснення виходу з профілю
 */
const logout = () => {
  localStorage.removeItem('authToken');
  localStorage.removeItem('userRole');
  localStorage.removeItem('userProfile');
  localStorage.removeItem('userId');

  router.push("/");
};

/**
 * Метод для обробки події переходу до сторінки даних профілю рекрутера
 * @returns {Promise<void>} проміс
 */
const goToRecruiterProfile = async () => {
  try {
    const response = await recrProfileService.getProfileRecruiter(localStorage.getItem('userId'),
        localStorage.getItem('authToken'));

    if (response.status === HttpStatusCode.Ok) {
      console.log("Відображення успішне", response.data);
      emit('show-profile');
    } else {
      console.error("Помилка відображення даних профіля", HttpStatusCode);
    }
  } catch (error) {
    console.error("Помилка відображення даних профіля", error.response?.data || error.message); // eslint-disable-line no-console
  }
};

/**
 * Метод для обробки кліка на кнопку "Пошук"
 */
const goToVacancies = () => {
  emit('show-vacancies');
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
