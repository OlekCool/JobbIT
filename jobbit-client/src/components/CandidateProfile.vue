<template>
  <div class="profile-page-layout">
    <div class="left-side">
      <div class="left-column">
        <div class="navigation-photo">
          <div class="profile-photo">
            <div style="width: 66px; height: 66px; border: 1px solid #ccc; display: flex; justify-content: center; align-items: center;">
              <img src="../../../files/userPhotos/userDemo.png" alt="Фото профілю" style="max-width: 66%; max-height: 66%;">
            </div>
          </div>
        </div>

        <div class="personal-info">
          <h3>Особиста інформація</h3>
          <div class="info-group">
            <label>Ім’я:</label>
            <span v-if="!isEditing">{{ editableProfile.firstName }}</span>
            <input v-if="isEditing" v-model="editableProfile.firstName" />
          </div>
          <div class="info-group">
            <label>Прізвище:</label>
            <span v-if="!isEditing">{{ editableProfile.lastName }}</span>
            <input v-if="isEditing" v-model="editableProfile.lastName" />
          </div>
          <div class="info-group">
            <label>По батькові:</label>
            <span v-if="!isEditing">{{ editableProfile.fatherName }}</span>
            <input v-if="isEditing" v-model="editableProfile.fatherName" />
          </div>
          <div class="info-group">
            <label>Вік:</label>
            <span v-if="!isEditing">{{ editableProfile.yearsOld }}</span>
            <input v-if="isEditing" type="number" v-model="editableProfile.yearsOld" />
          </div>
        </div>

        <div class="contact-info">
          <h3>Контакти</h3>
          <div class="info-group">
            <label>Telegram:</label>
            <span v-if="!isEditing">{{ editableProfile.tgNick }}</span>
            <input v-if="isEditing" v-model="editableProfile.tgNick" />
          </div>
          <div class="info-group">
            <label>Телефон:</label>
            <span v-if="!isEditing">{{ editableProfile.phone }}</span>
            <input v-if="isEditing" v-model="editableProfile.phone" />
          </div>
          <div class="info-group">
            <label>GitHub:</label>
            <span v-if="!isEditing">{{ editableProfile.github }}</span>
            <input v-if="isEditing" v-model="editableProfile.github" />
          </div>
        </div>
      </div>

      <div class="location-info">
        <h3>Місцезнаходження</h3>
        <div class="info-group">
          <label>Країна:</label>
          <span v-if="!isEditing">{{ editableProfile.country }}</span>
          <input v-if="isEditing" v-model="editableProfile.country" />
        </div>
        <div class="info-group">
          <label>Місто:</label>
          <span v-if="!isEditing">{{ editableProfile.city }}</span>
          <input v-if="isEditing" v-model="editableProfile.city" />
        </div>
      </div>

      <div class="experience-level">
        <h3>Досвід та рівень англійської</h3>
        <div class="info-group">
          <label>Досвід (років):</label>
          <span v-if="!isEditing">{{ editableProfile.experienceYears }}</span>
          <input v-if="isEditing" type="number" v-model="editableProfile.experienceYears" />
        </div>
        <div class="info-group">
          <label>Рівень англійської:</label>
          <span v-if="!isEditing">{{ editableProfile.levelEng }}</span>
          <input v-if="isEditing" v-model="editableProfile.levelEng" />
        </div>
      </div>

      <div class="desired-info">
        <h3>Бажана робота</h3>
        <div class="info-group">
          <label>Бажана посада:</label>
          <span v-if="!isEditing">{{ editableProfile.jobWants }}</span>
          <input v-if="isEditing" v-model="editableProfile.jobWants" />
        </div>
        <div class="info-group">
          <label>Бажана зарплата (грн.):</label>
          <span v-if="!isEditing">{{ editableProfile.salaryWants }}</span>
          <input v-if="isEditing" type="number" v-model="editableProfile.salaryWants" />
        </div>
      </div>

      <div class="cv-upload">
        <h3>Резюме</h3>
        <span v-if="!isEditing">{{ editableProfile.cvPath }}</span>
        <input v-if="isEditing" type="file" @change="handleCvUpload" />
      </div>

      <div class="actions">
        <button v-if="isEditing" @click="saveChanges">Зберегти дані</button>
        <button v-if="isEditing" @click="cancelEdit">Скасувати редагування</button>
        <button v-if="!isEditing" @click="isEditing = !isEditing">Редагувати дані</button>
      </div>

      <div class="projects-section">
        <div class="header-projects">
          <h3>Мої проєкти</h3>
          <button v-if="!isEditingProjects" @click="isEditingProjects = true" style="margin-top: 10px; padding: 5px 10px; font-size: 0.7em;">Керувати проєктами</button>
        </div>
        <div v-if="!isEditingProjects && !isAddingProject">
          <ul v-if="editableProjects">
            <li v-for="(project, index) in editableProjects" :key="index">
              <h4>{{ project.projName }}</h4>
              <p v-if="project.projDescription ">
                <strong>Опис:</strong> {{ project.projDescription  }}
              </p>
              <p v-if="project.projGithubLink">
                <strong>Посилання:</strong> <a :href="project.projGithubLink" target="_blank">{{ project.projGithubLink }}</a>
              </p>
              <img v-if="project.projPictSrc" :src="project.projPictSrc" alt="Фото проєкту" style="max-width: 100px; max-height: 100px;" />
            </li>
          </ul>
          <p v-else>Немає доданих проєктів.</p>
        </div>

        <div v-if="isEditingProjects && !isAddingProject && editingProject === null">
          <div class="header-projects-edit">
            <h4>Керувати проєктами</h4>
            <div>
              <button @click="addProject" class="project-action-button blue">Додати проєкт</button>
              <button @click="isEditingProjects = false" class="project-action-button secondary">Скасувати</button>
            </div>
          </div>
          <ul v-if="editableProjects.length > 0">
            <li v-for="(project, index) in editableProjects" :key="index" class="project-item-edit">
              <div class="project-info">
                <span>{{ project.projName }}</span>
                <a v-if="project.projGithubLink" :href="project.projGithubLink" target="_blank">(Посилання)</a>
              </div>
              <div class="project-actions">
                <button @click="editProject(project)" class="project-edit-button green">Редагувати</button>
                <button @click="deleteProject(index)" class="project-edit-button red">Видалити</button>
              </div>
            </li>
          </ul>
          <p v-else>Немає доданих проєктів.</p>
        </div>

        <div v-if="isEditingProjects && isAddingProject">
          <h4>Додати проєкт</h4>
          <div class="info-group">
            <label>Назва проєкту:</label>
            <input v-model="newProject.name" />
          </div>
          <div class="info-group">
            <label>Опис проєкту:</label>
            <textarea v-model="newProject.description"></textarea>
          </div>
          <div class="info-group">
            <label>Посилання:</label>
            <input v-model="newProject.link" />
          </div>
          <div class="info-group">
            <label>Фотографія:</label>
            <input type="file" @change="handleProjectPhotoUpload" />
          </div>
          <button @click="saveNewProject">Зберегти проєкт</button>
          <button @click="cancelAddProject">Скасувати</button>
        </div>

        <div v-if="isEditingProjects && editingProject !== null">
          <h4>Редагувати проєкт</h4>
          <div class="info-group">
            <label>Назва проєкту:</label>
            <input v-model="editingProject.name" />
          </div>
          <div class="info-group">
            <label>Опис проєкту:</label>
            <textarea v-model="editingProject.description"></textarea>
          </div>
          <div class="info-group">
            <label>Посилання:</label>
            <input v-model="editingProject.link" />
          </div>
          <div class="info-group">
            <label>Фотографія:</label>
            <input type="file" @change="handleProjectPhotoUpload" />
          </div>
          <button @click="saveEditedProject">Зберегти зміни</button>
          <button @click="cancelEditProject">Скасувати</button>
        </div>
      </div>

    </div>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted, defineProps, watch } from "vue";
