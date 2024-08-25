# 1단계: 빌드를 위한 Gradle 이미지
FROM gradle:8.8-jdk17 AS builder

# 애플리케이션 소스 코드 작업 디렉토리 설정
WORKDIR /overclock

# 소스 코드 복사
COPY --chown=gradle:gradle . .

# Gradle 빌드 수행 (JAR 파일 생성)
RUN gradle clean bootJar --no-daemon

# 2단계: 실행을 위한 경량 JDK 이미지
FROM openjdk:17-jdk-slim

# 애플리케이션 실행 디렉토리 설정
WORKDIR /overclock

# 빌드 단계에서 생성된 JAR 파일을 복사
COPY --from=builder /overclock/build/libs/*.jar movetomove.jar

# 컨테이너 시작 시 애플리케이션 실행 명령어
ENTRYPOINT ["java", "-jar", "mom.jar"]
