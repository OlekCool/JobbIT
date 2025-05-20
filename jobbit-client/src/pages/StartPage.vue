<template>
  <div class="start-page">
    <header class="header" role="banner">
      <h1 class="logo" aria-label="JobbIT - Home">JobbIT - find your IT-way</h1>
      <nav class="buttons" aria-label="Main Navigation">
        <router-link to="/auth/register" class="button registration" role="button">Реєстрація</router-link>
        <router-link to="/auth/login" class="button authorization" role="button">Авторизація</router-link>
      </nav>
    </header>

    <main class="content" id="main-content">
      <section class="description" aria-labelledby="description-heading">
        <h2 id="description-heading" class="visually-hidden">Про JobbIT</h2>
        <p>
          JobbIT - це сучасна система як для пошуку вакансій кандидатами, так і для знаходження потрібних кандидатів
          HR-працівниками компаній. За допомогою неї, кандидат може знайти потрібну йому вакансію та відгукнутися на
          неї, а рекрутер переглянути кандидатуру та прийняти рішення про запрошення на співбесіду.
        </p>
        <p>Платформа надає великий вибір вакансій, для всіх видів розробників.</p>
      </section>
    </main>

    <footer class="footer" role="contentinfo">
      <div class="footer-content">
        <div class="contacts">
          <h3>Контакти:</h3>
          <div class="social-links">
            <a href="#" class="social-link" target="_blank" rel="noopener noreferrer" aria-label="Наш канал у Telegram">
              <img src="@/assets/telegram.png" alt="Telegram icon" class="icon" />
            </a>
            <a href="#" class="social-link" target="_blank" rel="noopener noreferrer" aria-label="Наш репозиторій на GitHub">
              <img src="@/assets/github.png" alt="GitHub icon" class="icon" />
            </a>
          </div>
        </div>
        <div class="contact-info">
          <div class="contact-item">
            <img src="@/assets/phone.png" alt="Phone" class="icon" aria-hidden="true" />
            <span> +380xx-xxx-xx-xx</span>
          </div>
          <div class="contact-item">
            <img src="@/assets/gmail.png" alt="Email" class="icon" aria-hidden="true" />
            <span> *******@gmail.com</span>
          </div>
        </div>
      </div>
      <div class="copyright">© 2025 JobbIT. Всі права захищено.</div>
    </footer>
  </div>
</template>

<script setup></script>

<style scoped>

p, .copyright {
  font-size: 1vw;
}

h3 {
  font-size: 1.6vw;
}

span {
  font-size: 1.2vw;
}

/* Header section */
.start-page {
  display: flex;
  flex-direction: column;
  min-height: 96vh;
  font-family: sans-serif;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1.2vh;
  background-color: #b3b3b3;
  border: 0.2vh solid black;
  border-radius: 1.6vh;
}

.logo {
  font-size: 4vh;
  font-family: "Jaini Purva", serif;
}

.buttons {
  display: flex;
}

.button,
router-link {
  padding: 1vh 2vh;
  margin: 0 1vh;
  background-color: #4caf50;
  color: rgb(0, 0, 0);
  cursor: pointer;
  border-radius: 0.8vh;
  border: 0.2vh solid rgb(48, 48, 48);
  transition: background-color 0.3s ease, box-shadow 0.3s ease;
  text-decoration: none;
  font-size: 1vw;
}

.button:hover,
router-link:hover {
  background-color: #3c963f;
  box-shadow: 0 1vh 2vh rgba(0, 0, 0, 0.2);
}

/* Main section */
.content {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 1vh;
}

.description {
  text-align: center;
  max-width: 50vw;
}

/* Footer section */
.footer {
  padding: 1.2vh;
  background-color: #b3b3b3;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border: 0.2vh solid black;
  border-radius: 1.6vh;
}

.footer-content {
  display: flex;
  justify-content: space-between;
  width: 60vw;
}

/* Left part of footer */
.contacts {
  display: flex;
  flex-direction: column;
}

.contacts > h3 {
  margin: 1vh 0;
}

.social-links {
  display: flex;
  justify-content: space-between;
}

.social-link {
  display: flex;
  align-items: center;
}

.social-link > .icon {
  width: 2vw;
  height: 4vh;
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

/* Mid part of footer*/
.contact-info {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  justify-content: space-between;
}

.contact-item {
  display: flex;
  align-items: center;
  margin-bottom: 0;
}

.contact-item > .icon {
  width: 2vw;
  height: 4vh;
  margin-right: 1vw;
}

/* Right part of footer*/
.copyright {
  font-size: 2.4vh;
}

/* Адаптивні стилі */
@media (max-width: 768px) {
  .header {
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
  }

  .button {
    font-size: 1.2vw;
    padding: 1vh 2vw;
    margin: 1vh 0;
  }

  .logo {
    font-size: 4vh;
  }

  .description p {
    font-size: 2vw;
  }

  .footer {
    display: flex;
    align-items: center;
    height: auto;
    flex-direction: column;
  }

  .copyright {
    margin-top: 4vh;
    text-align: center;
    width: 90vw;
  }

  .footer-content {
    width: 90vw;
  }
}
</style>
