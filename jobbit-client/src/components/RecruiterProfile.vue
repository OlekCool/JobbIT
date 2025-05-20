<template>
  <div class="profile-page">
    <div class="profile-container">
      <header class="profile-header">
        <div class="profile-photo">
          <div class="profile-photo-container" @mouseover="showUploadHint = true" @mouseleave="showUploadHint = false"
               aria-live="polite" aria-atomic="true">
            <div class="profile-photo-inner">
              <img :src="profilePhotoUrl" alt="Фото профілю">
              <div v-if="showUploadHint && isEditing" class="upload-overlay" role="group" aria-label="Завантажити фото профілю рекрутера">
                <label for="profile-photo-upload" class="upload-label">
                  <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" class="upload-icon" aria-hidden="true">
                    <path fill-rule="evenodd" d="M12 3.75a.75.75 0 01.75.75v8.5h8.5a.75.75 0 010 1.5h-8.5v8.5a.75.75 0 01-1.5 0v-8.5H3.75a.75.75 0 010-1.5h8.5V4.5a.75.75 0 01.75-.75z" clip-rule="evenodd" />
                  </svg>
                  <span class="upload-text">Завантажити фото</span>
                </label>
                <input type="file" id="profile-photo-upload" @change="handleProfilePhotoChange" class="upload-input"
                       aria-label="Виберіть файл для завантаження фото профілю">
              </div>
            </div>
          </div>
        </div>
      </header>

      <section class="profile-info-section personal-info" aria-labelledby="personal-info-heading">
        <h3 id="personal-info-heading">Особиста інформація</h3>
        <div class="info-group">
          <label for="recruiterFirstName">Ім’я:</label>
          <span v-if="!isEditing">{{ editableProfile.firstName }}</span>
          <input v-if="isEditing" v-model="editableProfile.firstName" id="recruiterFirstName" />
        </div>
        <div class="info-group">
          <label for="recruiterLastName">Прізвище:</label>
          <span v-if="!isEditing">{{ editableProfile.lastName }}</span>
          <input v-if="isEditing" v-model="editableProfile.lastName" id="recruiterLastName" />
        </div>
        <div class="info-group">
          <label for="recruiterFatherName">По батькові:</label>
          <span v-if="!isEditing">{{ editableProfile.fatherName }}</span>
          <input v-if="isEditing" v-model="editableProfile.fatherName" id="recruiterFatherName" />
        </div>
        <div class="info-group">
          <label for="recruiterCompanyName">ІТ-компанія:</label>
          <span v-if="!isEditing">{{ editableProfile.companyName }}</span>
          <input v-if="isEditing" v-model="editableProfile.companyName" id="recruiterCompanyName" />
        </div>
      </section>

      <section class="profile-info-section contact-info" aria-labelledby="contact-info-heading">
        <h3 id="contact-info-heading">Контакти</h3>
        <div class="info-group">
          <label for="recruiterEmail">Email:</label>
          <span v-if="!isEditing">{{ editableProfile.email }}</span>
          <input v-if="isEditing" v-model="editableProfile.email" id="recruiterEmail" />
        </div>
        <div class="info-group">
          <label for="recruiterPhone">Телефон:</label>
          <span v-if="!isEditing">{{ editableProfile.phone }}</span>
          <input v-if="isEditing" v-model="editableProfile.phone" id="recruiterPhone" />
        </div>
        <div class="info-group">
          <label for="recruiterInstagram">Instagram:</label>
          <span v-if="!isEditing">{{ editableProfile.instagram }}</span>
          <input v-if="isEditing" v-model="editableProfile.instagram" id="recruiterInstagram" />
        </div>
      </section>

      <section class="profile-info-section location-info" aria-labelledby="location-info-heading">
        <h3 id="location-info-heading">Місцезнаходження</h3>
        <div class="info-group">
          <label for="recruiterCountry">Країна:</label>
          <span v-if="!isEditing">{{ editableProfile.country }}</span>
          <input v-if="isEditing" v-model="editableProfile.country" id="recruiterCountry" />
        </div>
        <div class="info-group">
          <label for="recruiterCity">Місто:</label>
          <span v-if="!isEditing">{{ editableProfile.city }}</span>
          <input v-if="isEditing" v-model="editableProfile.city" id="recruiterCity" />
        </div>
      </section>

      <section class="profile-info-section description" aria-labelledby="description-heading">
        <h3 id="description-heading">Про вас</h3>
        <div class="info-group">
          <label>Опишіть компанію:</label>
          <textarea v-if="isEditing" v-model="editableProfile.description" id="companyDescription" rows="5"></textarea>
          <span v-else>{{ editableProfile.description }}</span>
        </div>
      </section>

      <div class="actions">
        <button v-if="isEditing" @click="saveChanges" class="primary"
                aria-label="Зберегти всі зміни у профілі рекрутера">Зберегти дані</button>
        <button v-if="isEditing" @click="cancelEdit" class="secondary"
                aria-label="Скасувати редагування профілю рекрутера">Скасувати редагування</button>
        <button v-if="!isEditing" @click="isEditing = !isEditing" class="primary"
                aria-label="Перейти до редагування профілю рекрутера">Редагувати дані</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted, defineProps, watch } from "vue";
