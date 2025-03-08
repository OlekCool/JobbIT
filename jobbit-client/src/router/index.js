import { createRouter, createWebHistory } from "vue-router";
import StartPage from "@/components/StartPage.vue";

const routes = [{ path: "/", name: "home", component: StartPage }];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