import ProfileService from "../services/ProfileService.ts";
import ProjectService from "../services/ProjectService.ts";

const props = defineProps({
  candidateProfile: Object,
  candidateProjects: Array,
});

const isEditing = ref(false);
const isEditingProjects = ref(false);
const isAddingProject = ref(false);
const editingProject = ref(null);
const newProject = ref({ projName: '', projDescription: '', projGithubLink: '', photo: null });
const editableProjects = reactive([]);
const editableProfile = reactive(props.candidateProfile ? { ...props.candidateProfile } : {});
const userId = localStorage.getItem("userId");
const authToken = localStorage.getItem("authToken");

watch(() => props.candidateProfile, (newProfile) => {
  if (newProfile) {
    Object.assign(editableProfile, newProfile);
  } else {
    Object.assign(editableProfile, {});
  }
});

watch(() => props.candidateProjects, (newProjects) => {
  editableProjects.length = 0;
  if (newProjects && Array.isArray(newProjects)) {
    editableProjects.push(...newProjects);
  }
});

onMounted(() => {
  if (props.candidateProfile) {
    Object.assign(editableProfile, props.candidateProfile);
  }

  if (props.candidateProjects && Array.isArray(props.candidateProjects)) {
    editableProjects.push(...props.candidateProjects);
  }
});

/**
 * Збереження змін даних профілю кандидата
 */
async function saveChanges() {
  if (userId && authToken) {
    try {
      await ProfileService.updateProfileCandidate(editableProfile, userId, authToken);
      isEditing.value = false;
      window.location.reload();
    } catch (err) {
      console.error("Помилка при збереженні профілю", err);
      alert("Сталася помилка при збереженні.");
    }
  }
}

