# JDK17 이미지 사용
FROM openjdk:17-jdk-alpine

VOLUME /tmp

# JAR 파일 경로 지정 (Gradle 빌드 결과물)
ARG JAR_FILE=./build/libs/kosa-FinalProj-backend-0.0.1-SNAPSHOT.jar

# JAR 파일을 컨테이너 내부에 복사
COPY ${JAR_FILE} mom.jar

# 기본 포트를 환경 변수로 설정
ENV SERVER_PORT=8080

# 실행 시 동적으로 서버 포트를 설정
ENTRYPOINT ["java", "-jar", "mom.jar", "--server.port=${SERVER_PORT}"]