<template>
  <div class="dashboard">
    <NavbarRecruiter @show-profile="handleShowProfile" @show-vacancies="handleShowVacancies" />
    <div class="dashboard-body">
      <RecruiterProfile v-if="showProfile" />
    </div>
  </div>
</template>

<script setup>
import NavbarRecruiter from "@/components/NavbarRecruiter.vue";
import RecruiterProfile from "@/components/RecruiterProfile.vue";

import { useRouter } from "vue-router";
import { onMounted, ref } from "vue";

const router = useRouter();
const showProfile = ref(false);
const userId = ref(localStorage.getItem('userId'));

onMounted(() => {
  userId.value = localStorage.getItem('userId');
  console.log('userId onMounted:', userId.value, 'currentPath:', router.currentRoute.value.path);

  if (router.currentRoute.value.path.startsWith('/recruiter-dash/profile/')) {
    showProfile.value = true;
    console.log('Встановлено showProfile в true, оскільки шлях починається з /recruiter-dash/profile/');
  } else {
    showProfile.value = false;
    console.log('Встановлено showProfile в false');
    if (userId.value && router.currentRoute.value.path === '/recruiter-dash') {
      console.log('Перенаправлення на /candidate-dash/search');
      router.replace('/recruiter-dash/myvacancies');
    }
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
