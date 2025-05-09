<template>
  <div class="modal-overlay" @click.self="$emit('close')">
    <div class="modal-content">
      <h2>{{ title }}</h2>
      <textarea v-model="message" placeholder="Введіть повідомлення"></textarea>
      <div class="character-counter">{{ message.length }} / 1000 символів</div>
      <div class="modal-actions">
        <button @click="$emit('close')">Скасувати</button>
        <button @click="$emit('send', message)" :disabled="!message">Надіслати</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, defineProps, defineEmits } from 'vue';

const props = defineProps({
  title: String,
});

const emit = defineEmits(['close', 'send']);

const message = ref('');
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background-color: white;
  padding: 20px;
  border-radius: 5px;
  width: 50%;
  max-width: 400px;
}

h2 {
  margin-top: 0;
  margin-bottom: 15px;
}

textarea {
  width: 100%;
  padding: 10px;
  margin-bottom: 5px; /* Зменшено відступ, щоб лічильник був ближче */
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box;
  min-height: 100px;
}

.character-counter {
  font-size: 0.8em;
  color: #777;
  text-align: right;
  margin-bottom: 15px;
}

.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

.modal-actions button {
  padding: 10px 15px;
  border: none;
  border-radius: 4px;
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
