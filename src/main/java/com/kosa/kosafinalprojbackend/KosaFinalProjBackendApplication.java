package com.kosa.kosafinalprojbackend;

import java.util.TimeZone;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync  // 비동기 처리를 활성화
public class KosaFinalProjBackendApplication {

    public static void main(String[] args) {
        // 애플리케이션 시작 시 타임존 설정
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
        SpringApplication.run(KosaFinalProjBackendApplication.class, args);
    }

}
