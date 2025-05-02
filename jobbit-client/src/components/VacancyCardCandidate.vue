<template>
  <div class="vacancy-card-candidate">
    <VacancyDetails :vacancy="vacancy" />
    <div class="actions">
      <button class="save-button">Зберегти</button>
      <button class="apply-button">Відгукнутися</button>
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
</script>

<style scoped>
.vacancy-card-candidate {
  border: 1px solid #ccc;
  border-radius: 8px;
  margin-bottom: 15px;
  padding: 15px;
  background-color: #fff;
}

.actions {
  display: flex;
  gap: 10px;
  margin-top: 15px;
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
}

.save-button:hover {
  background-color: #b2ebf2;
}

.apply-button {
  background-color: #aed581;
  color: #1b5e20;
  border: 1px solid #9ccc65;
}

.apply-button:hover {
  background-color: #9ccc65;
}
</style>
