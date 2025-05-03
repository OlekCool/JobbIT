<template>
  <div class="vacancy-card-recruiter">
    <VacancyDetails :vacancy="vacancy" />
  </div>
</template>

<script setup>
import VacancyDetails from './VacancyDetails.vue';
import { defineProps, ref } from 'vue';

/**
 * Пропс для отримання даних про вакансію з батьківського компонента
 */
const props = defineProps({
  vacancy: {
    type: Object,
    required: true
  }
});

const vacancy = ref(props.vacancy);
</script>

<style scoped>
.vacancy-card-recruiter {
  border: 1px solid #ccc;
  border-radius: 8px;
  margin-bottom: 20px;
  padding: 15px;
  background-color: #fff;
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
