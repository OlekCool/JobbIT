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

      <div class="projects-section">
        <h3>Мої проєкти</h3>
        <div v-if="!isEditingProjects && !isAddingProject">
          <ul v-if="profile.projects && profile.projects.length > 0">
            <li v-for="(project, index) in profile.projects" :key="index">
              {{ project.name }}
            </li>
          </ul>
          <p v-else>Немає доданих проєктів.</p>
          <button @click="isEditingProjects = true" style="margin-top: 10px; padding: 5px 10px; font-size: 0.7em;">Керувати проєктами</button>
        </div>

        <div v-if="isEditingProjects && !isAddingProject && editingProjectIndex === null">
          <button @click="addProject" style="margin-top: 5px; padding: 5px 10px; font-size: 0.7em;">Додати проєкт</button>
          <ul v-if="profile.projects && profile.projects.length > 0">
            <li v-for="(project, index) in profile.projects" :key="index" style="display: flex; align-items: center; gap: 10px; margin-bottom: 5px;">
              <span>{{ project.name }}</span>
              <button @click="editProject(project, index)" style="padding: 3px 5px; font-size: 0.7em;">Редагувати</button>
              <button @click="deleteProject(index)" style="padding: 3px 5px; font-size: 0.7em; background-color: #dc3545; color: white; border: none; border-radius: 3px; cursor: pointer;">Видалити</button>
            </li>
          </ul>
          <p v-else>Немає доданих проєктів.</p>
        </div>

        <div v-if="isEditingProjects && isAddingProject">
          <h4>Додати проєкт</h4>
          <div class="info-group">
            <label>Назва проєкту:</label>
            <input v-model="currentProject.name" />
          </div>
          <div class="info-group">
            <label>Опис проєкту:</label>
            <textarea v-model="currentProject.description"></textarea>
          </div>
          <div class="info-group">
            <label>Посилання:</label>
            <input v-model="currentProject.link" />
          </div>
          <button @click="saveNewProject">Зберегти проєкт</button>
          <button @click="isAddingProject = false; currentProject = { name: '', description: '', link: '' };">Скасувати</button>
        </div>

        <div v-if="isEditingProjects && editingProjectIndex !== null">
          <h4>Редагувати проєкт</h4>
          <div class="info-group">
            <label>Назва проєкту:</label>
            <input v-model="currentProject.name" />
          </div>
          <div class="info-group">
            <label>Опис проєкту:</label>
            <textarea v-model="currentProject.description"></textarea>
          </div>
          <div class="info-group">
            <label>Посилання:</label>
            <input v-model="currentProject.link" />
          </div>
          <button @click="saveProject">Зберегти зміни</button>
          <button @click="cancelEditProject">Скасувати</button>
        </div>
      </div>

    </div>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from "vue";
import ProfileService from "../services/ProfileService.ts";

const isEditing = ref(false);
const isEditingProjects = ref(false);
const isAddingProject = ref(false);
const editingProjectIndex = ref(null);
const currentProject = ref({ name: '', description: '', link: '' });
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
  cvPath: "",
  projects: []
});
const userId = localStorage.getItem("userId");

/**
 * Завантаження даних профілю у момент рендерингу компонента
 */
function loadProfile() {
  if (userId) {
    ProfileService.getProfileCandidate(parseInt(userId))
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
    ProfileService.updateProfileCandidate(profile)
    .then(() => {
      isEditing.value = false;
      loadProfile();
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

/**
 * Функція для додавання проєкта
 */
const addProject = () => {
  isAddingProject.value = true;
  editingProjectIndex.value = null;
  currentProject.value = { name: '', description: '', link: '' };
};

/**
 * Функція для редагування проєкта
 */
const editProject = (project, index) => {
  editingProjectIndex.value = index;
  currentProject.value = { ...project };
};

/**
 * Функція для збереження нового проєкту
 */
const saveNewProject = () => {
  profile.projects.push({ ...currentProject.value });
  isAddingProject.value = false;
  currentProject.value = { name: '', description: '', link: '' };
};


/**
 * Функція для збереження проєкта
 */
const saveProject = () => {
  if (editingProjectIndex.value !== null) {
    profile.projects[editingProjectIndex.value] = { ...currentProject.value };
    editingProjectIndex.value = null;
  }
  currentProject.value = { name: '', description: '', link: '' };
};

/**
 * Функція для видалення проєкта
 */
const deleteProject = (index) => {
  if (confirm('Ви впевнені, що хочете видалити цей проєкт?')) {
    profile.projects.splice(index, 1);
  }
};

/**
 * Функція для скасування редагування проєкта
 */
const cancelEditProject = () => {
  editingProjectIndex.value = null;
  currentProject.value = { name: '', description: '', link: '' };
};

onMounted(loadProfile);
</script>

<style scoped>
.profile-page-layout {
  display: grid;
  grid-template-columns: 1fr;
  gap: 20px;
  padding: 20px;
  margin: 0 auto;
  width: 60vw;
  max-height: 75vh;
  overflow-y: auto;
}

.left-side {
  display: flex;
  flex-direction: column;
  gap: 20px;
  border: 1px solid #ccc;
  border-radius: 5px;
  padding: 10px;
}

.left-side > .actions {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

.profile-photo {
  display: flex;
  justify-content: center;
  margin: 20px 0;
}

.profile-photo > div {
  width: 66px;
  height: 66px;
  border: 1px solid #ccc;
  display: flex;
  justify-content: center;
  align-items: center;
  overflow: hidden;
  border-radius: 50%;
}

.personal-info, .contact-info, .location-info, .experience-level,
.desired-info, .cv-upload, .projects-section {
  margin-bottom: 10px;
  padding: 10px;
  border: 1px solid #f0f0f0;
  border-radius: 5px;
  background-color: #f9f9f9;
}

.personal-info h3, .contact-info h3, .location-info h3, .experience-level h3,
.desired-info h3, .cv-upload h3, .projects-section h3 {
  margin-top: 0;
  margin-bottom: 10px;
  font-size: 1.1em;
}

.info-group {
  display: flex;
  align-items: center;
  margin-bottom: 8px;
}

.info-group label {
  font-weight: bold;
  width: 120px;
  margin-right: 10px;
  font-size: 0.9em;
  flex-shrink: 0;
  text-align: left;
}

.info-group span,
.info-group input {
  flex-grow: 1;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 3px;
  font-size: 0.9em;
  margin-left: 0;
  width: 100%;
  box-sizing: border-box;
}

.actions {
  display: flex;
  justify-content: space-around;
}

.actions button {
  padding: 10px 15px;
  border-radius: 5px;
  cursor: pointer;
  font-size: 0.9em;
  font-weight: bold;
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
  font-size: 0.9em;
}

.projects-section ul {
  padding-left: 20px;
}

.projects-section li {
  margin-bottom: 5px;
}

.projects-section button {
  padding: 5px 8px;
  font-size: 0.8em;
  cursor: pointer;
  border-radius: 3px;
}

.projects-section button:first-child {
  background-color: #007bff;
  color: white;
  border: 1px solid #007bff;
}

.projects-section button:first-child:hover {
  background-color: #0056b3;
}

.projects-section button:nth-child(2) {
  background-color: #28a745;
  color: white;
  border: 1px solid #28a745;
}

.projects-section button:nth-child(2):hover {
  background-color: #1e7e34;
}

.projects-section button:last-child {
  background-color: #dc3545;
  color: white;
  border: 1px solid #dc3545;
}

.projects-section button:last-child:hover {
  background-color: #c82333;
}
</style>
