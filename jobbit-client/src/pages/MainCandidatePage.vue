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
                      && !showNotifications && !selectedVacancyId" @filter-change="applyFilters" />
      <VacanciesLayout
          v-if="!showProfile && !showNotifications && !selectedVacancyId"
          :vacancies="filteredVacancies"
          @search="applySearch"
          @select-vacancy="handleSelectVacancy"
      />
      <CandidateProfile v-if="showProfile && !showVacancies && !showSavedVacancies && !showAppliedVacancies
                        && !showNotifications && !selectedVacancyId"
                        :candidateProfile="candidateProfileData"
                        :candidateProjects="candidateProfileData.projects"
                        :userPhoto="userPhotoUrl"/>
      <div v-if="selectedVacancyId" class="vacancy-details-container">
        <component
            :is="getDetailedVacancyComponent()"
            :vacancy="selectedVacancy"
        />
        <button @click="closeVacancyDetails">Назад до списку</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import NavbarCandidate from "../components/NavbarCandidate.vue";
import FiltersSection from "../components/FiltersSection.vue";
import VacanciesLayout from "../components/VacanciesLayout.vue";
import CandidateProfile from "@/components/CandidateProfile.vue";
import VacancyCardCandidate from "@/components/VacancyCardCandidate.vue";
import ProfileService from "@/services/ProfileService.ts";
import VacancyService from "@/services/VacancyService.ts";
import { useRouter, useRoute  } from "vue-router";
import { computed, onMounted, ref, watch } from "vue";

const router = useRouter();
const route = useRoute();

//змінні сану відображення сторінки
const showProfile = ref(false);
const showVacancies = ref(true);
const showSavedVacancies = ref(false);
const showAppliedVacancies = ref(false);
const showNotifications = ref(false);

//отримані значення з локального сховища
const userId = ref(localStorage.getItem('userId'));
const authToken = localStorage.getItem('authToken');

//змінні для зберігання фото та даних профілю
const candidateProfileData = ref(null);
const userPhotoUrl = ref('/files/userPhotos/userDemo.png');

//фільтри, пошуковий запит, видані вакансії для завантаження у списку
const filters = ref({ remote: "", fulltime: [], level_eng: [], set_salary: null, min_exp: null, });
const searchQuery = ref("");
const vacancies = ref([]);

//обрана вакансія для перегляду
const selectedVacancyId = ref(null);
const selectedVacancy = ref(null);
const loadingVacancies = ref(false);

/**
 * Асинхронний метод для загрузки вакансій, залежно від url-адреси сторінки
 * @returns {Promise<void>}
 */
const loadInitialVacancies = async () => {
  loadingVacancies.value = true;

  try {
    if (route.path.startsWith('/candidate-dash/profile/')) {
      vacancies.value = null;

      showVacancies.value = false;
      showSavedVacancies.value = false;
      showAppliedVacancies.value = false;
      showNotifications.value = false;
      showProfile.value = true;
    } else if (route.path === '/candidate-dash/search') {
      vacancies.value = await VacancyService.getAllVacancies();
      console.log("Вакансії: " + vacancies.value)

      showVacancies.value = true;
      showSavedVacancies.value = false;
      showAppliedVacancies.value = false;
      showNotifications.value = false;
      showProfile.value = false;
    } else if (route.path === '/candidate-dash/saved') {
      vacancies.value = await VacancyService.watchSaved(userId.value, authToken);

      showVacancies.value = false;
      showSavedVacancies.value = true;
      showAppliedVacancies.value = false;
      showNotifications.value = false;
      showProfile.value = false;
    } else if (route.path === '/candidate-dash/applied') {
      vacancies.value = await VacancyService.watchApplied(userId.value, authToken);

      showVacancies.value = false;
      showSavedVacancies.value = false;
      showAppliedVacancies.value = true;
      showNotifications.value = false;
      showProfile.value = false;
    } else if (route.path === '/candidate-dash/notifications') {
      vacancies.value = null;

      showVacancies.value = false;
      showSavedVacancies.value = false;
      showAppliedVacancies.value = false;
      showNotifications.value = true;
      showProfile.value = false;
    } else {
      vacancies.value = await VacancyService.getAllVacancies();

      showVacancies.value = true;
      showSavedVacancies.value = false;
      showAppliedVacancies.value = false;
      showNotifications.value = false;
      showProfile.value = false;
    }
  } catch (error) {
    console.error('Помилка при отриманні вакансій:', error);
  } finally {
    loadingVacancies.value = false;
  }
}

/**
 * Обробка монтування компонента, де завантажується профіль, фото кандидата, вакансії, та перевірка, чи є
 * параметр vac_id в адресі (щоб отримати інформацію про вакансію)
 */
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

    await loadInitialVacancies();

    if (route.query.vac_id && vacancies.value.length > 0) {
      const searchId = Number(route.query.vac_id);
      selectedVacancy.value = vacancies.value.find(vac => vac.vacId === searchId);
      selectedVacancyId.value = route.query.vac_id;
    }
  } catch (error) {
    console.error('Помилка при завантаженні даних на головній сторінці кандидата:', error);
  }
});

/**
 * Відстеження зміни адресу сторінки на присутність параметра vac_id, щоб завантажити ідентифікатор вакансії та її дані
 */
watch(() => route.path, async (newPath) => {
  await loadInitialVacancies();

  if (!newPath.includes('vac_id') && selectedVacancyId.value) {
    selectedVacancyId.value = null;
    selectedVacancy.value = null;
  } else if (newPath.includes('vac_id') && vacancies.value) {
    const searchId = Number(route.query.vac_id);
    selectedVacancy.value = vacancies.value.find(vac => vac.vacId === searchId);
    selectedVacancyId.value = route.query.vac_id;
  }
});

/**
 * Обрання вакансії для відображення
 * @param vacancy вакансія, яку треба відобразити
 */
const handleSelectVacancy = (vacancy) => {
  selectedVacancyId.value = vacancy.vacId;
  selectedVacancy.value = vacancy;
  router.push({ path: route.path, query: { ...route.query, vac_id: vacancy.vacId } });
};

/**
 * Закриття детальної інформації про вакансію
 */
const closeVacancyDetails = () => {
  selectedVacancyId.value = null;
  selectedVacancy.value = null;
  const { vac_id, ...newQuery } = route.query;
  router.push({ path: route.path, query: newQuery });
};

/**
 * Отримання компонента вакансії, якщо є обрана вакансія зі списку
 */
const getDetailedVacancyComponent = () => {
  if (selectedVacancy.value) {
    if (route.path === '/candidate-dash/search' ||
        route.path === '/candidate-dash/saved' ||
        route.path === '/candidate-dash/applied') {
      return VacancyCardCandidate;
    }
    return null;
  }
};

/**
 * Обробник події відображення профілю
 */
const handleShowProfile = () => {
  router.push(`/candidate-dash/profile/${userId.value}`);
};

/**
 * Обробник події відображення пошуку вакансій
 */
const handleShowVacancies = () => {
  router.push('/candidate-dash/search');
};

/**
 * Обробник події відображення збережених вакансій
 */
const handleShowSavedVacancies = () => {
  router.push('/candidate-dash/saved');
};

/**
 * Обробник події відображення вакансій, на які відгукнувся
 */
const handleShowAppliedVacancies = () => {
  router.push('/candidate-dash/applied');
};

/**
 * Обробник події відображення сповіщень
 */
const handleShowNotifications = () => {
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

.vacancy-details-container {
  margin: 0 auto;
  padding: 20px;
}
</style>
