<template>
  <div class="notification-item">
    <div class="notification-header">
      <span class="vacancy-title" v-if="vacancyTitle">
        {{ vacancyTitle }}
      </span>
      <span class="notification-date">
        {{ formatDate(notification.notifDate) }}
      </span>
    </div>
    <div class="notification-body">
      <p class="notification-text">{{ notification.text }}</p>
    </div>
    <button class="delete-button" @click="emit('delete-notification', notification.notId)">
      Видалити
    </button>
  </div>
</template>

<script setup>
import {defineProps, defineEmits} from 'vue';
import { format } from "date-fns";
import { uk } from "date-fns/locale";

// eslint-disable-next-line
const props = defineProps({
  notification: {
    type: Object,
    required: true
  },
  vacancyTitle: {
    type: String,
    default: ''
  }
});

/**
 * Форматування дати для виводу в html-розмітку
 */
const formatDate = (dateString) => {
  if (!dateString) return '';
  const date = new Date(dateString);
  return format(date, 'dd MMMM yyyy, HH:mm', { locale: uk });
};

const emit = defineEmits(['delete-notification'])
</script>

<style scoped>
.notification-item {
  margin-bottom: 20px;
  background-color: #f9f9f9; /* Змінено колір фону */
  border: 1px solid #ddd; /* Додано тонку межу */
  border-radius: 8px;
  padding: 3%; /* Збільшено внутрішні відступи */
  display: flex;
  flex-direction: column; /* Розташовуємо елементи вертикально */
}

.notification-header {
  display: flex;
  justify-content: space-between; /* Розміщуємо заголовок та дату по краях */
  align-items: center; /* Вирівнюємо по вертикалі */
  margin-bottom: 10px; /* Відступ від заголовка до тексту */
}

.vacancy-title {
  font-weight: bold; /* Жирний шрифт для назви вакансії */
  color: #333; /* Темний колір тексту */
  font-size: 1.1em; /* Трохи більший розмір шрифту */
}

.notification-date {
  color: #777; /* Сірий колір для дати */
  font-size: 0.9em; /* Менший розмір шрифту */
  font-style: italic; /* Курсив для дати */
}

.notification-body {
  margin-bottom: 15px; /* Відступ від тексту до кнопки */
}

.notification-text {
  line-height: 1.5; /* Міжрядковий інтервал для кращої читабельності */
  color: #444;
  text-align: left; /* Вирівнювання тексту зліва */
}

.delete-button {
  padding: 8px 15px;
  background-color: #ffe0e0; /* Світло-червоний фон */
  border: 1px solid #ffaaaa; /* Червона межа */
  border-radius: 5px;
  cursor: pointer;
  font-size: 0.9em;
  transition: background-color 0.2s;
  align-self: flex-start; /* Вирівнюємо кнопку зліва */
}

.delete-button:hover {
  background-color: #ffc0c0; /* Трохи темніший червоний при наведенні */
}
</style>
