<template>
  <div class="vacancy-card">
    <div class="vacancy-title">{{ vacancy.title }}</div>
    <div class="vacancy-salary">{{ vacancy.setSalary }} грн.</div>
    <div class="vacancy-remote">{{ vacancy.remote }}</div>
    <div class="vacancy-experience">Досвід: {{ vacancy.minExp }}+ років</div>
    <button class="view-details-btn">Переглянути детальніше</button>
  </div>
</template>

<script setup>
import { defineProps } from "vue";

const props = defineProps({
  vacancy: Object
});
</script>

<style scoped>
.vacancy-card {
  display: grid;
  grid-template-columns: 2fr 1fr 1.5fr 1fr auto;
  align-items: center;
  padding: 10px;
  border: 2px solid #ddd;
  gap: 10px;
}

.vacancy-title {
  font-weight: bold;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  text-align: left;
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
