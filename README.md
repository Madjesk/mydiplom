| № | Микросервис | За что отвечает |
|---|-------------|-----------------|
| 1 | **mail-orchestrator** | UI, аутентификация, загрузка групп для рассылок, проксирование запросов в другие микросервисы |
| 2 | **mail-sender** | Отправка писем, планирование отложенных рассылок |
| 3 | **bizproc-service** | CRUD бизнес-процессов, журнал действий, интеграция с внешними системами|
| 4 | **mail-stat** | Логирование рассылок, отслеживание статистики |

---


### PostgreSQL + Kafka

```bash
# PostgreSQL 16
docker run --name pg-diploma -p 5432:5432 \
  -e POSTGRES_DB=diploma \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -d postgres:16

# Kafka 
docker network create kafka-net
docker run -d --name zookeeper --network kafka-net \
  -e ALLOW_ANONYMOUS_LOGIN=yes bitnami/zookeeper:3.9
docker run -d --name kafka --network kafka-net -p 9092:9092 \
  -e KAFKA_ZOOKEEPER_CONNECT=zookeeper:2181 \
  -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092 \
  -e ALLOW_PLAINTEXT_LISTENER=yes bitnami/kafka:3.6

# топики 
docker exec -it kafka kafka-topics.sh --create --topic action-performed \
  --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1
docker exec -it kafka kafka-topics.sh --create --topic stage-completed \
  --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1
