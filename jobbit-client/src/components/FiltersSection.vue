<template>
  <aside role="search" class="filters">
    <h2>Фільтри</h2>

    <div class="filter-group">
      <p class="filter-label">Розташування:</p>
      <label><input type="radio" value="" v-model="selectedLocation" /> Будь-який</label>
      <label><input type="radio" value="Remote" v-model="selectedLocation" /> Remote</label>
      <label><input type="radio" value="Office" v-model="selectedLocation" /> Office</label>
      <label><input type="radio" value="Partly Remote" v-model="selectedLocation" /> Partly Remote</label>
    </div>

    <div class="filter-group">
      <label class="filter-label" for="salary">Зарплата від:</label>
      <input id="salary" type="number" v-model.number="minSalary" placeholder="Наприклад, 25000" />
    </div>

    <div class="filter-group">
      <label class="filter-label" for="experience">Мінімальний досвід (роки):</label>
      <input id="experience" type="number" min="0" max="50" v-model.number="minExperience" placeholder="0" />
    </div>
  </aside>
</template>

<script setup>
import { ref, watch } from "vue";

const selectedLocation = ref("");
const minSalary = ref(null);
const minExperience = ref(null);

const emit = defineEmits(["filter-change"]);

/**
 * Реагує на зміну фільтрів для відображення вакансій
 */
watch([selectedLocation, minSalary, minExperience], () => {
  emit("filter-change", {
    location: selectedLocation.value,
    salaryFrom: minSalary.value,
    minExperience: minExperience.value,
  });
});
</script>

<style scoped>
.filters {
  width: 20%;
  height: 78vh;
  padding: 0 20px;
  border-right: 2px solid #ccc;
  background-color: #f9f9f9;
  box-sizing: border-box;
  margin: 2px;
}

h3 {
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
