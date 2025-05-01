<template>
  <div class="dashboard">
    <NavbarRecruiter :recruiterProfile="recruiterProfileData"
                     :userPhoto="userPhotoUrl"
                     @show-profile="handleShowProfile"
                     @show-vacancies="handleShowVacancies" />
    <div class="dashboard-body">
      <RecruiterProfile v-if="showProfile" :recruiterProfile="recruiterProfileData" :userPhoto="userPhotoUrl" />
    </div>
  </div>
</template>

<script setup>
import NavbarRecruiter from "@/components/NavbarRecruiter.vue";
import RecruiterProfile from "@/components/RecruiterProfile.vue";
import { useRouter } from "vue-router";
import { onMounted, ref } from "vue";
import ProfileService from "@/services/ProfileService.ts";

const router = useRouter();
const showProfile = ref(false);
const userPhotoUrl = ref('/files/userPhotos/userDemo.png');
const userId = ref(localStorage.getItem('userId'));
const authToken = localStorage.getItem('authToken');
const recruiterProfileData = ref(null);

onMounted(async () => {
  if (!userId || !authToken) {
    await router.push('/login');
    return;
  }

  try {
    const profileResponse = await ProfileService.getProfileRecruiter(userId.value, authToken);
    recruiterProfileData.value = profileResponse.data;
    console.log('Дані профілю кандидата завантажено:', recruiterProfileData.value);

    const photoPath = await ProfileService.getRecruiterProfilePhoto(userId.value, authToken);
    if (photoPath) {
      userPhotoUrl.value = `/${photoPath}`;
      console.log('Шлях до фото користувача отримано:', userPhotoUrl.value);
    }

    if (router.currentRoute.value.path.startsWith('/recruiter-dash/profile/')) {
      showProfile.value = true;
    } else if (router.currentRoute.value.path === '/recruiter-dash') {
      await router.replace('/recruiter-dash/myvacancies');
    }
  } catch (error) {
    console.error('Помилка при завантаженні даних на головній сторінці кандидата:', error);
  }
});

/**
 * Обробник події відображення профілю
 */
const handleShowProfile = () => {
  showProfile.value = true;
  router.push(`/recruiter-dash/profile/${userId.value}`);
};

/**
 * Обробник події відображення вакансій
 */
const handleShowVacancies = () => {
  showProfile.value = false;
  router.push('/recruiter-dash/myvacancies');
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
}
</style>
