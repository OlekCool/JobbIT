import { createRouter, createWebHistory } from "vue-router";
import StartPage from "@/pages/StartPage.vue";
import RegistrationPage from "@/pages/auth/RegistrationPage.vue";
import LoginPage from "@/pages/auth/LoginPage.vue";
import ForgotPassword from "@/pages/auth/ForgotPassword.vue";
import SendOTP from "@/pages/auth/SendOTP.vue";
import NewPassword from "@/pages/auth/NewPassword.vue";
import MainRecruiterPage from "@/pages/MainRecruiterPage.vue";
import MainCandidatePage from "@/pages/MainCandidatePage.vue";
import VerifyPage from "@/pages/auth/VerifyPage.vue";

/**
 * Описує навігацію по додатку.
 * @constant
 * @type {Array<Object>}
 */
const routes = [
  /**
   * Шлях до стартової сторінки.
   * @type {Object}
   * @property {string} path - Шлях до сторінки.
   * @property {string} name - Ім'я маршруту.
   * @property {Object} component - Компонент, який буде відображатися на сторінці.
   */
  { path: "/", name: "StartPage", component: StartPage },

  /**
   * Шлях до сторінки реєстрації.
   * @type {Object}
   * @property {string} path - Шлях до сторінки.
   * @property {string} name - Ім'я маршруту.
   * @property {Object} component - Компонент, який буде відображатися на сторінці.
   */
  { path: "/auth/register", name: "RegistrationPage", component: RegistrationPage },

  /**
   * Шлях до сторінки входу.
   * @type {Object}
   * @property {string} path - Шлях до сторінки.
   * @property {string} name - Ім'я маршруту.
   * @property {Object} component - Компонент, який буде відображатися на сторінці.
   */
  { path: "/auth/login", name: "LoginPage", component: LoginPage },

  /**
   * Шлях до сторінки надсилання пошти.
   * @type {Object}
   * @property {string} path - Шлях до сторінки.
   * @property {string} name - Ім'я маршруту.
   * @property {Object} component - Компонент, який буде відображатися на сторінці.
   */
  { path: "/auth/forgotpassword", name: "ForgotPassword", component: ForgotPassword },

  /**
   * Шлях до сторінки для надсилання OTP.
   * @type {Object}
   * @property {string} path - Шлях до сторінки.
   * @property {string} name - Ім'я маршруту.
   * @property {Object} component - Компонент, який буде відображатися на сторінці.
   */
  { path: "/auth/sendotp", name: "SendOTP", component: SendOTP },

  /**
   * Шлях до сторінки для зміни пароля.
   * @type {Object}
   * @property {string} path - Шлях до сторінки.
   * @property {string} name - Ім'я маршруту.
   * @property {Object} component - Компонент, який буде відображатися на сторінці.
   */
  { path: "/auth/newpassword", name: "NewPassword", component: NewPassword },

  /**
   * Шлях до сторінки для підтвердження електронної пошти.
   * @type {Object}
   * @property {string} path - Шлях до сторінки.
   * @property {string} name - Ім'я маршруту.
   * @property {Object} component - Компонент, який буде відображатися на сторінці.
   */
  { path: "/auth/verify", name: "VerifyPage", component: VerifyPage },

  /**
   * Шлях до головної сторінки рекрутера.
   * @type {Object}
   * @property {string} path - Шлях до сторінки.
   * @property {string} name - Ім'я маршруту.
   * @property {Object} component - Компонент, який буде відображатися на сторінці.
   * @property {function} beforeEnter - Перевірка перед входом на сторінку для рекрутера.
   */
  {
    path: "/recruiter-dash",
    name: "MainRecruiterPage",
    component: MainRecruiterPage,
    beforeEnter: (to, from, next) => {
      const role = localStorage.getItem("userRole");
      if (role === "RECRUITER") {
        next();
      } else {
        next("/login");
      }
    },
  },

  /**
   * Шлях до головної сторінки кандидата.
   * @type {Object}
   * @property {string} path - Шлях до сторінки.
   * @property {string} name - Ім'я маршруту.
   * @property {Object} component - Компонент, який буде відображатися на сторінці.
   * @property {function} beforeEnter - Перевірка перед входом на сторінку для кандидата.
   */
  {
    path: "/candidate-dash",
    name: "MainCandidatePage",
    component: MainCandidatePage,
    children: [
      {
        path: 'profile/:userId',
        name: 'CandidateProfile',
        component: MainCandidatePage,
      },
      {
        path: 'search',
        name: 'CandidateSearch',
        component: MainCandidatePage,
      },
    ],
    beforeEnter: (to, from, next) => {
      const role = localStorage.getItem("userRole");
      if (role === "CANDIDATE") {
        next();
      } else {
        next("/login");
      }
    },
  },
];

/**
 * Створення роутера для додатка.
 * @constant
 * @type {Object}
 * @property {function} history - Історія переходів для роутера.
 * @property {Array<Object>} routes - Маршрути додатка.
 */
const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
