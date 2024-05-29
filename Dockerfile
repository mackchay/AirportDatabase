# Используем базовый образ с установленной JDK
FROM openjdk:21

# Устанавливаем переменную окружения для Gradle
ENV GRADLE_USER_HOME /cache

# Устанавливаем рабочую директорию внутри контейнера
WORKDIR /app

# Копируем файлы Gradle проекта
COPY . .

# Собираем проект с помощью Gradle
RUN ./gradlew clean build

# Определяем команду для запуска вашего приложения
CMD ["java", "-jar", "build/jars/airportDatabase.jar"]


