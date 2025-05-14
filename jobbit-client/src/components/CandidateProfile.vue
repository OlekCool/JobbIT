<template>
  <div class="profile-page">
    <div class="profile-container">
      <div class="profile-header">
        <div class="profile-photo">
          <div class="profile-photo-container" @mouseover="showUploadHint = true" @mouseleave="showUploadHint = false">
            <div class="profile-photo-inner">
              <img :src="profilePhotoUrl" alt="Фото профілю">
              <div v-if="showUploadHint && isEditing && !props.isViewMode" class="upload-overlay">
                <label for="profile-photo-upload" class="upload-label">
                  <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" class="upload-icon">
                    <path fill-rule="evenodd" d="M12 3.75a.75.75 0 01.75.75v8.5h8.5a.75.75 0 010 1.5h-8.5v8.5a.75.75 0 01-1.5 0v-8.5H3.75a.75.75 0 010-1.5h8.5V4.5a.75.75 0 01.75-.75z" clip-rule="evenodd" />
                  </svg>
                  <span class="upload-text">Завантажити фото</span>
                </label>
                <input type="file" id="profile-photo-upload" @change="handleProfilePhotoChange" class="upload-input">
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="profile-info-section personal-info">
        <h3>Особиста інформація</h3>
        <div class="info-group">
          <label>Ім’я:</label>
          <span v-if="!isEditing">{{ editableProfile.firstName }}</span>
          <input v-if="!props.isViewMode && isEditing" v-model="editableProfile.firstName" />
        </div>
        <div class="info-group">
          <label>Прізвище:</label>
          <span v-if="!isEditing">{{ editableProfile.lastName }}</span>
          <input v-if="!props.isViewMode && isEditing" v-model="editableProfile.lastName" />
        </div>
        <div class="info-group">
          <label>По батькові:</label>
          <span v-if="!isEditing">{{ editableProfile.fatherName }}</span>
          <input v-if="!props.isViewMode && isEditing" v-model="editableProfile.fatherName" />
        </div>
        <div class="info-group">
          <label>Вік:</label>
          <span v-if="!isEditing">{{ editableProfile.yearsOld }}</span>
          <input v-if="!props.isViewMode && isEditing" type="number" v-model="editableProfile.yearsOld" />
        </div>
      </div>

      <div class="profile-info-section contact-info">
        <h3>Контакти</h3>
        <div class="info-group">
          <label>Telegram:</label>
          <span v-if="!isEditing">{{ editableProfile.tgNick }}</span>
          <input v-if="!props.isViewMode && isEditing" v-model="editableProfile.tgNick" />
        </div>
        <div class="info-group">
          <label>Телефон:</label>
          <span v-if="!isEditing">{{ editableProfile.phone }}</span>
          <input v-if="!props.isViewMode && isEditing" v-model="editableProfile.phone" />
        </div>
        <div class="info-group">
          <label>GitHub:</label>
          <span v-if="!isEditing">{{ editableProfile.github }}</span>
          <input v-if="!props.isViewMode && isEditing" v-model="editableProfile.github" />
        </div>
      </div>

      <div class="profile-info-section location-info">
        <h3>Місцезнаходження</h3>
        <div class="info-group">
          <label>Країна:</label>
          <span v-if="!isEditing">{{ editableProfile.country }}</span>
          <input v-if="!props.isViewMode && isEditing" v-model="editableProfile.country" />
        </div>
        <div class="info-group">
          <label>Місто:</label>
          <span v-if="!isEditing">{{ editableProfile.city }}</span>
          <input v-if="!props.isViewMode && isEditing" v-model="editableProfile.city" />
        </div>
      </div>

      <div class="profile-info-section experience-level">
        <h3>Досвід та рівень англійської</h3>
        <div class="info-group">
          <label>Досвід (років):</label>
          <span v-if="!isEditing">{{ editableProfile.experienceYears }}</span>
          <input v-if="!props.isViewMode && isEditing" type="number" v-model="editableProfile.experienceYears" />
        </div>
        <div class="info-group">
          <label>Рівень англійської:</label>
          <span v-if="!isEditing">{{ editableProfile.levelEng }}</span>
          <input v-if="!props.isViewMode && isEditing" v-model="editableProfile.levelEng" />
        </div>
      </div>

      <div class="profile-info-section desired-info">
        <h3>Бажана робота</h3>
        <div class="info-group">
          <label>Бажана посада:</label>
          <span v-if="!isEditing">{{ editableProfile.jobWants }}</span>
          <input v-if="!props.isViewMode && isEditing" v-model="editableProfile.jobWants" />
        </div>
        <div class="info-group">
          <label>Бажана зарплата (грн.):</label>
          <span v-if="!isEditing">{{ editableProfile.salaryWants }}</span>
          <input v-if="!props.isViewMode && isEditing" type="number" v-model="editableProfile.salaryWants" />
        </div>
      </div>

      <div class="profile-info-section cv-upload">
        <h3>Резюме</h3>
        <div v-if="editableProfile.cvPath" class="cv-link-container">
          <a :href="'/' + editableProfile.cvPath" target="_blank" class="cv-link">
            Переглянути резюме
          </a>
        </div>
        <div v-if="!props.isViewMode && isEditing" class="file-upload-container">
          <label for="cv-upload-input" class="file-upload-label">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" class="icon">
              <path fill-rule="evenodd" d="M12 2.25c-5.384 0-9.75 4.366-9.75 9.75s4.366 9.75 9.75 9.75 9.75-4.366 9.75-9.75S17.384 2.25 12 2.25zm3 10.25a.75.75 0 01-.75.75H9.75a.75.75 0 010-1.5h4.5a.75.75 0 01.75.75zm-3-4a.75.75 0 01-.75.75h-3a.75.75 0 010-1.5h3a.75.75 0 01.75.75zm3-4a.75.75 0 01-.75.75H9.75a.75.75 0 010-1.5h4.5a.75.75 0 01.75.75z" clip-rule="evenodd" />
            </svg>
            <span>Завантажити резюме</span>
          </label>
          <input v-if="!props.isViewMode" type="file" id="cv-upload-input" @change="handleCvUpload" class="file-upload-input" />
        </div>
      </div>

      <div class="actions">
        <button v-if="!props.isViewMode && isEditing" @click="saveChanges" class="primary">Зберегти дані</button>
        <button v-if="!props.isViewMode && isEditing" @click="cancelEdit" class="secondary">Скасувати редагування</button>
        <button v-if="!props.isViewMode && !isEditing" @click="isEditing = !isEditing" class="primary">Редагувати дані</button>
      </div>

      <div class="profile-info-section projects-section">
        <div class="section-header">
          <h3>Мої проєкти</h3>
          <button v-if="!props.isViewMode && !isEditingProjects" @click="isEditingProjects = true" class="control-button small">Керувати проєктами</button>
        </div>
        <div v-if="!isEditingProjects && !isAddingProject">
          <ul v-if="editableProjects">
            <li v-for="(project, index) in editableProjects" :key="index" class="project-item">
              <div class="project-details">
                <h4>{{ project.projName }}</h4>
                <p v-if="project.projDescription ">
                  <strong>Опис:</strong> {{ project.projDescription }}
                </p>
                <p v-if="project.projGithubLink">
                  <strong>Посилання:</strong> <a :href="project.projGithubLink" target="_blank" class="link">{{ project.projGithubLink }}</a>
                </p>
              </div>
              <img v-if="project.projPictSrc" :src="'/' + project.projPictSrc" alt="Фото проєкту" class="project-image" />
            </li>
          </ul>
          <p v-else class="no-data">Немає доданих проєктів.</p>
        </div>

        <div v-if="!props.isViewMode && isEditingProjects && !isAddingProject && editingProject === null">
          <div class="section-header-edit">
            <h4>Керувати проєктами</h4>
            <div>
              <button v-if="!props.isViewMode" @click="addProject" class="control-button primary small">Додати проєкт</button>
              <button v-if="!props.isViewMode" @click="isEditingProjects = false" class="control-button secondary small">Скасувати</button>
            </div>
          </div>
          <ul v-if="editableProjects.length > 0">
            <li v-for="project in editableProjects" :key="project.projId" class="project-item-edit">
              <div class="project-info">
                <span>{{ project.projName }}</span>
                <a v-if="project.projGithubLink" :href="project.projGithubLink" target="_blank" class="link">(Посилання)</a>
              </div>
              <div class="project-actions">
                <button v-if="!props.isViewMode" @click="editProject(project)" class="action-button green small">Редагувати</button>
                <button v-if="!props.isViewMode" @click="deleteProject(project.projId)" class="action-button red small">Видалити</button>
              </div>
            </li>
          </ul>
          <p v-else class="no-data">Немає доданих проєктів.</p>
        </div>

        <div v-if="!props.isViewMode && isEditingProjects && isAddingProject">
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
            <div class="file-upload-wrapper">
              <label for="project-photo-upload-new" class="file-upload-label small">Вибрати файл</label>
              <input type="file" id="project-photo-upload-new" @change="handleProjectPhotoUpload" class="file-input">
            </div>
          </div>
          <button v-if="!props.isViewMode" @click="saveNewProject" class="primary">Зберегти проєкт</button>
          <button v-if="!props.isViewMode" @click="cancelAddProject" class="secondary">Скасувати</button>
        </div>

        <div v-if="!props.isViewMode && isEditingProjects && editingProject !== null">
          <h4>Редагувати проєкт</h4>
          <div class="info-group">
            <label>Назва проєкту:</label>
            <input v-model="editingProject.projName" />
          </div>
          <div class="info-group">
            <label>Опис проєкту:</label>
            <textarea v-model="editingProject.projDescription"></textarea>
          </div>
          <div class="info-group">
            <label>Посилання:</label>
            <input v-model="editingProject.projGithubLink" />
          </div>
          <div class="info-group">
            <label>Фотографія:</label>
            <div class="file-upload-wrapper">
              <label for="project-photo-upload-new" class="file-upload-label small">Вибрати файл</label>
              <input type="file" id="project-photo-upload-new" @change="handleProjectPhotoUpload" class="file-input">
            </div>
          </div>
          <button v-if="!props.isViewMode" @click="saveEditedProject" class="primary">Зберегти зміни</button>
          <button v-if="!props.isViewMode" @click="cancelEditProject" class="secondary">Скасувати</button>
        </div>
      </div>

    </div>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted, defineProps, watch } from "vue";
