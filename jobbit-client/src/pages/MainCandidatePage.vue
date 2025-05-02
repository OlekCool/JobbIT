<template>
  <div class="dashboard">
    <NavbarCandidate :candidateProfile="candidateProfileData"
                     :userPhoto="userPhotoUrl"
                     @show-profile="handleShowProfile"
                     @show-vacancies="handleShowVacancies"
                     @show-saved-vacancies="handleShowSavedVacancies"
                     @show-applied-vacancies="handleShowAppliedVacancies"
                     @show-notifications="handleShowNotifications" />
    <div class="dashboard-body">
      <FiltersSection v-if="!showProfile && showVacancies && !showSavedVacancies && !showAppliedVacancies
                      && !showNotifications" @filter-change="applyFilters" />
      <VacanciesLayout
          v-if="!showProfile && showVacancies && !showSavedVacancies && !showAppliedVacancies && !showNotifications"
          :vacancies="filteredVacancies"
          @search="applySearch"
      />
      <CandidateProfile v-if="showProfile && !showVacancies && !showSavedVacancies && !showAppliedVacancies
                        && !showNotifications"
                        :candidateProfile="candidateProfileData"
                        :candidateProjects="candidateProfileData.projects"
                        :userPhoto="userPhotoUrl"/>
    </div>
  </div>
</template>

<script setup>
import NavbarCandidate from "../components/NavbarCandidate.vue";
import FiltersSection from "../components/FiltersSection.vue";
import VacanciesLayout from "../components/VacanciesLayout.vue";
import CandidateProfile from "@/components/CandidateProfile.vue";
import ProfileService from "@/services/ProfileService.ts";
import VacancyService from "@/services/VacancyService.ts";
import { useRouter } from "vue-router";
import { computed, onMounted, ref } from "vue";

const router = useRouter();

const showProfile = ref(false);
const showVacancies = ref(true);
const showSavedVacancies = ref(false);
const showAppliedVacancies = ref(false);
const showNotifications = ref(false);

const userId = ref(localStorage.getItem('userId'));
const authToken = localStorage.getItem('authToken');
const candidateProfileData = ref(null);
const userPhotoUrl = ref('/files/userPhotos/userDemo.png');
const filters = ref({ remote: "", fulltime: [], level_eng: [], set_salary: null, min_exp: null, });
const searchQuery = ref("");
const vacancies = ref([]);

onMounted(async () => {
  if (!userId || !authToken) {
    await router.push('/login');
    return;
  }

  try {
    const profileResponse = await ProfileService.getProfileCandidate(userId.value, authToken);
    candidateProfileData.value = profileResponse.data;

    const photoPath = await ProfileService.getCandidateProfilePhoto(userId.value, authToken);
    if (photoPath) {
      userPhotoUrl.value = `/${photoPath}`;
    }

    if (router.currentRoute.value.path.startsWith('/candidate-dash/profile/')) {
      showProfile.value = true;
      showVacancies.value = false;
      showSavedVacancies.value = false;
      showAppliedVacancies.value = false;
      showNotifications.value = false;
    } else if (router.currentRoute.value.path === '/candidate-dash/search') {
      showProfile.value = false;
      showVacancies.value = true;
      showSavedVacancies.value = false;
      showAppliedVacancies.value = false;
      showNotifications.value = false;
    } else if (router.currentRoute.value.path === '/candidate-dash/saved') {
      showProfile.value = false;
      showVacancies.value = false;
      showSavedVacancies.value = true;
      showAppliedVacancies.value = false;
      showNotifications.value = false;
    } else if (router.currentRoute.value.path === '/candidate-dash/applied') {
      showProfile.value = false;
      showVacancies.value = false;
      showSavedVacancies.value = false;
      showAppliedVacancies.value = true;
      showNotifications.value = false;
    } else if (router.currentRoute.value.path === '/candidate-dash/notifications') {
      showProfile.value = false;
      showVacancies.value = false;
      showSavedVacancies.value = false;
      showAppliedVacancies.value = false;
      showNotifications.value = true;
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
  showVacancies.value = false;
  showSavedVacancies.value = false;
  showAppliedVacancies.value = false;
  showNotifications.value = false;
  router.push(`/candidate-dash/profile/${userId.value}`);
};

/**
 * Обробник події відображення пошуку вакансій
 */
const handleShowVacancies = () => {
  showProfile.value = false;
  showVacancies.value = true;
  showSavedVacancies.value = false;
  showAppliedVacancies.value = false;
  showNotifications.value = false;
  router.push('/candidate-dash/search');
};

/**
 * Обробник події відображення збережених вакансій
 */
const handleShowSavedVacancies = () => {
  showProfile.value = false;
  showVacancies.value = false;
  showSavedVacancies.value = true;
  showAppliedVacancies.value = false;
  showNotifications.value = false;
  router.push('/candidate-dash/saved');
};

/**
 * Обробник події відображення вакансій, на які відгукнувся
 */
const handleShowAppliedVacancies = () => {
  showProfile.value = false;
  showVacancies.value = false;
  showSavedVacancies.value = false;
  showAppliedVacancies.value = true;
  showNotifications.value = false;
  router.push('/candidate-dash/applied');
};

/**
 * Обробник події відображення сповіщень
 */
const handleShowNotifications = () => {
  showProfile.value = false;
  showVacancies.value = false;
  showSavedVacancies.value = false;
  showAppliedVacancies.value = false;
  showNotifications.value = true;
  router.push('/candidate-dash/notifications');
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
