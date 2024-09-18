package com.kosa.kosafinalprojbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync  // 비동기 처리를 활성화
public class KosaFinalProjBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(KosaFinalProjBackendApplication.class, args);
    }

}
