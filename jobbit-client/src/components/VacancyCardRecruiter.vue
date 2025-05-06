<template>
  <div class="vacancy-card-recruiter">
    <VacancyDetails :vacancy="vacancy" />
    <div class="actions">
      <button class="edit-button" @click="openEditVacancyModal">Редагувати</button>
      <button class="delete-button" @click="deleteVacancy">Видалити</button>
    </div>

    <div class="applicants-list">
      <h3>Кандидати, які відгукнулися:</h3>
      <ul v-if="applicants.length > 0">
        <li v-for="applicant in applicants" :key="applicant.id" class="applicant-item">
          <div class="applicant-info" @click="$emit('open-candidate-profile', applicant.id)">
            <div class="applicant-photo" v-if="applicant.photo">
              <img :src="'/' + applicant.photo" alt="Фото кандидата">
            </div>
            <div class="applicant-details">
              <span class="applicant-name">{{ applicant.firstName }} {{ applicant.lastName }}</span>
              <span class="applicant-email">{{ applicant.email }}</span>
            </div>
          </div>
          <div class="applicant-actions">
            <button class="accept-button">Прийняти</button>
            <button class="reject-button">Відхилити</button>
          </div>
        </li>
      </ul>
      <p v-else>Поки що немає кандидатів, які відгукнулися.</p>
    </div>

    <VacancyModal
        v-if="showVacancyModal"
        :is-open="showVacancyModal"
        :is-edit-mode="isEditMode"
        :vacancy-to-edit="vacancyToEdit"
        @close="closeVacancyModal"
        @save="saveVacancy"
    />
  </div>
</template>

<script setup>
import VacancyDetails from './VacancyDetails.vue';
import VacancyModal from "@/components/VacancyModal.vue";
import VacancyService from '@/services/VacancyService.ts';
import { defineProps, ref, defineEmits, onMounted} from 'vue';

const showVacancyModal = ref(false);
const isEditMode = ref(false);
const vacancyToEdit = ref(null);

const userId = ref(localStorage.getItem('userId'));
const authToken = localStorage.getItem('authToken');

// кандидати, які відгукнулися на вакансію
const applicants = ref([]);

// отримана вакансія для перегляду
const vacancy = ref(props.vacancy);

/**
 * Пропс для отримання даних про вакансію з батьківського компонента
 */
const props = defineProps({
  vacancy: {
    type: Object,
    required: true
  }
});

const emit = defineEmits(['vacancy-updated', 'vacancy-deleted', 'open-candidate-profile']);

/**
 * Завантаження списку кандидатів при монтуванні компонента
 */
onMounted(async () => {
  await loadApplicants();
});

/**
 * Завантаження списку кандидатів, що відгукнулися
 * @returns {Promise<void>}
 */
const loadApplicants = async () => {
  if (authToken && props.vacancy?.vacId) {
    try {
      applicants.value = await VacancyService.getAppliedCandidates(props.vacancy.vacId, authToken);
    } catch (error) {
      console.error('Помилка при завантаженні кандидатів:', error);
    }
  }
}

/**
 * Для відкриття модального вікна редагування вакансії
 */
const openEditVacancyModal = () => {
  isEditMode.value = true;
  vacancyToEdit.value = { ...props.vacancy };
  showVacancyModal.value = true;
};

/**
 * Для закриття модального вікна редагування вакансії
 */
const closeVacancyModal = () => {
  showVacancyModal.value = false;
  vacancyToEdit.value = null;
};

/**
 * Метод для збереження вакансії, яку відредаговано
 * @param updatedVacancyData оновлені дані вакансії, які йдуть на сервер
 * @returns {Promise<void>} проміс після надсилання запиту
 */
const saveVacancy = async (updatedVacancyData) => {
  try {
    if (authToken) {
      const response = await VacancyService.updateVacancy(props.vacancy.vacId, updatedVacancyData, authToken);
      emit('vacancy-updated', response);

      closeVacancyModal();
      window.location.reload();
    } else {
      console.error('Токен авторизації відсутній.');
    }
  } catch (error) {
    console.error('Помилка при оновленні вакансії:', error);
  }
};

/**
 * Метод для видалення вакансії
 * @returns {Promise<void>} проміс після видалення вакансії
 */
const deleteVacancy = async () => {
  if (confirm(`Ви впевнені, що хочете видалити вакансію "${props.vacancy.title}"?`)) {
    try {
      if (authToken) {
        await VacancyService.deleteVacancy(props.vacancy.vacId, authToken);
        emit('vacancy-deleted-success', props.vacancy.vacId);
      } else {
        console.error('Токен авторизації відсутній.');
      }
    } catch (error) {
      console.error('Помилка при видаленні вакансії:', error);
    }
  }
};
</script>

<style scoped>
.vacancy-card-recruiter {
  border: 1px solid #ccc;
  border-radius: 8px;
  margin-bottom: 20px;
  padding: 15px;
  background-color: #fff;
  max-height: 60vh;
  overflow-y: auto;
}

.actions {
  padding: 10px 35%; /* Трохи зменшимо вертикальний відступ */
  display: flex;
  gap: 10px;
  justify-content: space-evenly;
  margin-bottom: 15px; /* Додамо відступ знизу для списку кандидатів */
}

.edit-button,
.delete-button {
  padding: 8px 12px; /* Зменшимо трохи відступи */
  border-radius: 5px;
  cursor: pointer;
  font-size: 14px; /* Зменшимо розмір шрифту */
}

.edit-button {
  background-color: #e0f7fa;
  color: #00acc1;
  border: 1px solid #b2ebf2;
}

.edit-button:hover {
  background-color: #b2ebf2;
}

.delete-button {
  background-color: #ffebee;
  color: #d32f2f;
  border: 1px solid #ef9a9a;
}

.delete-button:hover {
  background-color: #ef9a9a;
}

.applicants-list {
  margin-top: 15px; /* Зменшимо верхній відступ */
  padding: 10px;
  border-top: 1px solid #eee;
}

.applicants-list h3 {
  font-size: 16px; /* Зменшимо розмір шрифту заголовка */
  margin-bottom: 10px;
  color: #555;
}

.applicants-list ul {
  list-style: none;
  padding: 0;
}

.applicant-item {
  padding: 8px 0; /* Зменшимо вертикальні відступи */
  border-bottom: 1px dotted #ccc;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.applicant-item:last-child {
  border-bottom: none;
}

.applicant-info {
  display: flex;
  align-items: center;
  gap: 10px; /* Зменшимо відступ між елементами */
  cursor: pointer;
}

.applicant-photo {
  width: 40px; /* Зменшимо розмір фото */
  height: 40px;
  border-radius: 50%;
  overflow: hidden;
}

.applicant-photo img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.applicant-details {
  display: flex;
  flex-direction: column;
  font-size: 0.9em; /* Зменшимо розмір шрифту деталей */
}

.applicant-name {
  font-weight: bold;
  color: #333;
}

.applicant-email {
  color: #777;
}

.applicant-actions {
  display: flex;
  gap: 8px; /* Зменшимо відступ між кнопками */
}

.accept-button,
.reject-button {
  padding: 6px 10px; /* Зменшимо відступи кнопок */
  border-radius: 4px; /* Трохи менший радіус */
  cursor: pointer;
  font-size: 12px; /* Зменшимо розмір шрифту кнопок */
}

.accept-button {
  background-color: #4CAF50;
  color: white;
  border: none;
}

.accept-button:hover {
  background-color: #45a049;
}

.reject-button {
  background-color: #f44336;
  color: white;
  border: none;
}

.reject-button:hover {
  background-color: #d32f2f;
}
</style>
