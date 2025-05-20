<template>
  <div class="vacancy-list" role="list">
    <VacancyItem class="vacancy-item"
        v-for="vacancy in vacancies"
        :key="vacancy.id"
        :vacancy="vacancy"
        @select-vacancy="$emit('select-vacancy', vacancy)"
    />
    <div v-if="vacancies && vacancies.length === 0" class="empty-list-message" role="status" aria-live="polite">
      {{ emptyListMessage }}
    </div>
  </div>
</template>

<script setup>
import VacancyItem from "../components/VacancyItem.vue";
import { defineProps, defineEmits } from "vue";

// eslint-disable-next-line
const props = defineProps({
  vacancies: {
    type: Array,
    default: () => []
  },
  emptyListMessage: {
    type: String,
    default: 'Вакансій поки не знайдено... Спробуйте додати до збережених, або відгукнутися на вакансію'
  }
});

// eslint-disable-next-line
const emit = defineEmits(['select-vacancy']);
</script>

<style scoped>
.vacancy-list {
  flex: 1;
  overflow-y: auto;
  padding: 10px;
  border: 2px solid #ddd;
  border-radius: 6px;
  background-color: #fefefe;
  scroll-behavior: smooth;
}

.vacancy-item {
  margin-bottom: 10px;
}

.vacancy-list::-webkit-scrollbar {
  width: 8px;
}

.vacancy-list::-webkit-scrollbar-thumb {
  background-color: #bbb;
  border-radius: 4px;
}

.vacancy-list:hover::-webkit-scrollbar-thumb {
  background-color: #888;
}

.empty-list-message {
  text-align: center;
  font-size: 1.1em;
  color: #777;
  padding: 20px;
}
</style>