import ProfileService from "../services/ProfileService.ts";
import ProjectService from "../services/ProjectService.ts";

/**
 * Пропс для отримання даних про профіль кандидата та його проєкти, а також фото, з батьківського компонента
 */
const props = defineProps({
  candidateProfile: Object,
  candidateProjects: Array,
  userPhoto: String,
  isViewMode: Boolean,
});

/**
 * Зміни стану, чи редагується профіль та проєкти кандидата
 * @type {Ref<UnwrapRef<boolean>, UnwrapRef<boolean> | boolean>}
 */
const isEditing = ref(false);
const isEditingProjects = ref(false);
const isAddingProject = ref(false);
const editingProject = ref(null);
const showUploadHint = ref(false);

// посилання на фото кандидата
const profilePhotoUrl = ref(props.userPhoto);

// для роботи з проєктами
const newProject = ref({ projName: '', projDescription: '', projGithubLink: '', photo: null });
const editableProjects = reactive([]);
const editableProfile = reactive(props.candidateProfile ? { ...props.candidateProfile } : {});

const userId = localStorage.getItem("userId");
const authToken = localStorage.getItem("authToken");

// мультимедійні файли (резюме та фото)
const cvFile = ref(null);
const profilePhotoFile = ref(null);

/**
 * Відстеження зміни подання нового фото у профіль
 */
