import { createRouter, createWebHistory } from "vue-router";
import StartPage from "@/pages/StartPage.vue";
import RegistrationPage from "@/pages/RegistrationPage.vue";
import LoginPage from "@/pages/LoginPage.vue";
import MainRecruiterPage from "@/pages/MainRecruiterPage.vue";
import MainCandidatePage from "@/pages/MainCandidatePage.vue";

const routes = [
  { path: "/", name: "StartPage", component: StartPage },
  { path: "/register", name: "RegistrationPage", component: RegistrationPage },
  { path: "/login", name: "LoginPage", component: LoginPage },
  { path: "/recruiter-dash", name: "MainRecruiterPage", component: MainRecruiterPage },
  { path: "/candidate-dash", name: "MainCandidatePage", component: MainCandidatePage },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
