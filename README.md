# CRM-Система для управления продавцами и транзакциями 

## Описание проекта
Данный проект представляет собой RESTful API систему для управления продавцами, транзакциями и аналитикой продаж.
Система реализует полный набор методов для управления данными, сгруппированных по отдельным сервисам.
Для работы с большими объемами информации используется пагинация.
Все входящие данные проходят валидацию, а возникающие ошибки централизованно обрабатываются глобальным обработчиком с возвратом ответа клиенту.

## Функционал

### Управление продавцами (/sellers)
- Получение списка продавцов (с поддержкой пагинации и сортировки)
- Получение информации о продавце по ID
- Создание нового продавца
- Обновление данных продавца
- Удаление продавца

### Управление транзакциями (/transactions)
- Получение списка всех транзакций (с пагинацией)
- Получение транзакций конкретного продавца
- Получение информации о транзакции по ID
- Создание транзакции

### Аналитика (/analytics)
- Определение самого продуктивного продавца за выбранный период
- Поиск продавцов с суммой транзакций ниже заданного значения
- Определение наиболее прибыльного временного периода (день, месяц, квартал, год) для конкретного продавца в заданных временных границах

## Команды сборки и запуска
1. Сборка и запуск JAR
- Запуск контейнера базы данных
```
docker-compose up -d
```
- Сборка проекта
```
./gradlew clean build
```
- Запуск приложения
```
java -jar build/libs/Shiftlab-Test-0.0.1.jar
```

2. Сборка и запуск в Docker
- Запуск контейнера базы данных
```
docker-compose up -d postgres
```
- Сборка проекта
```
./gradlew clean build
```
- Запуск всех контейнеров (БД + приложение)
```
docker-compose up -d
```

## Технологический стек
- Java 21
- Spring Boot 3.5.13
- Spring Data JPA: работа с базой данных
- Spring Validation: валидация входных данных
- PostgreSQL: реляционная база данных
- Gradle: система сборки проекта
- Lombok: уменьшение boilerplate-кода
- MapStruct: маппинг DTO и сущностей
- SpringDoc OpenAPI: генерация документации API
- Docker & Docker Compose: контейнеризация приложения

## Зависимости проекта
Основные зависимости (подробнее в `build.gradle`):
```
implementation 'org.springframework.boot:spring-boot-starter-web'
implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
implementation 'org.springframework.boot:spring-boot-starter-validation'

runtimeOnly 'org.postgresql:postgresql'

implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.16'

implementation 'org.mapstruct:mapstruct:1.5.5.Final'
annotationProcessor 'org.mapstruct:mapstruct-processor:1.5.5.Final'

compileOnly 'org.projectlombok:lombok'
annotationProcessor 'org.projectlombok:lombok'
```

## Тестирование
Тестами покрыты контроллеры, сервисный слой, репозитории и глобальный обработчик ошибок.

Для тестирования использовались:
- MockMvc: для тестирования REST-контроллеров
- Mockito: для unit-тестов сервисного слоя
- Testcontainers: для интеграционного тестирования репозиториев с реальной PostgreSQL базой

## Документация API
Swagger UI доступен по адресу:
http://localhost:8080/swagger-ui.html

## API Эндпоинты

### Sellers
```
POST /sellers               # Создать продавца
GET  /sellers               # Получить всех продавцов
GET  /sellers/{sellerId}    # Получить продавца по ID
PUT  /sellers/{sellerId}    # Обновить информацию о продавце по ID
DELETE /sellers/{sellerId}   # Удалить продавца
```

### Transactions
```
POST /transactions                     # Создать транзакцию
GET  /transactions                     # Получить все транзакции
GET  /transactions/{transactionId}    # Получить транзакцию по ID
GET  /transactions/seller/{sellerId}   # Получить транзакции продавца
```

### Analytics
```
GET /analytics/most-productive/{period}   # Самый продуктивный продавец
GET /analytics/sellers/total-less-than    # Продавцы с суммой транзакций меньше N
GET /analytics/best-period/{sellerId}     # Лучший период продавца
```

