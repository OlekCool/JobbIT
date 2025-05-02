<template>
  <div role="search" class="search-container">
    <input
        id="search"
        type="text"
        class="search-input"
        placeholder="Введіть пошуковий запит"
        v-model="search"
        @keyup.enter="handleSearch"
    />
    <label for="search" class="searchBtn">
      <button class="search-button" @click="handleSearch" >
        🔍
      </button>
    </label>
  </div>
</template>

<script setup>
import { ref } from "vue";

const emit = defineEmits(["search"]);
const search = ref("");

/**
 * Здійснення пошуку вакансій за запитом
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
  background-color: #1c571d;
  border: none;
  color: white;
  font-weight: bold;
  border-radius: 4px;
  cursor: pointer;
}
</style>
