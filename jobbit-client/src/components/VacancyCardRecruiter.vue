<template>
  <div class="vacancy-card-recruiter">
    <VacancyDetails :vacancy="vacancy" />
    <div class="actions">
      <button class="edit-button" @click="openEditVacancyModal">Редагувати</button>
      <button class="delete-button" @click="deleteVacancy">Видалити</button>
    </div>

    <div class="applicants-list">
      <h3>Кандидати, які відгукнулися: </h3>
      <ul>
      </ul>
      <p v-if="false">Поки що немає кандидатів, які відгукнулися.</p>
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
import { defineProps, ref, defineEmits} from 'vue';

/**
 * Пропс для отримання даних про вакансію з батьківського компонента
 */
const props = defineProps({
  vacancy: {
    type: Object,
    required: true
  }
});

const emit = defineEmits(['vacancy-updated', 'vacancy-deleted']);

const showVacancyModal = ref(false);
const isEditMode = ref(false);
const vacancyToEdit = ref(null);
const userId = ref(localStorage.getItem('userId'));
const authToken = localStorage.getItem('authToken');

const vacancy = ref(props.vacancy);

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
  height: 80%;
  overflow-y: auto;
}

.actions {
  padding: 0 35%;
  display: flex;
  gap: 10px;
  justify-content: space-evenly;
}

.edit-button,
.delete-button {
  padding: 10px 15px;
  border-radius: 5px;
  cursor: pointer;
  font-size: 16px;
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
  margin-top: 20px;
  padding: 10px;
  border-top: 1px solid #eee;
}

.applicants-list h3 {
  font-size: 18px;
  margin-bottom: 10px;
  color: #555;
}

.applicants-list ul {
  list-style: none;
  padding: 0;
}

.applicants-list li {
  padding: 8px 0;
  border-bottom: 1px dotted #ccc;
}

.applicants-list li:last-child {
  border-bottom: none;
}
</style>
