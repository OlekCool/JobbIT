<template>
  <div class="vacancies-holder">
    <div class="search-container">
      <SearchVacancy @search="handleSearch" />
    </div>
    <VacanciesList :vacancies="vacancies" />
  </div>
</template>

<script setup>
import SearchVacancy from "../components/SearchVacancy.vue";
import VacanciesList from "../components/VacanciesList.vue";
import { defineProps, defineEmits } from "vue";

const props = defineProps({
  vacancies: Array
});

const emit = defineEmits(["search"]);

/**
 * Здійснення пошуку вакансій за запитом
 */
function handleSearch(query) {
  emit("search", query);
}
</script>

<style scoped>
.vacancies-holder {
  width: 60%;
  height: 80vh;
  padding: 20px 60px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  box-sizing: border-box;
  overflow: hidden;
}

.search-container {
  flex: 0 0 auto;
  height: 60px;
  margin-bottom: 10px;
}
</style>
