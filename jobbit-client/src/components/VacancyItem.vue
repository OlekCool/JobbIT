<template>
  <div class="vacancy-card" role="region" aria-labelledby="vacancy-title">
    <h3 class="vacancy-title" id="vacancy-title">{{ vacancy.title }}</h3>
    <div class="vacancy-salary" aria-label="Заробітна плата">{{ vacancy.setSalary }} грн.</div>
    <div class="vacancy-remote" aria-label="Формат роботи">{{ vacancy.remote }}</div>
    <div class="vacancy-experience" aria-label="Необхідний досвід">Досвід: {{ vacancy.minExp }}+ років</div>
    <button class="view-details-btn" @click="$emit('select-vacancy', vacancy)"
            aria-label="Переглянути деталі вакансії: {{ vacancy.title }}">Переглянути детальніше</button>
  </div>
</template>

<script setup>
import { defineProps, defineEmits } from "vue";

// eslint-disable-next-line
const props = defineProps({
  vacancy: {
    type: Object,
    required: true
  }
});

// eslint-disable-next-line
const emit = defineEmits(['select-vacancy']);
</script>

<style scoped>
.vacancy-card {
  display: grid;
  grid-template-columns: 2fr 1fr 1.5fr 1fr auto;
  align-items: center;
  padding: 10px;
  border: 2px solid #ddd;
  gap: 10px;
  border-radius: 5px;
  background-color: #f9f9f9;
  cursor: pointer;
  transition: background-color 0.2s ease-in-out, box-shadow 0.2s ease-in-out;
}

.vacancy-card:hover {
  background-color: #f0f0f0;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  border-color: #ccc;
}

.vacancy-title {
  font-weight: bold;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  text-align: left;
  font-size: 1vw;
}

.vacancy-salary,
.vacancy-remote,
.vacancy-experience{
  text-align: left;
}

.view-details-btn {
  padding: 8px 15px;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.9rem;
  transition: background-color 0.3s ease;
}

.view-details-btn:hover {
  background-color: #29752d;
}
</style>
