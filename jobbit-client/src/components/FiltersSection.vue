<template>
  <aside role="search" class="filters">
    <h2>Фільтри</h2>

    <div class="filter-group">
      <p class="filter-label">Віддалена робота:</p>
      <label><input type="radio" value="" v-model="filters.remote" /> Будь-яка </label>
      <label><input type="radio" value="Так" v-model="filters.remote" /> Так </label>
      <label><input type="radio" value="Ні" v-model="filters.remote" /> Ні </label>
      <label><input type="radio" value="Частково віддалено" v-model="filters.remote" /> Частково </label>
    </div>

    <div class="filter-group">
      <p class="filter-label">Тип зайнятості:</p>
      <label><input type="checkbox" v-model="filters.fulltime" :value="true" /> Повна </label>
      <label><input type="checkbox" v-model="filters.fulltime" :value="false" /> Часткова </label>
    </div>

    <div class="filter-group">
      <p class="filter-label">Рівень англійської:</p>
      <label><input type="checkbox" v-model="filters.level_eng" value="A1" /> A1</label>
      <label><input type="checkbox" v-model="filters.level_eng" value="A2" /> A2</label>
      <label><input type="checkbox" v-model="filters.level_eng" value="B1" /> B1</label>
      <label><input type="checkbox" v-model="filters.level_eng" value="B2" /> B2</label>
      <label><input type="checkbox" v-model="filters.level_eng" value="C1" /> C1</label>
      <label><input type="checkbox" v-model="filters.level_eng" value="C2" /> C2</label>
    </div>

    <div class="filter-group">
      <label class="filter-label" for="salary">Зарплата від:</label>
      <input id="salary" type="number" v-model.number="filters.set_salary" placeholder="Наприклад, 25000" />
    </div>

    <div class="filter-group">
      <label class="filter-label" for="experience">Мінімальний досвід (роки):</label>
      <input id="experience" type="number" min="0" max="50" v-model.number="filters.min_exp" placeholder="0" />
    </div>
  </aside>
</template>

<script setup>
import { ref, watch, defineEmits } from "vue";

/**
 * Збереження значень фільтрів у реактивну змінну
 */
const filters = ref({
  remote: "",
  fulltime: [],
  level_eng: [],
  set_salary: null,
  min_exp: null,
});

const emit = defineEmits(["filter-change"]);

/**
 * Реагує на зміну фільтрів для відображення вакансій
 */
watch(filters, () => {
  emit("filter-change", { ...filters.value });
}, { deep: true });
</script>

<style scoped>
.filters {
  width: 20.05%;
  height: 78vh;
  padding: 0 20px;
  border-right: 2px solid #ccc;
  background-color: #f9f9f9;
  box-sizing: border-box;
  margin: 2px;
  overflow-y: auto;
}

h2 {
  margin-bottom: 20px;
  font-size: 20px;
  color: #333;
}

.filter-group {
  margin-bottom: 20px;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

.filter-label {
  font-weight: bold;
  margin-bottom: 5px;
  text-align: left;
}

label {
  display: block;
  margin-bottom: 4px;
  font-size: 14px;
  text-align: left;
}

input[type="number"] {
  padding: 8px;
  font-size: 14px;
  border-radius: 6px;
  border: 1px solid #aaa;
  width: 100%;
  box-sizing: border-box;
}
</style>