watch(() => props.userPhoto, (newValue) => {
  profilePhotoUrl.value = newValue;
});

/**
 * Відстеження зміни даних профілю кандидата для збереження нових даних у редагований профіль
 */
watch(() => props.candidateProfile, (newProfile) => {
  if (newProfile) {
    Object.assign(editableProfile, newProfile);
  } else {
    Object.assign(editableProfile, {});
  }
});

/**
 * Відстеження змін у проєктах
 */
watch(() => props.candidateProjects, (newProjects) => {
  editableProjects.length = 0;
  if (newProjects && Array.isArray(newProjects)) {
    editableProjects.push(...newProjects);
    console.log('Оновлені проєкти:', editableProjects);
  }
});

/**
 * При монтуванні компонента збереження профілю рекрутера та його проєктів у редагований профіль
 */
onMounted(() => {
  if (props.candidateProfile) {
    Object.assign(editableProfile, props.candidateProfile);
  }

  if (props.candidateProjects && Array.isArray(props.candidateProjects)) {
    editableProjects.push(...props.candidateProjects);
  }

  console.log('Фото кандидата (для перегляду рекрутером): ' + props.userPhoto);
  console.log('Отримані проєкти:', props.candidateProjects);
});

/**
 * Скасовувати редагування
 */
const cancelEdit = () => {
  isEditing.value = false;
  cvFile.value = null;
  profilePhotoFile.value = null;
  profilePhotoUrl.value = props.candidateProfile?.photoSrc || 'files/userPhotos/userDemo.png'
  Object.assign(editableProfile, props.candidateProfile);
};

