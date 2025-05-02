<template>
  <div class="dashboard">
    <NavbarRecruiter :recruiterProfile="recruiterProfileData"
                     :userPhoto="userPhotoUrl"
                     @show-profile="handleShowProfile"
                     @show-all-vacancies="handleShowAllVacancies"
                     @show-my-vacancies="handleShowMyVacancies"/>
    <div class="dashboard-body">
      <FiltersSection v-if="!showProfile && !showMyVacancies && showAllVacancies" @filter-change="applyFilters" />
      <VacanciesLayout
          v-if="!showProfile && !showMyVacancies && showAllVacancies"
          :vacancies="filteredVacancies"
          @search="applySearch"
      />
      <RecruiterProfile v-if="showProfile && !showAllVacancies && !showMyVacancies"
                        :recruiterProfile="recruiterProfileData"
                        :userPhoto="userPhotoUrl" />
    </div>
  </div>
</template>

<script setup>
import NavbarRecruiter from "@/components/NavbarRecruiter.vue";
import RecruiterProfile from "@/components/RecruiterProfile.vue";
import ProfileService from "@/services/ProfileService.ts";
import VacancyService from "@/services/VacancyService.ts";
import VacanciesLayout from "@/components/VacanciesLayout.vue";
import FiltersSection from "@/components/FiltersSection.vue";
import { useRouter } from "vue-router";
import { computed, onMounted, ref } from "vue";

const router = useRouter();

const showProfile = ref(false);
const showAllVacancies = ref(false);
const showMyVacancies = ref(true);

const userPhotoUrl = ref('/files/userPhotos/userDemo.png');
const userId = ref(localStorage.getItem('userId'));
const authToken = localStorage.getItem('authToken');
const recruiterProfileData = ref(null);

const filters = ref({ remote: "", fulltime: [], level_eng: [], set_salary: null, min_exp: null, });
const searchQuery = ref("");
const vacancies = ref([]);

onMounted(async () => {
  if (!userId || !authToken) {
    await router.push('/login');
    return;
  }

  try {
    const profileResponse = await ProfileService.getProfileRecruiter(userId.value, authToken);
    recruiterProfileData.value = profileResponse.data;

    const photoPath = await ProfileService.getRecruiterProfilePhoto(userId.value, authToken);
    if (photoPath) {
      userPhotoUrl.value = `/${photoPath}`;
    }

    if (router.currentRoute.value.path.startsWith('/recruiter-dash/profile')) {
      showProfile.value = true;
      showAllVacancies.value = false;
      showMyVacancies.value = false;
    } else if (router.currentRoute.value.path === '/recruiter-dash/my-vacancies') {
      showAllVacancies.value = false;
      showMyVacancies.value = true;
      showProfile.value = false;
    } else if (router.currentRoute.value.path === '/recruiter-dash/all-vacancies') {
      showAllVacancies.value = true;
      showMyVacancies.value = false;
      showProfile.value = false;
    }
  } catch (error) {
    console.error('Помилка при завантаженні даних на головній сторінці кандидата:', error);
  }

  try {
    vacancies.value = await VacancyService.getAllVacancies();
  } catch (error) {
    console.error('Помилка при завантаженні вакансій з бекенду:', error);
  }
});

/**
 * Обробник події відображення профілю
 */
const handleShowProfile = () => {
  showProfile.value = true;
  showAllVacancies.value = false;
  showMyVacancies.value = false;
};

/**
 * Обробник події відображення всіх вакансій
 */
const handleShowAllVacancies = () => {
  showProfile.value = false;
  showAllVacancies.value = true;
  showMyVacancies.value = false;
  router.push('/recruiter-dash/all-vacancies');
};

/**
 * Обробник події відображення всіх вакансій
 */
const handleShowMyVacancies = () => {
  showProfile.value = false;
  showAllVacancies.value = false;
  showMyVacancies.value = true;
  router.push('/recruiter-dash/my-vacancies');
};

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
 * Фільтрація за фільтрами та запитом
 * @type {ComputedRef<*>}
 */
const filteredVacancies = computed(() => {
  return vacancies.value.filter(vac => {
    const matchRemote = filters.value.remote ? vac.remote === filters.value.remote : true;
    const matchFulltime = filters.value.fulltime.length > 0 ? filters.value.fulltime.includes(vac.fulltime) : true;
    const matchLevelEng = filters.value.level_eng.length > 0 ? filters.value.level_eng.includes(vac.levelEng) : true;
    const matchSalary = filters.value.set_salary != null ? vac.setSalary >= filters.value.set_salary : true;
    const matchExperience = filters.value.min_exp != null ? vac.minExp >= filters.value.min_exp : true;
    const matchSearch = searchQuery.value ? vac.title.toLowerCase().includes(searchQuery.value) : true;

    return matchRemote && matchFulltime && matchLevelEng && matchSalary && matchExperience && matchSearch;
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
