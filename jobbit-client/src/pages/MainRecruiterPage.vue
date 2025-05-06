<template>
  <div class="dashboard">
    <NavbarRecruiter :recruiterProfile="recruiterProfileData"
                     :userPhoto="userPhotoUrl"
                     @show-profile="handleShowProfile"
                     @show-all-vacancies="handleShowAllVacancies"
                     @show-my-vacancies="handleShowMyVacancies"/>
    <div class="dashboard-body">
      <div v-if="showMyVacancies && !selectedVacancyId" class="my-vacancies-sidebar">
        <button @click="openAddVacancyModal">Додати вакансію</button>
      </div>
      <FiltersSection v-if="!showProfile && !showMyVacancies && showAllVacancies && !selectedVacancyId"
                      @filter-change="applyFilters" />
      <VacanciesLayout
          v-if="!showProfile && !selectedVacancyId"
          :vacancies="filteredVacancies"
          @search="applySearch"
          @select-vacancy="handleSelectVacancy"
      />
      <RecruiterProfile v-if="showProfile && !showAllVacancies && !showMyVacancies && !selectedVacancyId"
                        :recruiterProfile="recruiterProfileData"
                        :userPhoto="userPhotoUrl" />
      <div v-if="selectedVacancyId" class="vacancy-details-container">
        <component
            :is="getDetailedVacancyComponent()"
            :vacancy="selectedVacancy"
            @vacancy-deleted-success="handleVacancyDeletedSuccess"
            @open-candidate-profile="handleOpenCandidateProfile"
        />
        <button @click="closeVacancyDetails">Назад до списку</button>
      </div>

      <VacancyModal
          v-if="showVacancyModal"
          :is-open="showVacancyModal"
          :is-edit-mode="isEditMode"
          :vacancy-to-edit="vacancyToEdit"
          @close="closeVacancyModal"
          @save="saveVacancy"
      />

      <div v-if="selectedCandidateId" class="candidate-profile-container">
        <h3>Профіль кандидата</h3>
        <CandidateProfile
            v-if="candidateProfileData"
            :candidateProfile="candidateProfileData"
            :candidateProjects="candidateProjects"
            :userPhoto="currentCandidatePhotoUrl"
            :isViewMode="true"
        />
        <button @click="closeCandidateProfile">Закрити профіль</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import NavbarRecruiter from "@/components/NavbarRecruiter.vue";
import RecruiterProfile from "@/components/RecruiterProfile.vue";
import VacanciesLayout from "@/components/VacanciesLayout.vue";
import FiltersSection from "@/components/FiltersSection.vue";
import VacancyCardCommon from "@/components/VacancyCardCommon.vue";
import VacancyCardRecruiter from "@/components/VacancyCardRecruiter.vue";
import VacancyModal from "@/components/VacancyModal.vue";
import CandidateProfile from "@/components/CandidateProfile.vue";
import ProfileService from "@/services/ProfileService.ts";
import VacancyService from "@/services/VacancyService.ts";
import { useRoute, useRouter } from "vue-router";
import { computed, onMounted, ref, watch } from "vue";

const router = useRouter();
const route = useRoute();

//змінні стану сторінки
const showProfile = ref(false);
const showAllVacancies = ref(false);
const showMyVacancies = ref(true);

//для вікна редагування/додавання вакансії
const showVacancyModal = ref(false);
const isEditMode = ref(false);

//з локального сховища дані користувача
const userId = ref(localStorage.getItem('userId'));
const authToken = localStorage.getItem('authToken');

//фото та дані профілю рекрутера
const recruiterProfileData = ref(null);
const userPhotoUrl = ref('/files/userPhotos/userDemo.png');

//фільтри пошуку та запит пошуку
const filters = ref({ remote: "", fulltime: [], level_eng: [], set_salary: null, min_exp: null, });
const searchQuery = ref("");
const vacancies = ref([]);

//обрана вакансія для детального перегляду
const selectedVacancyId = ref(null);
const selectedVacancy = ref(null);
const loadingVacancies = ref(false);

//для перегляду даних про профіль кандидата
const selectedCandidateId = ref(null);
const candidateProfileData = ref(null);
const currentCandidatePhotoUrl = ref('/files/userPhotos/userDemo.png');
const candidateProjects = ref([]);

/**
 * Асинхронний метод для загрузки вакансій, залежно від url-адреси сторінки
 * @returns {Promise<void>}
 */
