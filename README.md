Franchise API — Prueba Técnica Accenture
API reactiva para gestión de franquicias, sucursales y productos.
Construida con Java 17 · Spring Boot 3 · WebFlux · R2DBC · MySQL · Docker · Terraform (AWS).



 Tabla de Contenidos

Arquitectura
Tecnologías y Dependencias
Requisitos Previos
Despliegue con Docker
Despliegue Local sin Docker
Documentación Swagger
Endpoints
Ejemplos de Peticiones
Tests Unitarios
Despliegue en AWS con Terraform
Criterios Cumplidos

Arquitectura
El proyecto sigue Clean Architecture con separación estricta en capas:
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



    Requisitos Previos
Para Docker (recomendado)

Docker Desktop instalado y corriendo
Git

Para ejecución local

Java 17+
Maven 3.8+
MySQL 8.0

Para despliegue en AWS

Terraform >= 1.5
AWS CLI configurado

Despliegue con Docker (recomendado)
La forma más sencilla de correr el proyecto. No necesitas MySQL instalado localmente.

# 1. Clonar el repositorio
git clone : https://github.com/antony-mendoza-proyect01/Prueba_Backend_Accenture.git
cd franchise-api

# 2. Levantar MySQL + API con Docker Compose
docker-compose up --build

Docker Compose levanta automáticamente:

MySQL 8.0 en el puerto 3307 (externo) / 3306 (interno)
franchise-api en el puerto 8080 (espera a que MySQL esté listo con healthcheck)

La API estará disponible en: http://localhost:8080

Para detener:
bashdocker-compose down

Despliegue Local sin Docker

# 1. Crear la base de datos en MySQL
mysql -u root -p -e "CREATE DATABASE franchise_db;"

# 2. Editar src/main/resources/application.yml con tus credenciales:
#    spring.r2dbc.password y spring.flyway.password

# 3. Compilar
mvn clean package -DskipTests

# 4. Ejecutar
java -jar target/*.jar

O directamente desde IntelliJ con estas variables de entorno en Run Configuration:
DB_HOST=localhost
DB_PORT=3306
DB_NAME=franchise_db
DB_USER=root
DB_PASSWORD=tu_password


Documentación Swagger
Con la aplicación corriendo, accede a:
http://localhost:8080/swagger-ui.html
La documentación está organizada en 3 grupos:

Franquicias — CRUD de franquicias
Sucursales — gestión de sucursales por franquicia
Productos — gestión de productos por sucursal


Base URL: http://localhost:8080/api/v1
Franquicias
MétodoRutaDescripciónBodyPOST/franchisesCrear franquicia{"name": "string"}GET/franchisesListar todas—GET/franchises/{id}Obtener por ID—PATCH/franchises/{id}/nameActualizar nombre{"name": "string"}
Sucursales
MétodoRutaDescripciónBodyPOST/branchesCrear sucursal{"name": "string", "franchiseId": 1}GET/franchises/{franchiseId}/branchesSucursales de una franquicia—PATCH/branches/{id}/nameActualizar nombre{"name": "string"}
Productos
MétodoRutaDescripciónBodyPOST/productsCrear producto{"name": "string", "stock": 0, "branchId": 1}DELETE/products/{id}Eliminar producto—PATCH/products/{id}/stockModificar stock{"stock": 100}PATCH/products/{id}/nameActualizar nombre{"name": "string"}GET/franchises/{franchiseId}/top-stock-productsProducto con más stock por sucursal—

Ejemplos de Peticiones
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


  Tests Unitarios

  mvn test

  Los tests cubren los casos de uso con StepVerifier de Project Reactor:
TestCasos cubiertosFranchiseUseCaseTestCrear, obtener, listar, actualizar nombre, not foundBranchUseCaseTestCrear, actualizar nombre, franquicia no encontradaProductUseCaseTestCrear, eliminar, actualizar stock, top stock por franquicia


Despliegue en AWS con Terraform
Infraestructura creada
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

  Criterios Cumplidos
1.	El proyecto debe ser desarrollado en Sprint Boot
2.	Exponer endpoint para agregar una nueva franquicia
3.	Exponer endpoint para agregar una nueva sucursal a la franquicia
4.	Exponer endpoint para agregar un nuevo producto a la sucursal
5.	Exponer endpoint para eliminar un nuevo producto a una sucursal
6.	Exponer endpoint para modificar un Stock de un nuevo producto
7.	Exponer endpoint para agregar que permita mostrar cual es el producto que más stock tiene por sucursal para una franquicia puntual. Debe retoma un listado de productos que indiquen a que sucursal pertenece.
8.	Utilizar sistemas de persistencia de datos como Redis, MySql, Mongo BD, Dynamo en algún proveedor de nube. Ǫueda abierto a libre escogencia.
Puntos extra:
•	Plus si se empaqueta una aplicación con Docker
•	Plus si se utilizar una programación funcional, reactiva. Ǫueda abierto a libre escogencia.
•	Plus si se expone endpoint que permita actualizar el nombre de la franquicia.
•	Plus si se expone endpoint que permita actualizar el nombre de la sucursal.
•	Plus si se expone endpoint que permita actualizar el nombre del producto.
•	Plus si se aproviciona la persistencia de datos como infraestructura como código como Terrafom, Cloudformation, etc. Ǫueda a libre escogencia.
•	Plus si toda la solución se despliega en la nube
 se cumplio con todos los criterios 

 API : DESPLEGADA : http://3.239.45.141:8080/swagger-ui/index.html#/
