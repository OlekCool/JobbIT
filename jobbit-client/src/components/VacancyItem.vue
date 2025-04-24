<template>
  <div class="vacancy-card">
    <div class="vacancy-title">{{ vacancy.title }}</div>
    <div class="vacancy-salary">{{ vacancy.salary }}</div>
    <div class="vacancy-location">{{ vacancy.location }}</div>
    <div class="btns">
      <button>L</button>
      <button>A</button>
    </div>
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
  grid-template-columns: 2fr 1fr 1.5fr auto;
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
.vacancy-location {
  text-align: left;
}

.btns {
  display: flex;
  gap: 5px;
  justify-content: flex-end;
}
</style>