const loadInitialVacancies = async () => {
  loadingVacancies.value = true;

  try {
    if (route.path.startsWith('/recruiter-dash/profile')) {
      vacancies.value = null;

      showProfile.value = true;
      showAllVacancies.value = false;
      showMyVacancies.value = false;
    } else if (route.path === '/recruiter-dash/my-vacancies') {
      const recruiterId = JSON.parse(userId.value);
      vacancies.value = await VacancyService.getRecruiterVacancies(recruiterId, authToken);

      showAllVacancies.value = false;
      showMyVacancies.value = true;
      showProfile.value = false;
    } else if (route.path === '/recruiter-dash/all-vacancies') {
      vacancies.value = await VacancyService.getAllVacancies();

      showAllVacancies.value = true;
      showMyVacancies.value = false;
      showProfile.value = false;
    }
  } catch (error) {
    console.error('Помилка при отриманні вакансій:', error);
  } finally {
    loadingVacancies.value = false;
  }
};

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
    const profileResponse = await ProfileService.getProfileRecruiter(userId.value, authToken);
    recruiterProfileData.value = profileResponse.data;

    const photoPath = await ProfileService.getRecruiterProfilePhoto(userId.value, authToken);
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
 * Відстеження зміни адресу сторінки на присутність параметра vac_id, щоб завантажити ідентифікатор  вакансії та  її дані
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
    if (route.path === '/recruiter-dash/my-vacancies') {
      return VacancyCardRecruiter;
    } else if (route.path === '/recruiter-dash/all-vacancies') {
      return VacancyCardCommon;
    }
  }
  return null;
};

/**
 * Обробник події відкриття профілю кандидата з компонента VacancyCardRecruiter
 * @param candidateId ідентифікатор кандидата
 */
const handleOpenCandidateProfile = async (candidateId) => {
  selectedCandidateId.value = candidateId;
  try {
    const profileResponse = await ProfileService.getProfileCandidate(candidateId, authToken);
    candidateProfileData.value = profileResponse.data;

    const candPhotoPath = await ProfileService.getCandidateProfilePhoto(candidateId, authToken);
    if (candPhotoPath) {
      currentCandidatePhotoUrl.value = `/${candPhotoPath}`;
    }

    candidateProjects.value = candidateProfileData.value?.projects || [];
  } catch (error) {
    console.error('Помилка при завантаженні профілю кандидата:', error);
  }
};

/**
 * Закриття профілю кандидата
 */
const closeCandidateProfile = () => {
  selectedCandidateId.value = null;
  candidateProfileData.value = null;
  candidateProjects.value = [];
  currentCandidatePhotoUrl.value = null;
};

/**
 * Обробник події відображення профілю рекрутера
 */
const handleShowProfile = () => {
  router.push(`/recruiter-dash/profile/${userId.value}`);
};

/**
 * Обробник події всіх вакансій
 */
const handleShowAllVacancies = () => {
  router.push('/recruiter-dash/all-vacancies');
};

/**
 * Обробник події відображення вакансій рекрутера
 */
const handleShowMyVacancies = () => {
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

/**
 * Відкриття модального вікна вакансії
 */
const openAddVacancyModal = () => {
  isEditMode.value = false;
  showVacancyModal.value = true;
};

/**
 * Закриття модального вікна вакансії
 */
const closeVacancyModal = () => {
  showVacancyModal.value = false;
};

/**
 * Збереження вакансії, яку додають
 */
const saveVacancy = async (vacancyData) => {
  try {
    const recruiterId = JSON.parse(userId.value);
    if (isEditMode.value) {
    } else {
      const response = await VacancyService.addNewVacancy(recruiterId, vacancyData, authToken);
      await loadInitialVacancies();
    }
  } catch (error) {
    console.error('Помилка при збереженні вакансії:', error);
  }
};

/**
 * Обробка видалення вакансії
 */
const handleVacancyDeletedSuccess = () => {
  router.push("/recruiter-dash/my-vacancies");
  loadInitialVacancies();
  selectedVacancyId.value = null;
  selectedVacancy.value = null;
};
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
  position: relative;
  overflow: hidden;
}

.my-vacancies-sidebar {
  width: 20.05%;
  height: 78vh;
  padding: 0 20px;
  border-right: 2px solid #ccc;
  background-color: #f9f9f9;
  box-sizing: border-box;
  margin: 2px;
  align-content: center;
}

.my-vacancies-sidebar button {
  background-color: #4caf50;
  color: white;
  padding: 10px 15px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 16px;
  transition: background-color 0.3s ease;
}

.my-vacancies-sidebar button:hover {
  background-color: #29752d;
}

.vacancy-details-container {
  margin: 0 auto;
  padding: 20px;
}

.vacancy-details-container button {
  background-color: #424242;
  color: white;
  padding: 10px 15px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 16px;
  transition: background-color 0.3s ease;
}

.vacancy-details-container button:hover {
  background-color: #393939;
}

/* Стилі для контейнера профілю кандидата */
.candidate-profile-container {
  position: absolute;
  top: 50%; /* Центрування по вертикалі */
  left: 50%; /* Центрування по горизонталі */
  transform: translate(-50%, -50%);
  background-color: white;
  border: 1px solid #ccc;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  z-index: 10; /* Щоб бути над оверлеєм */
  max-width: 80vw;
  max-height: 80vh;
  overflow-y: auto;
}


.candidate-profile-container h3 {
  margin-top: 5px;
  margin-bottom: 15px;
  text-align: center;
  color: #333;
}

.candidate-profile-container button {
  background-color: #007bff;
  color: white;
  padding: 10px 15px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 16px;
  transition: background-color 0.3s ease;
  display: block;
  margin: 20px auto 0;
}

.candidate-profile-container button:hover {
  background-color: #0056b3;
}
</style>
