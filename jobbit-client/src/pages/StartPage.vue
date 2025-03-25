<template>
  <div class="start-page">
    <header class="header">
      <h1 class="logo">JobbIT - find your IT-way</h1>
      <div class="buttons">
        <router-link to="/auth/register" class="button registration">Реєстрація</router-link>
        <router-link to="/auth/login" class="button authorization">Авторизація</router-link>
      </div>
    </header>

    <main class="content">
      <div class="description">
        <p>
          JobbIT - це сучасна система як для пошуку вакансій кандидатами, так і для знаходження потрібних кандидатів
          HR-працівниками компаній.
        </p>
        <p>Платформа надає великий вибір вакансій, для всіх видів розробників.</p>
      </div>
    </main>

    <footer class="footer">
      <div class="footer-content">
        <div class="contacts">
          <h3>Контакти:</h3>
          <div class="social-links">
            <a href="#" class="social-link">
              <img src="@/assets/telegram.png" alt="Telegram" class="icon" />
            </a>
            <a href="#" class="social-link">
              <img src="@/assets/github.png" alt="GitHub" class="icon" />
            </a>
          </div>
        </div>
        <div class="contact-info">
          <div class="contact-item">
            <img src="@/assets/phone.png" alt="Phone" class="icon" />
            <span> +380xx-xxx-xx-xx</span>
          </div>
          <div class="contact-item">
            <img src="@/assets/gmail.png" alt="Email" class="icon" />
            <span> xxxxxxxxxxxxxxx@gmail.com</span>
          </div>
        </div>
      </div>
      <div class="copyright">© 2025 JobbIT. Всі права захищено.</div>
    </footer>
  </div>
</template>

<script setup></script>

<style scoped>
/* Header section */
.start-page {
  display: flex;
  flex-direction: column;
  min-height: 97vh;
  font-family: sans-serif;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px;
  background-color: #dfdfdf;
  border: 1px solid black;
  border-radius: 4px;
}

.logo {
  font-size: 32px;
  font-family: "Jaini Purva";
}

.buttons {
  display: flex;
}

.button,
router-link {
  padding: 10px 20px;
  margin: 0 10px;
  background-color: #4caf50;
  color: rgb(0, 0, 0);
  cursor: pointer;
  border-radius: 4px;
  border: 1px solid rgb(48, 48, 48);
  transition: background-color 0.3s ease, box-shadow 0.3s ease;
  text-decoration: none;
}

.button:hover,
router-link:hover {
  background-color: #3c963f;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

/* Main section */
.content {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 20px;
}

.description {
  text-align: center;
  max-width: 600px;
}

/* Footer section */
.footer {
  padding: 10px;
  background-color: #dfdfdf;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border: 1px solid black;
  border-radius: 4px;
}

.footer-content {
  display: flex;
  justify-content: space-between;
  width: 60%;
}

/* Left part of footer */
.contacts {
  display: flex;
  flex-direction: column;
}

.contacts > h3 {
  margin: 4px 0;
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
  width: 24px;
  height: 24px;
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
  margin-bottom: 5px;
}

.contact-item > .icon {
  width: 24px;
  height: 24px;
  margin-right: 12px;
}

/* Right part of footer*/
.copyright {
  font-size: 12px;
}

/* Адаптивні стилі */
@media (max-width: 768px) {
  .header {
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
  }

  .buttons {
    display: flex;
    flex-direction: column;
    margin: 0;
  }

  .button {
    font-size: 14px;
    padding: 8px 16px;
    margin: 5px 0;
  }

  .logo {
    font-size: 16px;
  }

  .description p {
    font-size: 14px;
  }

  .footer {
    display: flex;
    align-items: center;
    height: auto;
    flex-direction: column;
  }

  .copyright {
    margin-top: 20px;
    text-align: center;
    width: 100%;
  }

  .footer-content {
    width: 100%;
  }
}
</style>
