<template>
  <div class="profile-page-layout">
    <div class="left-side">
      <div class="left-column">
        <div class="navigation-photo">
          <div class="profile-photo">
            <div style="width: 66px; height: 66px; border: 1px solid #ccc; display: flex; justify-content: center; align-items: center;">
              <img src="../assets/userPhotos/userDemo.png" alt="Фото профілю" style="max-width: 66%; max-height: 66%;">
            </div>
          </div>
        </div>

        <div class="personal-info">
          <h3>Особиста інформація</h3>
          <div class="info-group">
            <label>Ім’я:</label>
            <span v-if="!isEditing">{{ profile.firstName }}</span>
            <input v-if="isEditing" v-model="profile.firstName" />
          </div>
          <div class="info-group">
            <label>Прізвище:</label>
            <span v-if="!isEditing">{{ profile.lastName }}</span>
            <input v-if="isEditing" v-model="profile.lastName" />
          </div>
          <div class="info-group">
            <label>По батькові:</label>
            <span v-if="!isEditing">{{ profile.fatherName }}</span>
            <input v-if="isEditing" v-model="profile.fatherName" />
          </div>
          <div class="info-group">
            <label>Вік:</label>
            <span v-if="!isEditing">{{ profile.yearsOld }}</span>
            <input v-if="isEditing" type="number" v-model="profile.yearsOld" />
          </div>
        </div>

        <div class="contact-info">
          <h3>Контакти</h3>
          <div class="info-group">
            <label>Telegram:</label>
            <span v-if="!isEditing">{{ profile.tgNick }}</span>
            <input v-if="isEditing" v-model="profile.tgNick" />
          </div>
          <div class="info-group">
            <label>Телефон:</label>
            <span v-if="!isEditing">{{ profile.phone }}</span>
            <input v-if="isEditing" v-model="profile.phone" />
          </div>
          <div class="info-group">
            <label>GitHub:</label>
            <span v-if="!isEditing">{{ profile.github }}</span>
            <input v-if="isEditing" v-model="profile.github" />
          </div>
        </div>
      </div>

      <div class="right-column">
        <div class="location-info">
          <h3>Місцезнаходження</h3>
          <div class="info-group">
            <label>Країна:</label>
            <span v-if="!isEditing">{{ profile.country }}</span>
            <input v-if="isEditing" v-model="profile.country" />
          </div>
          <div class="info-group">
            <label>Місто:</label>
            <span v-if="!isEditing">{{ profile.city }}</span>
            <input v-if="isEditing" v-model="profile.city" />
          </div>
        </div>

        <div class="experience-level">
          <h3>Досвід та рівень англійської</h3>
          <div class="info-group">
            <label>Досвід (років):</label>
            <span v-if="!isEditing">{{ profile.experienceYears }}</span>
            <input v-if="isEditing" type="number" v-model="profile.experienceYears" />
          </div>
          <div class="info-group">
            <label>Рівень англійської:</label>
            <span v-if="!isEditing">{{ profile.levelEng }}</span>
            <input v-if="isEditing" v-model="profile.levelEng" />
          </div>
        </div>

        <div class="desired-info">
          <h3>Бажана робота</h3>
          <div class="info-group">
            <label>Бажана посада:</label>
            <span v-if="!isEditing">{{ profile.jobWants }}</span>
            <input v-if="isEditing" v-model="profile.jobWants" />
          </div>
          <div class="info-group">
            <label>Бажана зарплата (грн.):</label>
            <span v-if="!isEditing">{{ profile.salaryWants }}</span>
            <input v-if="isEditing" type="number" v-model="profile.salaryWants" />
          </div>
        </div>

        <div class="cv-upload">
          <h3>Резюме</h3>
          <span v-if="!isEditing">{{ profile.cvPath }}</span>
          <input v-if="isEditing" type="file" @change="handleCvUpload" />
        </div>

        <div class="actions">
          <button v-if="isEditing" @click="saveChanges">Зберегти дані</button>
          <button v-if="isEditing" @click="cancelEdit">Скасувати редагування</button>
          <button v-if="!isEditing" @click="isEditing = !isEditing">Редагувати дані</button>
        </div>
      </div>
    </div>

    <div class="right-side">
      <h3>Мої проєкти</h3>
      <ul>
        <li>Проєкт 1 (Опис проєкту та посилання)</li>
        <li>Проєкт 2 (Опис проєкту та посилання)</li>
        <li>Проєкт 3 (Опис проєкту та посилання)</li>
      </ul>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted, toRaw } from "vue";
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

