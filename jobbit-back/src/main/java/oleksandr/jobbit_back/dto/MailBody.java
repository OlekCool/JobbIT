package oleksandr.jobbit_back.dto;

/**
 * DTO (Data Transfer Object) для представлення тіла електронного листа.
 * Містить інформацію про отримувача, тему та вміст листа.
 * Використовується для передачі даних, необхідних для надсилання електронних повідомлень.
 *
 * @author Oleksandr Borovyk
 */
public class MailBody {

    private String to;
    private String subject;
    private String text;

    /**
     * Конструктор за замовчуванням для об'єкта {@code MailBody}.
     * Не приймає жодних аргументів.
     */
    public MailBody() {
    }

    /**
     * Конструктор для створення об'єкта {@code MailBody} з вказаними отримувачем, темою та текстом листа.
     *
     * @param to Електронна пошта отримувача.
     * @param subject Тема електронного листа.
     * @param text Вміст електронного листа.
     */
    public MailBody(String to, String subject, String text) {
        this.to = to;
        this.subject = subject;
        this.text = text;
    }

    /**
     * Гетер для поля to.
     * @return to Кому надсилається повідомлення.
     */
    public String getTo() {
        return to;
    }

    /**
     * Сетер для поля to.
     * @param to Кому надсилається повідомлення.
     */
    public void setTo(String to) {
        this.to = to;
    }

    /**
     * Гетер для поля subject.
     * @return subject Тема повідомлення.
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Сетер для поля to.
     * @param subject Тема повідомлення.
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * Гетер для поля text.
     * @return text Текст повідомлення.
     */
    public String getText() {
        return text;
    }

    /**
     * Сетер для поля to.
     * @param text Текст повідомлення.
     */
    public void setText(String text) {
        this.text = text;
    }
}
