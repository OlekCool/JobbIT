<template>
  <nav role="navigation" class="navbar">
    <div class="profile-section" @click="goToRecruiterProfile" style="cursor: pointer">
      <h1 class="candName">
        {{ props.recruiterProfile?.firstName && props.recruiterProfile?.lastName
          ? `${props.recruiterProfile.firstName} ${props.recruiterProfile.lastName}`
          : 'Ім\'я Прізвище' }}
      </h1>

      <div class="profile-photo">
        <img :src="userPhoto" alt="User Photo" />
      </div>
    </div>

    <div class="nav-buttons-section">
      <button @click="goToMyVacancies" :class="{ 'active': isActive('/recruiter-dash/my-vacancies') }">Мої вакансії</button>
      <button @click="goToAllVacancies" :class="{ 'active': isActive('/recruiter-dash/all-vacancies') }">Всі вакансії</button>
    </div>

    <div class="logout-section">
      <button @click="logout">Вийти</button>
    </div>
  </nav>
</template>

<script setup>
import { ref, defineProps } from 'vue';
import { useRouter, useRoute } from "vue-router";
import recrProfileService from "@/services/ProfileService.ts";
import { HttpStatusCode } from "axios";
import { defineEmits } from 'vue';

const router = useRouter();
const route = useRoute();
const userId = ref(JSON.parse(localStorage.getItem('userId')));
const emit = defineEmits(['show-profile', 'show-my-vacancies', 'show-all-vacancies']);
const props = defineProps({
  recruiterProfile: Object,
  userPhoto: String
});

/**
 * Метод для перевірки, чи є поточний шлях активним. Потрібно для підсвічення кнопки,
 * яка відповідає певному шляху (який зараз)
 * @param path Шлях для перевірки
 * @returns {boolean} True, якщо поточний шлях співпадає з переданим, інакше false
 */
const isActive = (path) => {
  return route.path === path;
};

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
      emit('show-profile');
    } else {
      console.error("Помилка відображення даних профіля", HttpStatusCode);
    }
  } catch (error) {
    console.error("Помилка відображення даних профіля", error.response?.data || error.message); // eslint-disable-line no-console
  }
};

/**
 * Метод для обробки кліка на кнопку "Мої вакансії"
 */
const goToMyVacancies = () => {
  emit('show-my-vacancies');
};

/**
 * Метод для обробки кліка на кнопку "Всі вакансії"
 */
const goToAllVacancies = () => {
  emit('show-all-vacancies');
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

.nav-buttons-section button.active {
  background-color: #aaffaa; /* Зелений колір для активної кнопки */
  border-color: #88ff88;
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
