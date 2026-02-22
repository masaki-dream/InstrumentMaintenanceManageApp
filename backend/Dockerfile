# JDK17 + Alpine Linux
FROM eclipse-temurin:17-jdk-alpine

# 作業ディレクトリ
WORKDIR /app

# プロジェクト全体をコピー
COPY . .

# Gradleビルド
RUN ./gradlew build

# 起動ポート
EXPOSE 8080

# 起動コマンド
CMD ["java", "-jar", "build/libs/InstrumentMaintenanceManageApp-0.0.1-SNAPSHOT.jar"]