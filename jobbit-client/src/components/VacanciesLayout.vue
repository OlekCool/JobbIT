<template>
  <div class="vacancies-holder">
    <section class="search-container" aria-labelledby="search-heading">
      <h2 id="search-heading" class="visually-hidden">Пошук вакансій</h2>
      <SearchVacancy @search="handleSearch" />
    </section>
    <VacanciesList :vacancies="vacancies" @select-vacancy="$emit('select-vacancy', $event)" />
  </div>
</template>

<script setup>
import SearchVacancy from "../components/SearchVacancy.vue";
import VacanciesList from "../components/VacanciesList.vue";
import { defineProps, defineEmits } from "vue";

// eslint-disable-next-line
const props = defineProps({
  vacancies: Array
});

const emit = defineEmits(["search", "select-vacancy"]);

/**
 * Емітування події пошуку вакансій за запитом
 */
function handleSearch(query) {
  emit("search", query);
}
</script>

<style scoped>
.vacancies-holder {
  width: 80%;
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

.visually-hidden {
  position: absolute;
  width: 1px;
  height: 1px;
  margin: -1px;
  padding: 0;
  overflow: hidden;
  clip: rect(0, 0, 0, 0);
  border: 0;
}
</style>