/**
 * Обробка завантаження резюме кандидата
 * @param event
 */
const handleCvUpload = (event) => {
  cvFile.value = event.target.files[0];
};

/**
 * Обробка завантаження фото кандидата
 * @param event
 */
const handleProfilePhotoChange = (event) => {
  const file = event.target.files[0];
  profilePhotoFile.value = file;

  if (file) {
    const reader = new FileReader();
    reader.onload = (e) => {
      profilePhotoUrl.value = e.target?.result;
    };
    reader.readAsDataURL(file);
  }
};

/**
 * Функція для початку додавання проєкта
 */
const addProject = () => {
  isAddingProject.value = true;
  editingProject.value = null;
  newProject.value = { projName: '', projDescription: '', projGithubLink: '' };
};

/**
 * Функція для початку редагування проєкту
 */
const editProject = (project) => {
  editingProject.value = { ...project };
};

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

// Функції для надсилання даних на сервер

/**
 * Збереження змін даних профілю кандидата (а також CV та фото)
 */
async function saveChanges() {
  if (userId && authToken) {
    try {
      await ProfileService.updateProfileCandidate(editableProfile, userId, authToken);
      isEditing.value = false;

      if (cvFile.value) {
        await uploadCv();
      }

      if (profilePhotoFile.value) {
        await uploadProfilePhoto();
      }

      window.location.reload();
    } catch (err) {
      alert("Сталася помилка при збереженні.");
    }
  }
}

/**
 * Обробка збереження CV кандидата на сервері
 * @returns {Promise<void>}
 */
async function uploadCv() {
  if (userId && authToken && cvFile.value) {
    const formData = new FormData();
    formData.append('cvFile', cvFile.value);

    try {
      const response = await ProfileService.uploadCvCandidate(formData, userId, authToken, 'multipart/form-data');
      editableProfile.cvPath = response.cvPath;

      window.location.reload();
    } catch (error) {
      alert("Не вдалося завантажити резюме.");
    }
  } else if (!cvFile.value) {
    alert("Будь ласка, виберіть файл резюме для завантаження.");
  }
}

/**
 * Обробка збереження фото користувача на сервері
 * @returns {Promise<void>}
 */
async function uploadProfilePhoto() {
  if (userId && authToken && profilePhotoFile.value) {
    const formData = new FormData();
    formData.append('photo', profilePhotoFile.value);

    try {
      const response = await ProfileService.uploadProfilePhotoCandidate(formData, userId, authToken, 'multipart/form-data');
      const photoPath = response?.photo || response?.filePath || response?.src;
      if (photoPath) {
        profilePhotoUrl.value = photoPath;
      }
      profilePhotoFile.value = null;
    } catch (error) {
      alert("Не вдалося завантажити фото профілю.");
    }
  }
}

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
      alert("Не вдалося додати проєкт.");
    }
  }
}

/**
 * Функція для збереження відредагованого проєкту
 */
async function saveEditedProject() {
  if (userId && authToken && editingProject.value && editingProject.value.projId) {
    const updatedProject = { ...editingProject.value };
    try {
      await ProjectService.updateProject(updatedProject.projId, userId, updatedProject, authToken);
      editingProject.value = null;

      window.location.reload();
    } catch (error) {
      alert("Не вдалося оновити проєкт.");
    }
  } else {
    alert("Не вдалося оновити проєкт. ID проєкту не знайдено.");
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
      alert("Не вдалося видалити проєкт!");
    }
  }
}
</script>

<style scoped>
/* Layout Styles */
.profile-page {
  display: flex;
  justify-content: center;
  padding: 20px;
  margin: 0 auto;
}

.profile-container {
  width: 60vw;
  max-width: 800px;
  display: flex;
  flex-direction: column;
  gap: 20px;
  max-height: 77vh;
  overflow-y: auto;
}

/* Header Styles */
.profile-header {
  display: flex;
  justify-content: center;
  margin-bottom: 20px;
}

.profile-photo {
  position: relative;
}

.profile-photo-container {
  width: 6vw;
  height: 13vh;
  border: 1px solid #ccc;
  border-radius: 50%;
  overflow: hidden;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
}

.profile-photo-inner {
  position: relative;
  width: 100%;
  height: 100%;
}

.profile-photo-inner img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.upload-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.3);
  display: flex;
  justify-content: center;
  align-items: center;
  opacity: 0;
  transition: opacity 0.3s ease;
  border-radius: 50%;
}

