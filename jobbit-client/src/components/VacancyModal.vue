<template>
  <div class="modal-overlay" @click.self="closeModal">
    <div class="modal-content">
      <h2>{{ isEditMode ? 'Редагувати вакансію' : 'Додати нову вакансію' }}</h2>
      <form @submit.prevent="submitForm">
        <div class="form-group">
          <label for="title">Назва вакансії:</label>
          <input type="text" id="title" v-model="vacancyForm.title" required>
        </div>

        <div class="form-group">
          <label for="description">Опис вакансії:</label>
          <textarea id="description" v-model="vacancyForm.description" rows="5"></textarea>
        </div>

        <div class="form-group">
          <label for="remote">Віддалена робота:</label>
          <select id="remote" v-model="vacancyForm.remote">
            <option value="Так">Так</option>
            <option value="Ні">Ні</option>
            <option value="Частково віддалено">Частково віддалено</option>
          </select>
        </div>

        <div class="form-group radio-group">
          <label>Зайнятість:</label>
          <input type="radio" id="fulltime" value="true" v-model="vacancyForm.fulltime"> <label for="fulltime">Повна</label>
          <input type="radio" id="parttime" value="false" v-model="vacancyForm.fulltime"> <label for="parttime">Часткова</label>
        </div>

        <div class="form-group">
          <label for="levelEng">Рівень англійської:</label>
          <select id="levelEng" v-model="vacancyForm.levelEng">
            <option value="A1">A1 (Beginner)</option>
            <option value="A2">A2 (Elementary)</option>
            <option value="B1">B1 (Intermediate)</option>
            <option value="B2">B2 (Upper-Intermediate)</option>
            <option value="C1">C1 (Advanced)</option>
            <option value="C2">C2 (Proficiency)</option>
          </select>
        </div>

        <div class="form-group">
          <label for="minExp">Мінімальний досвід (років):</label>
          <input type="number" id="minExp" v-model="vacancyForm.minExp" min="0">
        </div>

        <div class="form-group">
          <label for="setSalary">Зарплата:</label>
          <input type="number" id="setSalary" v-model="vacancyForm.setSalary" min="0">
        </div>

        <div class="modal-actions">
          <button type="button" @click="closeModal">Скасувати</button>
          <button type="submit">{{ isEditMode ? 'Зберегти зміни' : 'Додати вакансію' }}</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, defineProps, defineEmits, watch } from 'vue';

/**
 * Пропс для отримання даних про вакансію для редагування, а також режим редагування/додавання
 */
const props = defineProps({
  isOpen: Boolean,
  isEditMode: Boolean,
  vacancyToEdit: Object
});

const emit = defineEmits(['close', 'save']);

/**
 * Дані вакансії, які йдуть в форму
 */
const vacancyForm = ref({
  title: '',
  description: '',
  remote: 'Так',
  fulltime: null,
  levelEng: 'A1',
  minExp: 0,
  setSalary: null
});

/**
 * Реагує на зміну даних редагованої/додаваної вакансії
 */
watch(() => props.vacancyToEdit, (newVacancy) => {
      if (newVacancy) {
        vacancyForm.value = { ...newVacancy };
      } else {
        resetForm();
      }
    },
    { deep: true, immediate: true }
);

/**
 * Метод, який оновлює форму з даними до початкових значень або до значень vacancyToEdit
 */
const resetForm = () => {
  vacancyForm.value = {
    title: props.vacancyToEdit?.title || '',
    description: props.vacancyToEdit?.description || '',
    remote: props.vacancyToEdit?.remote || 'Так',
    fulltime: props.vacancyToEdit?.fulltime !== undefined ? props.vacancyToEdit.fulltime : null,
    levelEng: props.vacancyToEdit?.levelEng || 'A1',
    minExp: props.vacancyToEdit?.minExp !== undefined ? props.vacancyToEdit.minExp : 0,
    setSalary: props.vacancyToEdit?.setSalary !== undefined ? props.vacancyToEdit.setSalary : null
  };
};

/**
 * Метод, який емітує закривання вікна додавання/редагування вакансії наверх
 */
const closeModal = () => {
  emit('close');
  resetForm();
};

/**
 * Метод, який емітує метод збереження наверх
 */
const submitForm = () => {
  emit('save', vacancyForm.value);
  closeModal();
};
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.6);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background-color: white;
  padding: 20px;
  border-radius: 8px;
  width: 80%;
  height: 80%;
  max-width: 600px;
  margin: 30px;
  overflow-y: auto;
  border: 1px solid #cccccc;
}

h2 {
  margin-top: 0;
  color: #333;
}

.form-group {
  margin-bottom: 15px;
}

label {
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
  color: #555;
}

.form-group.radio-group label {
  display: block;
  margin-bottom: 15px;
  font-weight: bold;
}

.form-group.radio-group input[type="radio"] {
  margin-right: 5px;
}

.form-group.radio-group label[for="fulltime"],
.form-group.radio-group label[for="parttime"] {
  display: inline-block;
  font-weight: normal;
}

.form-group.radio-group > div {
  display: flex;
  align-items: center;
  gap: 5px;
  margin-bottom: 5px;
}

input[type="text"],
input[type="number"],
select,
textarea {
  width: 100%;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box;
  font-size: 16px;
}

input[type="checkbox"] {
  margin-right: 5px;
}

.modal-actions {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

.modal-actions button {
  padding: 10px 15px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 16px;
}

.modal-actions button:first-child {
  background-color: #f44336;
  color: white;
}

.modal-actions button:first-child:hover {
  background-color: #d32f2f;
}

.modal-actions button:last-child {
  background-color: #4caf50;
  color: white;
}

.modal-actions button:last-child:hover {
  background-color: #2e7d32;
}
</style>
