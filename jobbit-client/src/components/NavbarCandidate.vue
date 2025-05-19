<template>
  <nav role="navigation" class="navbar">
    <div class="profile-section" @click="goToCandidateProfile" style="cursor: pointer">
      <h1 class="candName">
        {{ props.candidateProfile?.firstName && props.candidateProfile?.lastName
          ? `${props.candidateProfile.firstName} ${props.candidateProfile.lastName}`
          : 'Ім\'я Прізвище' }}
      </h1>

      <div class="profile-photo">
        <img :src="userPhoto" alt="User Photo" />
      </div>
    </div>

    <div class="nav-buttons-section">
      <button @click="goToVacancies" :class="{ 'active': isActive('/candidate-dash/search') }">Пошук</button>
      <button @click="goToSaved" :class="{ 'active': isActive('/candidate-dash/saved') }">Збережене</button>
      <button @click="goToApplied" :class="{ 'active': isActive('/candidate-dash/applied') }">Відгуки</button>
      <button @click="goToNotifications" :class="{ 'active': isActive('/candidate-dash/notifications') }">Повідомлення</button>
    </div>

    <div class="logout-section">
      <button @click="logout">Вийти</button>
    </div>
  </nav>
</template>

<script setup>
import { ref, defineProps } from "vue";
import { useRouter, useRoute } from "vue-router";
import candProfileService from "@/services/ProfileService.ts";
import { HttpStatusCode } from "axios";
import { defineEmits } from 'vue';

const router = useRouter();
const route = useRoute();
const userId = ref(localStorage.getItem('userId'));
const emit = defineEmits(['show-profile', 'show-vacancies', 'show-saved-vacancies',
  'show-applied-vacancies', 'show-notifications']);

/**
 * Пропс для отримання даних про профіль кандидата та його фото з батьківського компонента
 */
const props = defineProps({
  candidateProfile: Object,
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
 * Метод для обробки події переходу до сторінки даних профілю кандидата
 * @returns {Promise<void>} проміс
 */
const goToCandidateProfile = async () => {
  try {
    const response = await candProfileService.getProfileCandidate(userId.value, localStorage.getItem('authToken'));

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

/**
 * Метод для обробки кліка на кнопку "Пошук"
 */
const goToSaved = () => {
  emit('show-saved-vacancies');
};

/**
 * Метод для обробки кліка на кнопку "Пошук"
 */
const goToApplied = () => {
  emit('show-applied-vacancies');
};

/**
 * Метод для обробки кліка на кнопку "Пошук"
 */
const goToNotifications = () => {
  emit('show-notifications');
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
  width: 6vw;
  height: 14vh;
  background: #eee;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
}

.profile-photo img {
  width: 100%;
  height: 100%;
  object-fit: cover;
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
  background-color: #aaffaa;
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
