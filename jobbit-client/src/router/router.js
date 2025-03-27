import { createRouter, createWebHistory } from "vue-router";
import StartPage from "@/pages/StartPage.vue";
import RegistrationPage from "@/pages/auth/RegistrationPage.vue";
import LoginPage from "@/pages/auth/LoginPage.vue";
import ForgotPassword from "@/pages/auth/ForgotPassword.vue";
import SendOTP from "@/pages/auth/SendOTP.vue";
import NewPassword from "@/pages/auth/NewPassword.vue";
import MainRecruiterPage from "@/pages/MainRecruiterPage.vue";
import MainCandidatePage from "@/pages/MainCandidatePage.vue";

const routes = [
  { path: "/", name: "StartPage", component: StartPage },
  { path: "/auth/register", name: "RegistrationPage", component: RegistrationPage },
  { path: "/auth/login", name: "LoginPage", component: LoginPage },
  { path: "/auth/forgotpassword", name: "ForgotPassword", component: ForgotPassword },
  { path: "/auth/sendotp", name: "SendOTP", component: SendOTP },
  { path: "/auth/newpassword", name: "NewPassword", component: NewPassword },

  {
    path: "/recruiter-dash",
    name: "MainRecruiterPage",
    component: MainRecruiterPage,
    beforeEnrer: (to, from, next) => {
      const role = localStorage.getItem("userRole");
      if (role === "RECRUITER") {
        next();
      } else {
        next("/login");
      }
    },
  },

  {
    path: "/candidate-dash",
    name: "MainCandidatePage",
    component: MainCandidatePage,
    beforeEnrer: (to, from, next) => {
      const role = localStorage.getItem("userRole");
      if (role === "CANDIDATE") {
        next();
      } else {
        next("/login");
      }
    },
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
