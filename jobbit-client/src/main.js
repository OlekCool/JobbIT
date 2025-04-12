/**
 * @file main.js
 * @description Точка входу у Vue-додаток. Ініціалізує застосунок, підключає роутер та монтує його до DOM.
 */

import { createApp } from "vue";
import App from "./App.vue";
import router from "./router/router.ts";

/**
 * Створення екземпляра Vue-додатка, підключення роутера та монтування до елемента з id "app".
 */
createApp(App).use(router).mount("#app");
