<template>
  <div role="search" class="search-container">
    <label for="search" class="visually-hidden">Пошуковий запит</label>
    <input
        id="search"
        type="text"
        class="search-input"
        placeholder="Введіть пошуковий запит"
        v-model="search"
        @keyup.enter="handleSearch"
        aria-label="Введіть пошуковий запит для пошуку вакансій"
    />
    <label for="search" class="searchBtn">
      <button class="search-button" @click="handleSearch" aria-label="Знайти вакансії">
        🔍
      </button>
    </label>
  </div>
</template>

<script setup>
import { ref, defineEmits } from "vue";

const emit = defineEmits(["search"]);
const search = ref("");

/**
 * Емітування події пошуку вакансій за запитом
 */
function handleSearch() {
  emit("search", search.value);
}
</script>

<style scoped>
.searchBtn {
  align-content: center;
}

.search-container {
  display: flex;
  width: 100%;
  gap: 8px;
}

.search-input {
  flex: 1;
  padding: 8px;
  border: 2px solid #aaa;
  border-radius: 4px;
}

.search-button {
  padding: 20px 20px;
  background-color: #4caf50;
  border: none;
  color: white;
  font-weight: bold;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.search-button:hover {
  background-color: #29752d;
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
</style>
