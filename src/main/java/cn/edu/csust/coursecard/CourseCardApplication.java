package cn.edu.csust.coursecard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class CourseCardApplication {

    public static void main(String[] args) {
        SpringApplication.run(CourseCardApplication.class, args);
    }

}
