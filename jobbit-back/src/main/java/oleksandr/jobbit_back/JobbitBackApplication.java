package oleksandr.jobbit_back;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Головний клас застосунку Spring Boot {@code JobbitBackApplication}.
 * Анотація {@code @SpringBootApplication} є зручною анотацією, яка об'єднує:
 * <ul>
 * <li>{@code @Configuration}: позначає клас як джерело бінів застосунку.</li>
 * <li>{@code @EnableAutoConfiguration}: вказує Spring Boot на необхідність автоматичної конфігурації
 * застосунку на основі доданих залежностей у файлі `pom.xml`.</li>
 * <li>{@code @ComponentScan}: вказує Spring сканувати поточний пакет (та його підпакети) на наявність
 * компонентів Spring ({@code @Component}, {@code @Service}, {@code @Repository}, {@code @Controller} тощо).</li>
 * </ul>
 * Містить головний метод {@code main}, який є точкою входу в застосунок та запускає Spring Boot.
 *
 * @author Oleksandr Borovyk
 */
@SpringBootApplication
public class JobbitBackApplication {

    /**
     * Головний метод застосунку. Викликається при запуску JAR-файлу або через IDE.
     * Ініціалізує та запускає Spring Boot застосунок за допомогою методу {@link SpringApplication#run(Class, String...)}.
     *
     * @param args Масив рядкових аргументів командного рядка, переданих при запуску застосунку.
     * Зазвичай не використовуються або використовуються для передачі специфічних конфігурацій.
     */
    public static void main(String[] args) {
        SpringApplication.run(JobbitBackApplication.class, args);
    }
}
