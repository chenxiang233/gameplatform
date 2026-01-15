package xyz.cx233.game;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.mongodb.autoconfigure.MongoAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(
        // ⭐ 添加这两行，排除 MongoDB 的自动配置
        exclude = {
                MongoAutoConfiguration.class,
        }
)
@EnableScheduling
public class GameplatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(GameplatformApplication.class, args);
    }

}