/**
 * Завантаження даних профілю у момент рендерингу компонента
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
  loadProfile();
};

/**
 * Обробка завантаження резюме кандидата
 * @param event
 */
const handleCvUpload = (event) => {
  console.log("Завантажено файл:", event.target.files[0]);
};

onMounted(loadProfile);
</script>

<style scoped>
.profile-page-layout {
  display: grid;
  grid-template-columns: auto auto;
  gap: 20px;
  padding: 20px;
  margin: 0 auto;
}

.left-side {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
  border: 1px solid #ccc;
  border-radius: 5px;
  padding: 10px;
  width: 60vw;
}

.left-side > .navigation-photo {
  grid-column: 1 / 3;
  width: 91%;
  padding: 10px;
  margin-bottom: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
  cursor: pointer;
  text-align: left;
}

.left-side > .actions {
  grid-column: 1 / 3;
}

.right-side {
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
  width: 28vw;
}

.profile-photo {
  display: flex;
  justify-content: center;
  margin: 20px 0;
}

.personal-info, .contact-info, .location-info, .experience-level, .desired-info, .cv-upload {
  margin-bottom: 10px;
  padding: 10px;
  border: 1px solid #f0f0f0;
  border-radius: 5px;
  background-color: #f9f9f9;
}

.personal-info h3, .contact-info h3, .location-info h3, .experience-level h3, .desired-info h3, .cv-upload h3 {
  margin-top: 0;
  margin-bottom: 5px;
  font-size: 1em;
}

.info-group {
  display: flex;
  align-items: center;
  margin-bottom: 5px;
}

.info-group label {
  font-weight: bold;
  width: 90px;
  margin-right: 5px;
  font-size: 0.9em;
  flex-shrink: 0;
}

.info-group span,
.info-group input[type="text"],
.info-group input[type="number"] {
  flex-grow: 1;
  padding: 5px;
  border: 1px solid #ccc;
  border-radius: 3px;
  font-size: 0.9em;
  margin-left: 0;
  width: 50%;
}

.cv-upload input[type="file"] {
  flex-grow: 1;
  padding: 5px;
  border: 1px solid #ccc;
  border-radius: 3px;
  margin-left: 0;
  font-size: 0.9em;
  width: 100%;
  box-sizing: border-box;
}

.actions {
  display: flex;
  justify-content: space-around;
  margin-top: 10px;
}

.actions button {
  padding: 8px 10px;
  border-radius: 5px;
  cursor: pointer;
  font-size: 0.8em;
}

.actions button:first-child {
  background-color: #28a745;
  color: white;
  border: 1px solid #28a745;
}

.actions button:first-child:hover {
  background-color: #1e7e34;
}

.actions button:nth-child(2) {
  background-color: #007bff;
  color: white;
  border: 1px solid #007bff;
}

.actions button:nth-child(2):hover {
  background-color: #0056b3;
}

span, label {
  text-align: left;
  font-size: 0.85em;
}

.right-side h3 {
  font-size: 1em;
  margin-top: 0;
  margin-bottom: 5px;
}

.right-side ul {
  padding-left: 20px;
  margin-bottom: 0;
  font-size: 0.9em;
}

.right-side li {
  margin-bottom: 3px;
}
</style>
