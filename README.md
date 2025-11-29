# 🚀 DevOps Demo Application

A production-ready Spring Boot application designed for learning DevOps practices. Features real-time WebSocket updates, comprehensive health checks, Prometheus metrics, and full CI/CD pipeline configuration.

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-green)
![License](https://img.shields.io/badge/License-MIT-blue)

## 📋 Features

### Application Features
- ✅ **RESTful API** - CRUD operations for task management
- ✅ **Real-time WebSocket** - Live metrics dashboard with STOMP/SockJS
- ✅ **Swagger/OpenAPI** - Interactive API documentation
- ✅ **H2 Database** - In-memory database for development
- ✅ **Caching** - Built-in response caching
- ✅ **Validation** - Request validation with error handling

### DevOps Features
- ✅ **Health Checks** - Kubernetes-ready liveness/readiness probes
- ✅ **Prometheus Metrics** - Custom metrics + JVM/system metrics
- ✅ **Docker** - Multi-stage optimized Dockerfile
- ✅ **Docker Compose** - Full stack with Prometheus & Grafana
- ✅ **Kubernetes** - Complete manifests with HPA, PDB, NetworkPolicy
- ✅ **Helm Chart** - Production-ready Helm chart
- ✅ **GitHub Actions** - CI/CD pipeline with security scanning
- ✅ **JaCoCo** - Code coverage reporting

## 🏗️ Project Structure

```
devops-demo-app/
├── src/
│   ├── main/
│   │   ├── java/com/devops/demo/
│   │   │   ├── config/          # Configuration classes
│   │   │   ├── controller/      # REST controllers
│   │   │   ├── model/           # Entity models
│   │   │   ├── repository/      # Data repositories
│   │   │   ├── service/         # Business logic
│   │   │   └── websocket/       # WebSocket handlers
│   │   └── resources/
│   │       ├── static/          # Dashboard HTML
│   │       └── application.yml  # Configuration
│   └── test/                    # Unit & integration tests
├── .github/workflows/           # CI/CD pipelines
├── k8s/                         # Kubernetes manifests
├── helm/                        # Helm charts
├── monitoring/                  # Prometheus & Grafana config
├── Dockerfile                   # Container image
├── docker-compose.yml           # Local development stack
└── pom.xml                      # Maven configuration
```

## 🚀 Quick Start

### Prerequisites
- Java 17+
- Maven 3.8+
- Docker & Docker Compose (optional)

### Run Locally

```bash
# Clone and build
git clone <repository-url>
cd devops-demo-app

# Run with Maven
./mvnw spring-boot:run

# Or build and run JAR
./mvnw clean package
java -jar target/devops-demo-1.0.0.jar
```

Access the application:
- 🌐 **Dashboard**: http://localhost:8080
- 📚 **Swagger UI**: http://localhost:8080/swagger-ui.html
- 📊 **Prometheus Metrics**: http://localhost:8080/actuator/prometheus
- 💚 **Health Check**: http://localhost:8080/actuator/health

### Run with Docker

```bash
# Build and run single container
docker build -t devops-demo .
docker run -p 8080:8080 devops-demo

# Or run full stack with monitoring
docker-compose up -d
```

Full stack endpoints:
- 🌐 **Application**: http://localhost:8080
- 📈 **Prometheus**: http://localhost:9090
- 📊 **Grafana**: http://localhost:3000 (admin/admin123)

## 📡 API Endpoints

### Tasks API
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/tasks` | Get all tasks |
| GET | `/api/v1/tasks/{id}` | Get task by ID |
| POST | `/api/v1/tasks` | Create a task |
| PUT | `/api/v1/tasks/{id}` | Update a task |
| DELETE | `/api/v1/tasks/{id}` | Delete a task |
| GET | `/api/v1/tasks/status/{status}` | Get tasks by status |
| GET | `/api/v1/tasks/statistics` | Get task statistics |

### System API
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/info` | Application info |
| GET | `/api/v1/health/live` | Liveness probe |
| GET | `/api/v1/health/ready` | Readiness probe |
| GET | `/api/v1/metrics/system` | System metrics |
| GET | `/api/v1/simulate/load` | Simulate CPU load |
| GET | `/api/v1/simulate/memory` | Simulate memory usage |

### Actuator Endpoints
| Endpoint | Description |
|----------|-------------|
| `/actuator/health` | Health status |
| `/actuator/info` | Application info |
| `/actuator/prometheus` | Prometheus metrics |
| `/actuator/metrics` | Micrometer metrics |

## 🔌 WebSocket

Connect to real-time updates:

```javascript
const socket = new SockJS('/ws');
const stompClient = Stomp.over(socket);

stompClient.connect({}, function() {
    // Subscribe to metrics (updates every 2 seconds)
    stompClient.subscribe('/topic/metrics', function(message) {
        console.log(JSON.parse(message.body));
    });

    // Subscribe to task updates
    stompClient.subscribe('/topic/tasks', function(message) {
        console.log(JSON.parse(message.body));
    });
});
```

## ☸️ Kubernetes Deployment

### Using kubectl
```bash
kubectl apply -f k8s/deployment.yaml
```

### Using Helm
```bash
helm install devops-demo ./helm/devops-demo \
  --namespace devops-demo \
  --create-namespace
```

## 📊 Monitoring

### Prometheus Queries

```promql
# CPU usage rate
rate(process_cpu_usage[5m])

# Memory usage
jvm_memory_used_bytes{area="heap"}

# HTTP request rate
rate(http_server_requests_seconds_count[5m])

# Custom task metrics
tasks_created_total
tasks_completed_total
```

### Key Metrics Exposed
- `tasks.created.total` - Total tasks created
- `tasks.completed.total` - Total tasks completed
- `tasks.operation.duration` - Task operation timing
- Standard JVM metrics (memory, GC, threads)
- Spring Boot Actuator metrics

## 🔧 Configuration

### Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `PORT` | 8080 | Server port |
| `APP_ENV` | development | Environment name |
| `SPRING_PROFILES_ACTIVE` | default | Active profile |
| `DATABASE_URL` | H2 in-memory | Database connection |
| `DATABASE_USER` | sa | Database username |
| `DATABASE_PASSWORD` | - | Database password |

### Profiles

- **default**: H2 in-memory database, debug logging
- **production**: PostgreSQL, optimized settings

## 🧪 Testing

```bash
# Run all tests
./mvnw test

# Run with coverage report
./mvnw test jacoco:report

# View coverage report
open target/site/jacoco/index.html
```

## 🛠️ DevOps Learning Topics

This application helps you practice:

1. **Containerization**
   - Multi-stage Docker builds
   - Image optimization
   - Security best practices

2. **CI/CD**
   - GitHub Actions workflows
   - Build, test, scan, deploy stages
   - Environment management

3. **Kubernetes**
   - Deployments, Services, Ingress
   - ConfigMaps and Secrets
   - HPA autoscaling
   - Pod Disruption Budgets
   - Network Policies

4. **Observability**
   - Prometheus metrics collection
   - Grafana dashboards
   - Structured logging
   - Health checks

5. **Infrastructure as Code**
   - Helm charts
   - Kubernetes manifests
   - Docker Compose

## 📝 License

MIT License - feel free to use for learning and experimentation!

## 🤝 Contributing

Contributions welcome! Please feel free to submit issues and pull requests.

---

**Happy DevOps Learning! 🎉**
