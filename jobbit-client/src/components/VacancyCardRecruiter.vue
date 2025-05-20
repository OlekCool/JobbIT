<template>
  <article class="vacancy-card-recruiter" aria-labelledby="vacancy-heading">
    <h2 id="vacancy-heading" class="visually-hidden">Деталі вакансії: {{ vacancy.title }}</h2>
    <VacancyDetails :vacancy="vacancy" />
    <div class="actions" role="group" aria-label="Дії з вакансією">
      <button class="edit-button" @click="openEditVacancyModal">Редагувати</button>
      <button class="delete-button" @click="deleteVacancy">Видалити</button>
    </div>

    <div class="applicants-list" aria-labelledby="applicants-heading">
      <h3 id="applicants-heading">Кандидати, які відгукнулися:</h3>
      <ul v-if="applicants.length > 0" role="list">
        <li v-for="applicant in applicants" :key="applicant.id" class="applicant-item" role="listitem">
          <div class="applicant-info" @click="$emit('open-candidate-profile', applicant.id)"
               tabindex="0" role="link" aria-label="`Переглянути профіль кандидата">
            <div class="applicant-photo" v-if="applicant.photo">
              <img :src="'/' + applicant.photo" alt="Фото кандидата">
            </div>
            <div class="applicant-details">
              <span class="applicant-name">{{ applicant.firstName }} {{ applicant.lastName }}</span>
              <span class="applicant-email">{{ applicant.email }}</span>
            </div>
          </div>
          <div class="applicant-actions" role="group" aria-label="Дії для кандидата">
            <button class="accept-button" @click="openSendMessageModal(applicant.id, true)"
                    aria-label="Надіслати повідомлення про запрошення на співбесіду">Прийняти</button>
            <button class="reject-button" @click="openSendMessageModal(applicant.id, false)"
                    aria-label="Надіслати повідомлення про відхилення кандидата">Відхилити</button>
          </div>
        </li>
      </ul>
      <p v-else role="status" aria-live="polite">Поки що немає кандидатів, які відгукнулися.</p>
    </div>

    <VacancyModal
        v-if="showVacancyModal"
        :is-open="showVacancyModal"
        :is-edit-mode="isEditMode"
        :vacancy-to-edit="vacancyToEdit"
        @close="closeVacancyModal"
        @save="saveVacancy"
    />

    <SendMessageModal
        v-if="showSendMessageModal"
        :title="isAccepting ? 'Надіслати повідомлення про прийняття' : 'Надіслати повідомлення про відхилення'"
        @close="showSendMessageModal = false; currentApplicantId = null; isAccepting = false;"
        @send="sendMessageToApplicant"
    />
  </article>
</template>

<script setup>
import VacancyDetails from './VacancyDetails.vue';
import VacancyModal from "@/components/VacancyModal.vue";
import SendMessageModal from './SendMessageModal.vue';
import VacancyService from '@/services/VacancyService.ts';
import { defineProps, ref, defineEmits, onMounted, watch} from 'vue';

const showVacancyModal = ref(false);
const isEditMode = ref(false);
const vacancyToEdit = ref(null);

const authToken = ref(localStorage.getItem('authToken'));

// кандидати, які відгукнулися на вакансію
const applicants = ref([]);

// дані про повідомлення, кому та чи приймаємо
const showSendMessageModal = ref(false);
const currentApplicantId = ref(null);
const isAccepting = ref(false);

/**
 * Пропс для отримання даних про вакансію з батьківського компонента
 */
const props = defineProps({
  vacancy: {
    type: Object,
    required: true
  }
});

// отримана вакансія для перегляду
const vacancy = ref(props.vacancy);

const emit = defineEmits(['vacancy-updated', 'vacancy-deleted', 'open-candidate-profile']);

/**
 * Спостерігання за тим, чи змінилися дані для відображення
 */
watch(
    () => props.vacancy,
    (newVacancy) => {
      vacancy.value = newVacancy;
    },
    { deep: true }
);

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
  if (authToken.value && props.vacancy?.vacId) {
    try {
      applicants.value = await VacancyService.getAppliedCandidates(props.vacancy.vacId, authToken.value);
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

const openSendMessageModal = (applicantId, accept) => {
  currentApplicantId.value = applicantId;
  isAccepting.value = accept;
  showSendMessageModal.value = true;
};

/**
 * Метод для збереження вакансії, яку відредаговано
 * @param updatedVacancyData оновлені дані вакансії, які йдуть на сервер
 * @returns {Promise<void>} проміс після надсилання запиту
 */
const saveVacancy = async (updatedVacancyData) => {
  try {
    if (authToken.value) {
      const response = await VacancyService.updateVacancy(props.vacancy.vacId, updatedVacancyData, authToken.value);
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
      if (authToken.value) {
        await VacancyService.deleteVacancy(props.vacancy.vacId, authToken.value);
        emit('vacancy-deleted-success', props.vacancy.vacId);
        window.location.reload();
      } else {
        console.error('Токен авторизації відсутній.');
      }
    } catch (error) {
      console.error('Помилка при видаленні вакансії:', error);
    }
  }
};

const sendMessageToApplicant = async (message) => {
  if (authToken.value && currentApplicantId.value && props.vacancy?.vacId) {
    try {
      if (isAccepting.value) {
        await VacancyService.acceptCandidate(
            props.vacancy.vacId,
            currentApplicantId.value,
            message,
            authToken.value
        );
        console.log(`Кандидата з ID ${currentApplicantId.value} прийнято. Повідомлення: ${message}`);
      } else {
        await VacancyService.rejectCandidate(
            props.vacancy.vacId,
            currentApplicantId.value,
            message,
            authToken.value
        );
        console.log(`Кандидата з ID ${currentApplicantId.value} відхилено. Повідомлення: ${message}`);
      }

      await loadApplicants();
    } catch (error) {
      console.error('Помилка при оновленні статусу:', error);
    } finally {
      showSendMessageModal.value = false;
      currentApplicantId.value = null;
      isAccepting.value = false;
    }
  } else {
    console.warn('Відсутній токен, ID кандидата або ID вакансії.');
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
