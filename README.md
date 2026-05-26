# Franchise API — Prueba Técnica Accenture

API reactiva para gestión de franquicias, sucursales y productos.  
Construida con **Java 17 · Spring Boot 3 · WebFlux · R2DBC · MySQL · Docker · Terraform (AWS)**.


 API : DESPLEGADA : http://3.239.45.141:8080/swagger-ui/index.html#/

---

##  Tabla de Contenidos

- [Arquitectura](#arquitectura)
- [Tecnologías y Dependencias](#tecnologías-y-dependencias)
- [Requisitos Previos](#requisitos-previos)
- [Despliegue con Docker](#despliegue-con-docker-recomendado)
- [Despliegue Local sin Docker](#despliegue-local-sin-docker)
- [Documentación Swagger](#documentación-swagger)
- [Endpoints](#endpoints-de-la-api)
- [Ejemplos de Peticiones](#ejemplos-de-peticiones)
- [Tests Unitarios](#tests-unitarios)
- [Despliegue en AWS con Terraform](#despliegue-en-aws-con-terraform)
- [Criterios Cumplidos](#criterios-cumplidos)

---

##  Arquitectura

El proyecto sigue **Clean Architecture** con separación estricta en capas:

```
src/main/java/
└── com/accenture/franchise/
    ├── domain/                        # Núcleo — sin dependencias externas
    │   ├── model/                     # Entidades de dominio (Franchise, Branch, Product)
    │   ├── repository/                # Puertos (interfaces de repositorio)
    │   └── service/                   # Servicios de dominio con lógica de negocio
    │
    ├── application/                   # Casos de uso y DTOs
    │   ├── usecase/                   # FranchiseUseCase, BranchUseCase, ProductUseCase
    │   └── dto/                       # Request/Response DTOs
    │
    ├── infrastructure/                # Adaptadores externos
    │   ├── persistence/               # Entidades R2DBC, repositorios, mappers
    │   ├── web/
    │   │   ├── controller/            # RestController (Swagger)
    │   │   ├── handler/               # Handlers funcionales reactivos
    │   │   └── router/                # Router funcional WebFlux
    │   └── config/                    # Configuración Swagger, WebFlux
    │
    └── shared/exception/              # Manejo global de errores
```

**Flujo de dependencias:** `Infrastructure → Application → Domain`  
El dominio no depende de ninguna capa externa.

---

## 🛠️ Tecnologías y Dependencias

| Dependencia | Versión | Propósito |
|---|---|---|
| `spring-boot-starter-webflux` | 3.x | Web reactiva con Netty |
| `spring-boot-starter-data-r2dbc` | 3.x | Acceso reactivo a BD |
| `r2dbc-mysql` (io.asyncer) | 1.1.0 | Driver R2DBC para MySQL 8 |
| `mysql-connector-j` | 8.x | Driver JDBC para Flyway |
| `flyway-core` + `flyway-mysql` | — | Migraciones automáticas de esquema |
| `spring-boot-starter-validation` | 3.x | Validaciones en DTOs |
| `spring-boot-starter-actuator` | 3.x | Health endpoint |
| `springdoc-openapi-starter-webflux-ui` | 2.5.0 | Documentación Swagger UI |
| `reactor-test` | — | StepVerifier para tests reactivos |
| `r2dbc-h2` + `h2` | — | BD en memoria para tests |

---

##  Requisitos Previos

### Para Docker (recomendado)
- [Docker Desktop](https://www.docker.com/products/docker-desktop/) instalado y corriendo
- Git

### Para ejecución local
- Java 17+
- Maven 3.8+
- MySQL 8.0

### Para despliegue en AWS
- [Terraform >= 1.5](https://developer.hashicorp.com/terraform/downloads)
- [AWS CLI](https://aws.amazon.com/cli/) configurado

---

##  Despliegue con Docker (recomendado)

La forma más sencilla de correr el proyecto. No necesitas MySQL instalado localmente.

```bash
# 1. Clonar el repositorio
git clone https://github.com/<tu-usuario>/franchise-api.git
cd franchise-api

# 2. Levantar MySQL + API con Docker Compose
docker-compose up --build
```

Docker Compose levanta automáticamente:
- **MySQL 8.0** en el puerto `3307` (externo) / `3306` (interno)
- **franchise-api** en el puerto `8080` (espera a que MySQL esté listo con healthcheck)

La API estará disponible en: `http://localhost:8080`

Para detener:
```bash
docker-compose down
```

---

##  Despliegue Local sin Docker

```bash
# 1. Crear la base de datos en MySQL
mysql -u root -p -e "CREATE DATABASE franchise_db;"

# 2. Editar src/main/resources/application.yml con tus credenciales:
#    spring.r2dbc.password y spring.flyway.password

# 3. Compilar
mvn clean package -DskipTests

# 4. Ejecutar
java -jar target/*.jar
```

O directamente desde IntelliJ con estas variables de entorno en Run Configuration:
```
DB_HOST=localhost
DB_PORT=3306
DB_NAME=franchise_db
DB_USER=root
DB_PASSWORD=tu_password
```

---

##  Documentación Swagger

Con la aplicación corriendo, accede a:

```
http://localhost:8080/swagger-ui.html
```

La documentación está organizada en 3 grupos:
- **Franquicias** — CRUD de franquicias
- **Sucursales** — gestión de sucursales por franquicia
- **Productos** — gestión de productos por sucursal

---

##  Endpoints de la API

Base URL: `http://localhost:8080/api/v1`

### Franquicias
| Método | Ruta | Descripción | Body |
|--------|------|-------------|------|
| `POST` | `/franchises` | Crear franquicia | `{"name": "string"}` |
| `GET` | `/franchises` | Listar todas | — |
| `GET` | `/franchises/{id}` | Obtener por ID | — |
| `PATCH` | `/franchises/{id}/name` | Actualizar nombre | `{"name": "string"}` |

### Sucursales
| Método | Ruta | Descripción | Body |
|--------|------|-------------|------|
| `POST` | `/branches` | Crear sucursal | `{"name": "string", "franchiseId": 1}` |
| `GET` | `/franchises/{franchiseId}/branches` | Sucursales de una franquicia | — |
| `PATCH` | `/branches/{id}/name` | Actualizar nombre | `{"name": "string"}` |

### Productos
| Método | Ruta | Descripción | Body |
|--------|------|-------------|------|
| `POST` | `/products` | Crear producto | `{"name": "string", "stock": 0, "branchId": 1}` |
| `DELETE` | `/products/{id}` | Eliminar producto | — |
| `PATCH` | `/products/{id}/stock` | Modificar stock | `{"stock": 100}` |
| `PATCH` | `/products/{id}/name` | Actualizar nombre | `{"name": "string"}` |
| `GET` | `/franchises/{franchiseId}/top-stock-products` | Producto con más stock por sucursal | — |

---

## 📋 Ejemplos de Peticiones

```bash
# Crear franquicia
curl -X POST http://localhost:8080/api/v1/franchises \
  -H "Content-Type: application/json" \
  -d '{"name": "McDonald'\''s Colombia"}'

# Respuesta: {"id": 1, "name": "McDonald's Colombia"}

# Crear sucursal
curl -X POST http://localhost:8080/api/v1/branches \
  -H "Content-Type: application/json" \
  -d '{"name": "Sucursal Norte", "franchiseId": 1}'

# Crear producto
curl -X POST http://localhost:8080/api/v1/products \
  -H "Content-Type: application/json" \
  -d '{"name": "BigMac", "stock": 150, "branchId": 1}'

# Modificar stock
curl -X PATCH http://localhost:8080/api/v1/products/1/stock \
  -H "Content-Type: application/json" \
  -d '{"stock": 200}'

# Eliminar producto
curl -X DELETE http://localhost:8080/api/v1/products/1

# Producto con más stock por sucursal (franquicia id=1)
curl http://localhost:8080/api/v1/franchises/1/top-stock-products

# Respuesta:
# [
#   {"productId": 1, "productName": "BigMac", "stock": 200, "branchId": 1, "branchName": "Sucursal Norte"},
#   {"productId": 3, "productName": "McFlurry", "stock": 80, "branchId": 2, "branchName": "Sucursal Sur"}
# ]

# Actualizar nombre de franquicia
curl -X PATCH http://localhost:8080/api/v1/franchises/1/name \
  -H "Content-Type: application/json" \
  -d '{"name": "Burger King Colombia"}'
```

---

##  Tests Unitarios

```bash
mvn test
```

Los tests cubren los casos de uso con `StepVerifier` de Project Reactor:

| Test | Casos cubiertos |
|---|---|
| `FranchiseUseCaseTest` | Crear, obtener, listar, actualizar nombre, not found |
| `BranchUseCaseTest` | Crear, actualizar nombre, franquicia no encontrada |
| `ProductUseCaseTest` | Crear, eliminar, actualizar stock, top stock por franquicia |

---

##  Despliegue en AWS con Terraform

### Infraestructura creada

```
AWS
├── VPC (10.0.0.0/16)
│   ├── Subnet pública A (us-east-1a)
│   └── Subnet pública B (us-east-1b)
├── Security Groups (app + rds)
├── Internet Gateway
├── RDS MySQL 8.0 (db.t3.micro)
├── ECR Repository
├── ECS Fargate Cluster
│   └── Task Definition + Service
├── IAM Role (ECS Task Execution)
└── CloudWatch Log Group
```

### Pasos

```bash
# 1. Configurar AWS CLI
aws configure

# 2. Obtener Account ID
aws sts get-caller-identity --query Account --output text

# 3. Subir imagen a ECR
aws ecr get-login-password --region us-east-1 | \
  docker login --username AWS --password-stdin <ACCOUNT_ID>.dkr.ecr.us-east-1.amazonaws.com

docker tag franchise-api:latest \
  <ACCOUNT_ID>.dkr.ecr.us-east-1.amazonaws.com/franchise-api:latest

docker push <ACCOUNT_ID>.dkr.ecr.us-east-1.amazonaws.com/franchise-api:latest

# 4. Aplicar Terraform
cd terraform
terraform init

terraform apply \
  -var="db_password=MiPassword123!" \
  -var="ecr_image_uri=<ACCOUNT_ID>.dkr.ecr.us-east-1.amazonaws.com/franchise-api:latest"

# 5. Destruir infraestructura (cuando no se necesite)
terraform destroy \
  -var="db_password=MiPassword123!" \
  -var="ecr_image_uri=<ACCOUNT_ID>.dkr.ecr.us-east-1.amazonaws.com/franchise-api:latest"
```

---

##  Criterios Cumplidos

| Criterio | Estado | Detalle |
|---|---|---|
| Spring Boot | ✅ | Spring Boot 3.x con WebFlux |
| Programación reactiva | ✅ | Mono/Flux en toda la cadena, R2DBC, Netty |
| Agregar franquicia | ✅ | `POST /api/v1/franchises` |
| Agregar sucursal | ✅ | `POST /api/v1/branches` |
| Agregar producto | ✅ | `POST /api/v1/products` |
| Eliminar producto | ✅ | `DELETE /api/v1/products/{id}` |
| Modificar stock | ✅ | `PATCH /api/v1/products/{id}/stock` |
| Top stock por sucursal | ✅ | `GET /api/v1/franchises/{id}/top-stock-products` |
| Persistencia MySQL | ✅ | R2DBC + Flyway migrations |
| Docker | ✅ | Dockerfile multi-stage + docker-compose |
| Unit Tests | ✅ | 13 tests con StepVerifier |
| Infrastructure as Code | ✅ | Terraform — AWS RDS + ECS Fargate |
| Clean Architecture | ✅ | Domain / Application / Infrastructure |
| Actualizar nombre franquicia | ✅ ⭐ | `PATCH /api/v1/franchises/{id}/name` |
| Actualizar nombre sucursal | ✅ ⭐ | `PATCH /api/v1/branches/{id}/name` |
| Actualizar nombre producto | ✅ ⭐ | `PATCH /api/v1/products/{id}/name` |
| Swagger UI | ✅ ⭐ | `http://localhost:8080/swagger-ui.html` |

---

##  Autor

**Antony Mendoza**  
Prueba técnica Backend — Accenture
correo: antoni-6191@hotmail.com