import ProfileService from "../services/ProfileService.ts";

/**
 * Пропс для отримання даних про профіль рекрутера та його фото з батьківського компонента
 */
const props = defineProps({
  recruiterProfile: Object,
  userPhoto: String
});

const isEditing = ref(false);
const editableProfile = reactive(props.recruiterProfile ? { ...props.recruiterProfile } : {});

const showUploadHint = ref(false);
const profilePhotoFile = ref(null);

const profilePhotoUrl = ref(props.userPhoto);

const userId = localStorage.getItem("userId");
const authToken = localStorage.getItem("authToken");

/**
 * Відстеження змін у профілі для збереження редагованого профілю рекрутера
 */
watch(() => props.recruiterProfile, (newProfile) => {
  if (newProfile) {
    Object.assign(editableProfile, newProfile);
  } else {
    Object.assign(editableProfile, {});
  }
});

/**
 * При монтуванні компонента збереження профілю рекрутера у редагований профіль
 */
onMounted(() => {
  if (props.recruiterProfile) {
    Object.assign(editableProfile, props.recruiterProfile);
  }
});

/**
 * Скасовувати редагування
 */
const cancelEdit = () => {
  isEditing.value = false;
  profilePhotoFile.value = null;
  profilePhotoUrl.value = props.recruiterProfile?.photoSrc || 'files/userPhotos/userDemo.png'
  Object.assign(editableProfile, props.recruiterProfile);
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
 * Обробка збереження фото користувача на сервері
 * @returns {Promise<void>}
 */
async function uploadProfilePhoto() {
  if (userId && authToken && profilePhotoFile.value) {
    const formData = new FormData();
    formData.append('photo', profilePhotoFile.value);

    try {
      const response = await ProfileService.uploadProfilePhotoRecruiter(formData, userId, authToken, 'multipart/form-data');
      const photoPath = response?.photo || response?.filePath || response?.src;
      if (photoPath) {
        profilePhotoUrl.value = `/${photoPath}`;
      }
      profilePhotoFile.value = null;
    } catch (error) {
      alert("Не вдалося завантажити фото профілю.");
    }
  }
}

/**
 * Збереження змін даних профілю кандидата
 */
async function saveChanges() {
  if (userId && authToken) {
    try {
      await ProfileService.updateProfileRecruiter(editableProfile, userId, authToken);
      isEditing.value = false;

      if (profilePhotoFile.value) {
        await uploadProfilePhoto();
      }

      window.location.reload();
    } catch (err) {
      alert("Сталася помилка при збереженні профілю рекрутера.");
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

.actions button {
  padding: 10px 15px;
  border-radius: 5px;
  cursor: pointer;
  font-size: 0.9em;
  font-weight: bold;
  border: none;
  transition: background-color 0.3s ease;
}

.actions button {
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

.visually-hidden {
  position: absolute;
  width: 1px;
  height: 1px;
  margin: -1px;
  padding: 0;
  overflow: hidden;
  clip: rect(0, 0, 0, 0);
  border: 0;
}
</style>
