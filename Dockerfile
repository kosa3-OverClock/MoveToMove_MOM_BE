# 1단계: 빌드를 위한 Gradle 이미지
FROM gradle:8.8-jdk17 AS builder

# Gradle 종속성 캐시 디렉토리 설정
WORKDIR /home/gradle/src

# 종속성 파일만 먼저 복사하고 캐싱을 위한 설정
COPY build.gradle.kts settings.gradle.kts ./

# Gradle 종속성 캐싱
RUN gradle --no-daemon dependencies

# 나머지 소스 코드 복사
COPY --chown=gradle:gradle . .

# Gradle 빌드 수행 (JAR 파일 생성)
RUN gradle clean bootJar --no-daemon

# 2단계: 실행을 위한 경량 JDK 이미지
FROM openjdk:17-jdk-alpine

# 애플리케이션 실행 디렉토리 설정
WORKDIR /overclock

# 빌드 단계에서 생성된 JAR 파일을 복사
COPY --from=builder /home/gradle/src/build/libs/*.jar movetomove.jar

# 컨테이너 시작 시 애플리케이션 실행 명령어
ENTRYPOINT ["java", "-jar", "movetomove.jar"]
