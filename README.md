# getOffer

Микросервисная система для расчета рекомендованного оффера

## Состав проекта

- `candidate-service` — регистрация и обновление кандидатов, публикация события `candidate-created`
- `calculation-service` — расчет коэффициентов и рекомендаций, публикация события `calculation-completed`
- `offer-service` — формирование и хранение офферов и их истории
- `docker-compose.yml` — запуск всей системы в Docker

## Архитектура

`candidate-service -> Kafka -> calculation-service -> Kafka -> offer-service`

Хранилища:

- PostgreSQL — данные кандидатов
- Redis — коэффициенты и временные расчетные данные
- MongoDB — офферы и история изменений
- Kafka — асинхронное взаимодействие сервисов

## Запуск

Из корня проекта:

```powershell
Copy-Item .env.example .env
docker compose up --build -d
```

## Полезные адреса

- `candidate-service`: `http://localhost:8080`
- `calculation-service`: `http://localhost:8081`
- `offer-service`: `http://localhost:8082`
- Swagger UI candidate-service: `http://localhost:8080/swagger-ui/index.html`
- Swagger UI offer-service: `http://localhost:8082/swagger-ui/index.html`
