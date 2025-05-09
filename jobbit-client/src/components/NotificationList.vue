<template>
  <div class="notification-list">
    <div class="notification-header">
      <h2>Ваші сповіщення</h2>
      <button class="delete-all-button" @click="deleteAllNotifications" v-if="notifications.length > 0">
        Видалити всі сповіщення
      </button>
    </div>
    <div v-if="loading" class="loading-message">Завантаження сповіщень...</div>
    <div v-else-if="notifications.length === 0" class="empty-message">У вас немає нових сповіщень.</div>
    <div v-else class="notifications">
      <NotificationItem
          v-for="notification in sortedNotifications"
          :key="notification.notifId"
          :notification="notification"
          :vacancyTitle="vacancyTitles[notification.vacId]"
          @delete-notification="handleDeleteNotification"
      />
    </div>
  </div>
</template>

<script setup>
import { onMounted, defineProps , ref, computed } from "vue";
import NotificationItem from "./NotificationItem.vue";
import NotificationService from "@/services/NotificationService.ts";
import VacancyService from "@/services/VacancyService.ts";

const props = defineProps({
  notifications: {
    type: Array,
    default: () => []
  }
});

const userId = localStorage.getItem('userId');
const authToken = localStorage.getItem('authToken');
const loading = ref(false);
const vacancyTitles = ref({});

onMounted(async () => {
  await fetchVacancyTitles();
});

const fetchVacancyTitles = async () => {
  try {
    const uniqueVacancyIds = [...new Set(props.notifications.map(notif => notif.vacId))];
    const titles = {};
    for (const id of uniqueVacancyIds) {
      const vacancy = await VacancyService.getAllVacancies();
      const foundVacancy = vacancy.find(v => v.vacId === id);
      if (foundVacancy) {
        titles[id] = foundVacancy.title;
      }
    }
    vacancyTitles.value = titles;
  } catch (error) {
    console.error('Помилка при отриманні назв вакансій:', error);
  }
};

/**
 * Обчислювані значення вакансій для сортування сповіщень за датою (новіші зверху)
 */
const sortedNotifications = computed(() => {
  return [...props.notifications].sort((a, b) => new Date(b.notifDate) - new Date(a.notifDate));
});

/**
 * Метод для видалення окремого сповіщення
 * @param notificationId Ідентифікатор сповіщення для видалення
 */
const handleDeleteNotification = async (notificationId) => {
  if (authToken) {
    try {
      loading.value = true;
      await NotificationService.deleteNotification(notificationId, authToken);
      window.location.reload();
      console.log(`Сповіщення з ID ${notificationId} видалено.`);
    } catch (error) {
      console.error('Помилка при видаленні сповіщення:', error);
    } finally {
      loading.value = false;
    }
  } else {
    console.warn('Токен авторизації відсутній.');
  }
};

/**
 * Метод для видалення всіх сповіщень кандидата
 */
const deleteAllNotifications = async () => {
  if (userId && authToken) {
    try {
      loading.value = true;
      await NotificationService.deleteAllNotificationsForCandidate(userId, authToken);
      window.location.reload();
      console.log('Всі сповіщення видалено.');
    } catch (error) {
      console.error('Помилка при видаленні всіх сповіщень:', error);
    } finally {
      loading.value = false;
    }
  } else {
    console.warn('ID користувача або токен авторизації відсутні.');
  }
};
</script>

<style scoped>
.notification-list {
  width: 80%;
  margin: 0 auto;
  padding: 2%;
  background-color: #fefefe;
}

.notification-header {
  padding: 0 30%;
  max-height: 50px;
  display: flex;
  align-items: center;
  margin-bottom: 3%;
  justify-content: space-around;
}

.notification-header h2 {
  margin: 0;
  font-size: 1.5em;
}

.notification-header .delete-all-button {
  padding: 8px 12px;
  background-color: #fdd;
  border: 1px solid #fbb;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.2s;
  font-size: 0.9em;
  height: fit-content;
}

.notification-header .delete-all-button:hover {
  background-color: #faa;
}

.loading-message {
  font-style: italic;
  color: #777;
  text-align: center;
  padding: 20px;
}

.empty-message {
  color: #999;
  text-align: center;
  padding: 20px;
}

.notifications {
  margin-top: 10px;
  border: 1px solid #ddd;
  border-radius: 8px;
  overflow-y: auto;
  max-height: 70%;
  padding: 3%;
}

</style>
