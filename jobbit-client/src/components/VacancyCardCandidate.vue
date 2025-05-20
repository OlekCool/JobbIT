<template>
  <div class="vacancy-card-candidate">
    <VacancyDetails :vacancy="vacancy" />
    <div class="actions" role="group" aria-label="Дії з вакансією">
      <button class="save-button" @click="toggleSave" :class="{ 'saved': isSaved }" aria-label="Збереження вакансії">
        {{ isSaved ? 'Відмінити збереження' : 'Зберегти' }}
      </button>
      <button class="apply-button" @click="applyToVacancy" :class="{ 'applied': isApplied }" aria-label="Відгук на вакансію">
        {{ isApplied ? 'Відмінити відгук' : 'Відгукнутися' }}
      </button>
    </div>
  </div>
</template>

<script setup>
import { defineProps, ref, onMounted } from 'vue';
import VacancyDetails from './VacancyDetails.vue';
import VacancyService from "@/services/VacancyService.ts";

/**
 * Пропс для отримання даних про вакансію з батьківського компонента
 */
const props = defineProps({
  vacancy: {
    type: Object,
    required: true
  }
});

const candidateId = ref(localStorage.getItem('userId'));
const authToken = ref(localStorage.getItem('authToken'));
const isSaved = ref(false);
const isApplied = ref(false);

/**
 * Перевірка, чи вакансія збережена кандидатом
 * @returns {Promise<void>} проміс зі значенням того, чи збережено
 */
const checkIsSaved = async () => {
  if (candidateId.value && authToken.value && props.vacancy?.vacId) {
    try {
      isSaved.value = await VacancyService.isVacancySaved(props.vacancy.vacId, candidateId.value, authToken.value);
    } catch (error) {
      console.error('Помилка при перевірці збереженої вакансії', error);
    }
  }
}

/**
 * Перевірка, чи подана кандидатура на вакансію
 * @returns {Promise<void>} проміс зі значенням того, чи уже відгукувався кандидат
 */
const checkIsApplied = async () => {
  if (candidateId.value && authToken.value && props.vacancy?.vacId) {
    try {
      isApplied.value = await VacancyService.isVacancyApplied(props.vacancy.vacId, candidateId.value, authToken.value);
    } catch (error) {
      console.error('Помилка при перевірці відгуку на вакансію', error);
    }
  }
}

/**
 * Обробка кнопки задля збереження/видалення зі збережених вакансій кандидатом
 * @returns {Promise<void>} проміс з результатом збереження/видалення зі збережених
 */
const toggleSave = async () => {
  if (!candidateId.value || !authToken.value || !props.vacancy?.vacId) {
    console.warn('ID кандидата або токен відсутні, або вакансія не визначена.');
    return;
  }

  try {
    if (isSaved.value) {
      await VacancyService.unSaveVacancy(props.vacancy?.vacId, candidateId.value, authToken.value);
      isSaved.value = false;
    } else {
      await VacancyService.saveVacancy(props.vacancy?.vacId, candidateId.value, authToken.value);
      isSaved.value = true;
    }
  } catch (error) {
    console.error('Помилка при збереженні/відміні збереження:', error);
  }
};

/**
 * Обробка відгукування на вакансію
 * @returns {Promise<void>} проміс з результатом відгуку/скасування відгуку на вакансію
 */
const applyToVacancy = async () => {
  if (!candidateId.value || !authToken.value || !props.vacancy?.vacId) {
    console.warn('ID кандидата або токен відсутні, або вакансія не визначена.');
    return;
  }

  try {
    if (isApplied.value) {
      await VacancyService.unApplyVacancy(props.vacancy.vacId, candidateId.value, authToken.value);
      isApplied.value = false;
    } else {
      await VacancyService.applyVacancy(props.vacancy.vacId, candidateId.value, authToken.value);
      isApplied.value = true;
    }
  } catch (error) {
    console.error('Помилка при поданні/відкликанні кандидатури:', error);
  }
};

/**
 * Дії при монтуванні компонента
 */
onMounted(() => {
  checkIsSaved();
  checkIsApplied();
});
</script>

<style scoped>
.vacancy-card-candidate {
  border: 1px solid #ccc;
  border-radius: 8px;
  margin-bottom: 15px;
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

.save-button,
.apply-button {
  padding: 10px 15px;
  border-radius: 5px;
  cursor: pointer;
  font-size: 16px;
}

.save-button {
  background-color: #e0f7fa;
  color: #00acc1;
  border: 1px solid #b2ebf2;
  width: 100%;
}

.save-button:hover {
  background-color: #b2ebf2;
}

.apply-button {
  background-color: #aed581;
  color: #1b5e20;
  border: 1px solid #9ccc65;
  width: 100%;
}

.apply-button:hover {
  background-color: #9ccc65;
}

.save-button.saved {
   background-color: #9a9a9a;
   color: #1b5e20;
   border: 1px solid #9ccc65;
 }

.apply-button.applied {
  background-color: #9a9a9a;
  color: #1b5e20;
  border: 1px solid #9ccc65;
}
</style>
