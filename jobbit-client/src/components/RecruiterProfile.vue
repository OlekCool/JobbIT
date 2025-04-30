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
          <h3>Особиста інформація:</h3>
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
            <label>ІТ-компанія:</label>
            <span v-if="!isEditing">{{ editableProfile.companyName }}</span>
            <input v-if="isEditing" v-model="editableProfile.companyName" />
          </div>
        </div>

        <div class="contact-info">
          <h3>Контакти:</h3>
          <div class="info-group">
            <label>Email:</label>
            <span v-if="!isEditing">{{ editableProfile.email }}</span>
            <input v-if="isEditing" v-model="editableProfile.email" />
          </div>
          <div class="info-group">
            <label>Телефон:</label>
            <span v-if="!isEditing">{{ editableProfile.phone }}</span>
            <input v-if="isEditing" v-model="editableProfile.phone" />
          </div>
          <div class="info-group">
            <label>Instagram:</label>
            <span v-if="!isEditing">{{ editableProfile.instagram }}</span>
            <input v-if="isEditing" v-model="editableProfile.instagram" />
          </div>
        </div>
      </div>

      <div class="location-info">
        <h3>Місцезнаходження:</h3>
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

      <div class="description">
        <h3>Про вас:</h3>
        <div class="info-group">
          <label>Опишіть компанію:</label>
          <span v-if="!isEditing">{{ editableProfile.description }}</span>
          <input v-if="isEditing" v-model="editableProfile.description" />
        </div>
      </div>

      <div class="actions">
        <button v-if="isEditing" @click="saveChanges">Зберегти дані</button>
        <button v-if="isEditing" @click="cancelEdit">Скасувати редагування</button>
        <button v-if="!isEditing" @click="isEditing = !isEditing">Редагувати дані</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted, defineProps, watch } from "vue";
import ProfileService from "../services/ProfileService.ts";

const props = defineProps({
  recruiterProfile: Object
});

const isEditing = ref(false);
const editableProfile = reactive(props.recruiterProfile ? { ...props.recruiterProfile } : {});
const userId = localStorage.getItem("userId");
const authToken = localStorage.getItem("authToken");

watch(() => props.recruiterProfile, (newProfile) => {
  if (newProfile) {
    Object.assign(editableProfile, newProfile);
  } else {
    Object.assign(editableProfile, {});
  }
});

onMounted(() => {
  if (props.recruiterProfile) {
    Object.assign(editableProfile, props.recruiterProfile);
  }
});

/**
 * Збереження змін даних профілю кандидата
 */
async function saveChanges() {
  if (userId && authToken) {
    try {
      await ProfileService.updateProfileRecruiter(editableProfile, userId, authToken);
      isEditing.value = false;
      window.location.reload();
    } catch (err) {
      console.error("Помилка при збереженні профілю рекрутера", err);
      alert("Сталася помилка при збереженні профілю рекрутера.");
    }
  }
}

/**
 * Скасовувати редагування
 */
const cancelEdit = () => {
  isEditing.value = false;
  Object.assign(editableProfile, props.recruiterProfile);
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
  order: 10;
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

.personal-info,
.contact-info,
.location-info,
.description {
  margin-bottom: 10px;
  padding: 10px;
  border: 1px solid #f0f0f0;
  border-radius: 5px;
  background-color: #f9f9f9;
}

.personal-info h3,
.contact-info h3,
.location-info h3,
.description h3 {
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

.description h3 {
  margin-top: 15px;
}

.description span {
  display: block;
  white-space: pre-line;
  font-size: 0.9em;
  line-height: 1.6;
}

.description input {
  width: 100%;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 3px;
  font-size: 0.9em;
  box-sizing: border-box;
  min-height: 100px;
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
</style>
