<template>
  <div class="vacancy-card-recruiter">
    <VacancyDetails :vacancy="vacancy" />
    <div class="actions">
      <button class="edit-button">Редагувати</button>
      <button class="delete-button">Видалити</button>
    </div>

    <div class="applicants-list">
      <h3>Кандидати, які відгукнулися: </h3>
      <ul>
      </ul>
      <p v-if="false">Поки що немає кандидатів, які відгукнулися.</p>
    </div>
  </div>
</template>

<script setup>
import VacancyDetails from './VacancyDetails.vue';
import { defineProps, ref } from 'vue';

const props = defineProps({
  vacancy: {
    type: Object,
    required: true
  }
});

const vacancy = ref(props.vacancy);

// Тут можна буде додати логіку для кнопок "Редагувати" та "Видалити"
</script>

<style scoped>
.vacancy-card-recruiter {
  border: 1px solid #ccc;
  border-radius: 8px;
  margin-bottom: 20px;
  padding: 15px;
  background-color: #fff;
}

.actions {
  display: flex;
  gap: 10px;
  margin-top: 15px;
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
