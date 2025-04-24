<template>
  <div class="dashboard">
    <NavbarCandidate />
    <div class="dashboard-body">
      <FiltersSection @filter-change="applyFilters" />
      <VacanciesLayout
          :vacancies="filteredVacancies"
          @search="applySearch"
      />
    </div>
  </div>
</template>

<script setup>
import NavbarCandidate from "../components/NavbarCandidate.vue";
import FiltersSection from "../components/FiltersSection.vue";
import VacanciesLayout from "../components/VacanciesLayout.vue";

import { ref, computed } from "vue";

/**
 * Це поки що для перевірки роботи фронтенду по відображенню вакансій
 * @type {Ref<UnwrapRef<[{location: string, id: number, title: string, salary: number, experience: number},{location: string, id: number, title: string, salary: number, experience: number},{location: string, id: number, title: string, salary: number, experience: number},{location: string, id: number, title: string, salary: number, experience: number},{location: string, id: number, title: string, salary: number, experience: number},null,null,null,null,null]>, UnwrapRef<[{location: string, id: number, title: string, salary: number, experience: number},{location: string, id: number, title: string, salary: number, experience: number},{location: string, id: number, title: string, salary: number, experience: number},{location: string, id: number, title: string, salary: number, experience: number},{location: string, id: number, title: string, salary: number, experience: number},null,null,null,null,null]> | [{location: string, id: number, title: string, salary: number, experience: number},{location: string, id: number, title: string, salary: number, experience: number},{location: string, id: number, title: string, salary: number, experience: number},{location: string, id: number, title: string, salary: number, experience: number},{location: string, id: number, title: string, salary: number, experience: number},null,null,null,null,null]>}
 */
const vacancies = ref([
  { id: 1, title: "Middle Frontend Dev.", salary: 40000, location: "Remote", experience: 2 },
  { id: 2, title: "Junior Backend Dev.", salary: 50000, location: "Partly Remote", experience: 1 },
  { id: 3, title: "Senior Fullstack Dev.", salary: 60000, location: "Office", experience: 5 },
  { id: 4, title: "Database Architect", salary: 45000, location: "Office", experience: 4 },
  { id: 5, title: "HR specialist", salary: 30000, location: "Remote", experience: 1 },
  { id: 6, title: "Web-designer", salary: 35000, location: "Office", experience: 2 },
  { id: 7, title: "Middle Fullstack Dev.", salary: 55000, location: "Partly Remote", experience: 3 },
  { id: 8, title: "DevOps Engineer", salary: 50000, location: "Office", experience: 4 },
  { id: 9, title: "System Administrator Dev.", salary: 30000, location: "Remote", experience: 2 },
  { id: 10, title: "Middle Java Developer.", salary: 45000, location: "Office", experience: 3 },
]);

const filters = ref({ location: "", salaryFrom: 0, minExperience: 0 });
const searchQuery = ref("");

/**
 * Застосування фільтрів
 * @param newFilters
 */
const applyFilters = (newFilters) => {
  filters.value = newFilters;
};

/**
 * Застосування пошукового запита
 * @param query
 */
const applySearch = (query) => {
  searchQuery.value = query.toLowerCase();
};

/**
 * Фільтрація за фільтрами і запитом
 * @type {ComputedRef<*>}
 */
const filteredVacancies = computed(() => {
  return vacancies.value.filter(vac => {
    const matchLocation = filters.value.location ? vac.location === filters.value.location : true;
    const matchSalary = filters.value.salaryFrom != null ? vac.salary >= filters.value.salaryFrom : true;
    const matchExperience = filters.value.minExperience != null ? vac.experience >= filters.value.minExperience : true;
    const matchSearch = searchQuery.value ? vac.title.toLowerCase().includes(searchQuery.value) : true;

    return matchLocation && matchSalary && matchExperience && matchSearch;
  });
});
</script>

<style>
.dashboard {
  display: flex;
  flex-direction: column;
  height: 96vh;
  border: 2px solid #ccc;
}
.dashboard-body {
  display: flex;
  flex: 1;
  border-top: 2px solid #aaa;
}
</style>