/**
 * Скасовувати редагування
 */
const cancelEdit = () => {
  isEditing.value = false;
  Object.assign(editableProfile, props.candidateProfile);
};

/**
 * Обробка завантаження резюме кандидата
 * @param event
 */
const handleCvUpload = (event) => {
  console.log("Завантажено файл:", event.target.files[0]);
  // TODO: реалізувати фронт
};

/**
 * Функція для додавання проєкта
 */
const addProject = () => {
  isAddingProject.value = true;
  editingProject.value = null;
  newProject.value = { projName: '', projDescription: '', projGithubLink: '' };
};

/**
 * Функція для редагування проєкта
 */
const editProject = (project) => {
  editingProject.value = { ...project };
};

/**
 * Функція для збереження нового проєкту
 */
async function saveNewProject() {
  if (userId && authToken) {
    try {
      await ProjectService.addProject(userId, newProject.value, authToken);
      isAddingProject.value = false;
      newProject.value = { projName: '', projDescription: '', projGithubLink: '' };
      window.location.reload();
    } catch (error) {
      console.error("Помилка при додаванні проєкту", error);
      alert("Не вдалося додати проєкт.");
    }
  }
}

/**
 * Функція для збереження проєкта
 */
async function saveEditedProject() {
  if (userId && authToken && editingProject.value) {
    try {
      await ProjectService.updateProject(editingProject.value.id, userId, editingProject.value, authToken);
      editingProject.value = null;
      window.location.reload();
    } catch (error) {
      console.error("Помилка при редагуванні проєкту", error);
      alert("Не вдалося оновити проєкт.");
    }
  }
}

/**
 * Функція для видалення проєкта
 */
async function deleteProject(projectId) {
  if (confirm('Ви впевнені, що хочете видалити цей проєкт?') && userId && authToken) {
    try {
      await ProjectService.deleteProject(projectId, userId, authToken);
      window.location.reload();
    } catch (error) {
      console.error("Помилка при видаленні проєкту", error);
      alert("Не вдалося видалити проєкт.");
    }
  }
}

/**
 * Функція для обробки завантаження фотографії проєкту
 * @param event обробник подій
 */
const handleProjectPhotoUpload = (event) => {
  if (isAddingProject.value) {
    newProject.value.photo = event.target.files[0];
  } else if (editingProject.value) {
    editingProject.value.photo = event.target.files[0];
  }
};

/**
 * Функція для скасування додавання проєкта
 */
const cancelAddProject = () => {
  isAddingProject.value = false;
  newProject.value = { projName: '', projDescription: '', projGithubLink: '' };
};

/**
 * Функція для скасування редагування проєкта
 */
const cancelEditProject = () => {
  editingProject.value = null;
};
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

/* project styles */

.projects-section ul {
  padding-left: 0;
  list-style: none;
}

.projects-section li {
  margin-bottom: 15px;
  padding: 10px;
  border: 1px solid #eee;
  border-radius: 5px;
}

.project-header h4 {
  margin: 0;
  font-size: 1.1em;
  flex-grow: 1;
}

.project-header a {
  text-decoration: none;
  color: #007bff;
  font-size: 0.9em;
}

.project-header a:hover {
  text-decoration: underline;
}

.project-header img {
  max-width: 80px;
  max-height: 80px;
  border-radius: 5px;
  object-fit: cover;
}

.projects-section button {
  padding: 5px 8px;
  font-size: 0.8em;
  cursor: pointer;
  border-radius: 3px;
  border: none;
  color: white;
  background-color: #007bff;
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
  background-color: #28a745;
  color: white;
}

.projects-section button:last-child:hover {
  background-color: #1e7e34;
}

.header-projects {
  align-items: center;
  margin-bottom: 10px;
}

.header-projects h4 {
  margin: 0;
}

.project-item-edit {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.project-info {
  display: flex;
  align-items: center;
  gap: 10px;
}

.project-info span {
  font-weight: bold;
  font-size: 0.9em;
}

.project-info a {
  font-size: 0.9em;
  color: rgba(0, 32, 0, 0.72);
  text-decoration: none;
  background-color: #e6f7ff;
  padding: 3px 8px;
  border-radius: 5px;
  margin-left: 5px;
}

.project-info a:hover {
  text-decoration: underline;
  background-color: #b3e0ff;
}

.project-actions {
  width: 200px;
}

.project-actions button {
  padding: 5px 10px;
  font-size: 0.8em;
  border-radius: 4px;
  cursor: pointer;
  border: none;
}

.project-action-button {
  padding: 8px 12px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.8em;
  border: none;
  color: white;
}
</style>