.upload-label {
  cursor: pointer;
  color: white;
  display: flex;
  flex-direction: column;
  align-items: center;
  font-size: 0.8em;
}

.upload-icon {
  width: 24px;
  height: 24px;
  fill: white;
  margin-bottom: 5px;
}

.upload-input {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  opacity: 0;
  cursor: pointer;
}

/* Section Styles */
.profile-info-section {
  padding: 15px;
  border: 1px solid #f0f0f0;
  border-radius: 5px;
  background-color: #f9f9f9;
  margin-bottom: 10px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.section-header h3,
.profile-info-section h3 {
  margin-top: 0;
  margin-bottom: 10px;
  font-size: 1.1em;
}

/* Info Group Styles */
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
.info-group input,
.info-group textarea {
  flex-grow: 1;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 3px;
  font-size: 0.9em;
  margin-left: 0;
  width: 100%;
  box-sizing: border-box;
  text-align: left;
}

.info-group textarea {
  min-height: 80px;
}

/* Button Styles */
.actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 20px;
}

.control-button,
.action-button,
.actions button {
  padding: 10px 15px;
  border-radius: 5px;
  cursor: pointer;
  font-size: 0.9em;
  font-weight: bold;
  border: none;
  transition: background-color 0.3s ease;
}

.control-button.small,
.action-button.small,
.actions button.small {
  padding: 5px 10px;
  font-size: 0.8em;
}

.primary {
  background-color: #007bff;
  color: white;
}

.primary:hover {
  background-color: #0056b3;
}

.secondary {
  background-color: #6c757d;
  color: white;
}

.secondary:hover {
  background-color: #545b62;
}

.green {
  background-color: #28a745;
  color: white;
}

.green:hover {
  background-color: #1e7e34;
}

.red {
  background-color: #dc3545;
  color: white;
}

.red:hover {
  background-color: #c82333;
}

/* Link Styles */
.link {
  color: #007bff;
  text-decoration: none;
}

.link:hover {
  text-decoration: underline;
}

/* Project Styles */
.projects-section ul {
  padding-left: 0;
  list-style: none;
}

.project-item {
  display: flex;
  gap: 15px;
  margin-bottom: 15px;
  padding: 10px;
  border: 1px solid #eee;
  border-radius: 5px;
  align-items: center;
}

.project-details {
  flex-grow: 1;
}

.project-details h4 {
  margin-top: 0;
  margin-bottom: 5px;
  font-size: 1em;
}

.project-image {
  max-width: 80px;
  max-height: 80px;
  border-radius: 5px;
  object-fit: cover;
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

.project-actions {
  display: flex;
  gap: 10px;
}

.section-header-edit {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.section-header-edit > div {
  display: flex;
  gap: 10px;
}

/* CV Upload Styles */
.cv-link-container {
  margin-top: 10px;
}

.cv-link {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  color: #28a745;
  text-decoration: none;
  border: 1px solid #28a745;
  padding: 8px 12px;
  border-radius: 5px;
}

.cv-link:hover {
  background-color: #1e7e34;
  color: white;
}

.icon {
  width: 20px;
  height: 20px;
}

.no-data {
  color: #777;
  font-style: italic;
  margin-top: 10px;
  display: block;
  font-size: 0.9em;
}

.file-upload-container {
  margin-top: 10px;
  position: relative;
  overflow: hidden;
  display: inline-block;
  border: 1px solid #ccc;
  border-radius: 5px;
}

.file-upload-label {
  padding: 8px 12px;
  display: inline-flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  background-color: #f8f9fa;
  color: #333;
  transition: background-color 0.3s ease;
  font-size: 0.9em;
}

.file-upload-label:hover {
  background-color: #e9ecef;
}

.file-upload-wrapper {
  position: relative; /* Робимо обгортку позиціонованим контейнером */
  display: inline-block; /* Щоб розміри залежали від контенту */
  border: 1px solid #ccc;
  border-radius: 5px;
  overflow: hidden; /* Приховує можливі виходи інпута за межі */
}

.file-upload-label.small {
  padding: 5px 10px;
  display: inline-flex;
  align-items: center;
  gap: 5px;
  cursor: pointer;
  background-color: #f8f9fa;
  color: #333;
  transition: background-color 0.3s ease;
  font-size: 0.8em;
}

.file-upload-label.small:hover {
  background-color: #e9ecef;
}

/* File Input Styles */
.file-input {
  position: absolute;
  left: 0;
  top: 0;
  opacity: 0;
  width: 100%;
  height: 100%;
  cursor: pointer;
}
</style>
