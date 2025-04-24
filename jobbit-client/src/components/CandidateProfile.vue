<template>
  <div class="profile-wrapper">
    <h2>Профіль кандидата</h2>

    <div v-if="!isEditing" class="profile-view">
      <p><strong>Ім’я:</strong> {{ profile.firstName }}</p>
      <p><strong>Прізвище:</strong> {{ profile.lastName }}</p>
      <p><strong>По батькові:</strong> {{ profile.fatherName }}</p>
      <p><strong>Вік:</strong> {{ profile.yearsOld }}</p>
      <p><strong>Країна:</strong> {{ profile.country }}</p>
      <p><strong>Місто:</strong> {{ profile.city }}</p>
      <p><strong>Досвід:</strong> {{ profile.experienceYears }}</p>
      <p><strong>Рівень англійської:</strong> {{ profile.levelEng }}</p>
      <p><strong>Бажана посада:</strong> {{ profile.jobWants }}</p>
      <p><strong>Бажана зарплата:</strong> {{ profile.salaryWants }}</p>
      <p><strong>Telegram:</strong> {{ profile.tgNick }}</p>
      <p><strong>Телефон:</strong> {{ profile.phone }}</p>
      <p><strong>GitHub:</strong> {{ profile.github }}</p>
      <p><strong>Резюме:</strong> {{ profile.cvPath }}</p>

      <button @click="isEditing = true">Редагувати</button>
    </div>

    <div v-else class="profile-edit">
      <label>Ім’я: <input v-model="profile.firstName" /></label>
      <label>Прізвище: <input v-model="profile.lastName" /></label>
      <label>По батькові: <input v-model="profile.fatherName" /></label>
      <label>Вік: <input type="number" v-model="profile.yearsOld" /></label>
      <label>Країна: <input v-model="profile.country" /></label>
      <label>Місто: <input v-model="profile.city" /></label>
      <label>Досвід (років): <input type="number" v-model="profile.experienceYears" /></label>
      <label>Рівень англійської: <input v-model="profile.levelEng" /></label>
      <label>Бажана посада: <input v-model="profile.jobWants" /></label>
      <label>Бажана зарплата: <input v-model="profile.salaryWants" /></label>
      <label>Telegram: <input v-model="profile.tgNick" /></label>
      <label>Телефон: <input v-model="profile.phone" /></label>
      <label>GitHub: <input v-model="profile.github" /></label>
      <label>Резюме (шлях): <input v-model="profile.cvPath" /></label>

      <button @click="saveChanges">Зберегти</button>
      <button @click="cancelEdit">Скасувати</button>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from "vue";
import ProfileService from "../services/CandProfileService.ts";

const isEditing = ref(false);
const profile = reactive({
  firstName: "",
  lastName: "",
  fatherName: "",
  yearsOld: 0,
  country: "",
  city: "",
  experienceYears: 0,
  levelEng: "",
  jobWants: "",
  salaryWants: 0,
  tgNick: "",
  phone: "",
  github: "",
  cvPath: ""
});

const userId = localStorage.getItem("userId");

if (!userId) {
  console.error("userId не знайдений в localStorage");
  alert("Ви не авторизовані!");
}

/**
 * Завантаження даних профілю у момент рендеру компонента
 */
function loadProfile() {
  if (userId) {
    ProfileService.getProfile(parseInt(userId))
        .then((response) => {
          Object.assign(profile, response.data);
        })
        .catch((err) => {
          console.error("Помилка при завантаженні профілю", err);
          alert("Не вдалося завантажити профіль");
        });
  }
}

/**
 * Збереження змін даних профілю кандидата
 */
function saveChanges() {
  if (userId) {
    ProfileService.updateProfile(profile)
        .then(() => {
          isEditing.value = false;
          alert("Профіль оновлено успішно!");
        })
        .catch((err) => {
          console.error("Помилка при збереженні профілю", err);
          alert("Сталася помилка при збереженні.");
        });
  }
}

/**
 * Скасовувати редагування
 */
const cancelEdit = () => {
  isEditing.value = false;
};

onMounted(loadProfile);
</script>

<style scoped>
.profile-wrapper {
  padding: 30px;
  max-width: 800px;
  margin: 0 auto;
}
label {
  display: block;
  margin: 10px 0;
}
input {
  width: 100%;
  padding: 8px;
}
button {
  margin: 10px 5px 0 0;
}
</style>
