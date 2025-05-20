<template>
  <div class="vacancy-details">
    <h2 class="vacancy-title" id="vacancy-details-title">{{ vacancy.title }}</h2>

    <div class="details-grid" role="group" aria-labelledby="vacancy-info-heading">
      <h3 id="vacancy-info-heading" class="visually-hidden">Детальна інформація про вакансію</h3>
      <div class="detail-item">
        <span class="label" id="salary-label">Зарплата:</span>
        <span class="value" aria-labelledby="salary-label">{{ vacancy.setSalary }} грн</span>
      </div>
      <div class="detail-item">
        <span class="label" id="experience-label">Досвід:</span>
        <span class="value" aria-labelledby="experience-label">{{ vacancy.minExp }} років</span>
      </div>
      <div class="detail-item">
        <span class="label" id="employment-label">Тип зайнятості:</span>
        <span class="value" aria-labelledby="employment-label">{{ vacancy.fulltime ? 'Повний робочий день' : 'Часткова зайнятість' }}</span>
      </div>
      <div class="detail-item">
        <span class="label" id="remote-label">Віддаленість:</span>
        <span class="value" aria-labelledby="remote-label">{{ vacancy.remote }}</span>
      </div>
      <div class="detail-item">
        <span class="label" id="english-label">Рівень англійської:</span>
        <span class="value" aria-labelledby="english-label">{{ vacancy.levelEng }}</span>
      </div>
      <div class="detail-item">
        <span class="label" id="posted-date-label">Дата публікації:</span>
        <span class="value" aria-labelledby="posted-date-label">{{ formatDate(vacancy.postedDate) }}</span>
      </div>
    </div>

    <div class="vacancy-description" role="article" aria-labelledby="description-heading">
      <h3 id="description-heading">Опис вакансії</h3>
      <p>{{ vacancy.description }}</p>
    </div>
  </div>
</template>

<script setup>
import { defineProps } from 'vue';
import { ref } from 'vue';
import { format } from 'date-fns';
import { uk } from 'date-fns/locale';

/**
 * Пропс для отримання даних про вакансію з батьківського компонента
 */
const props = defineProps({
  vacancy: {
    type: Object,
    required: true
  }
});

const vacancy = ref(props.vacancy);

/**
 * Форматування дати для виводу в html-розмітку
 */
const formatDate = (dateString) => {
  if (!dateString) return '';
  const date = new Date(dateString);
  return format(date, 'dd MMMM yyyy, HH:mm', { locale: uk });
};
</script>

<style scoped>
.vacancy-details {
  border: 1px solid #eee;
  padding: 20px;
  margin-bottom: 15px;
  border-radius: 8px;
  background-color: #f9f9f9;
}

.vacancy-title {
  font-size: 24px;
  margin-bottom: 10px;
  color: #333;
}

.visually-hidden {
  position: absolute;
  width: 1px;
  height: 1px;
  margin: -1px;
  padding: 0;
  overflow: hidden;
  clip: rect(0, 0, 0, 0);
  border: 0;
}

.details-grid {
  display: grid;
  grid-template-columns: repeat(6, minmax(150px, 1fr));
  gap: 10px;
  margin-bottom: 20px;
}

.detail-item {
  display: flex;
  flex-direction: column;
}

.label {
  font-weight: bold;
  color: #555;
  margin-bottom: 5px;
  font-size: 14px;
}

.value {
  font-size: 16px;
  color: #333;
}

.vacancy-description {
  margin-top: 20px;
}

.vacancy-description h3 {
  font-size: 20px;
  margin-bottom: 10px;
  color: #333;
}

.vacancy-description p {
  line-height: 1.6;
  color: #444;
}

@media (max-width: 900px) {
  .details-grid {
    grid-template-columns: repeat(3, minmax(150px, 1fr));
  }
}

@media (max-width: 600px) {
  .details-grid {
    grid-template-columns: repeat(2, minmax(150px, 1fr));
  }
}

@media (max-width: 400px) {
  .details-grid {
    grid-template-columns: 1fr;
  }
}
</style>
