<template>
  <div class="vacancy-list">
    <VacancyItem class="vacancy-item"
        v-for="vacancy in vacancies"
        :key="vacancy.id"
        :vacancy="vacancy"
        @select-vacancy="$emit('select-vacancy', vacancy)"
    />
  </div>
</template>

<script setup>
import VacancyItem from "../components/VacancyItem.vue";
import { defineProps, defineEmits } from "vue";

/**
 * Пропс для отримання даних про вакансію з батьківського компонента
 */
const props = defineProps({
  vacancies: Array
});

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
</style>
