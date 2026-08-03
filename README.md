# Product CRUD — Spring MVC Demo

A minimal Spring Boot app demonstrating classic **Controller → Service → Repository** MVC layering, backed by Supabase (Postgres). One resource (`Product`), full CRUD, plus a barcode-scanner-friendly search endpoint.

```
[Browser page + USB scanner]
        |  JSON over HTTP
        v
  Controller  (@RestController - HTTP only, no business logic)
        |
     Service   (business rules)
        |
   Repository  (JpaRepository - DB access)
        |
     Supabase (Postgres)
```

## 1. Prerequisites

### Install Java 17

Spring Boot 3.x requires Java 17 or newer.

- **Windows**: download the installer from [Eclipse Temurin](https://adoptium.net/temurin/releases/?version=17) (choose JDK 17, `.msi` for Windows), run it, and make sure "Add to PATH" and "Set JAVA_HOME" are checked during install.
- Alternatively with `winget`:
  ```powershell
  winget install EclipseAdoptium.Temurin.17.JDK
  ```

Verify it worked (open a **new** terminal so PATH changes apply):
```powershell
java -version
```
You should see something like `openjdk version "17.0.x"`.

### Install Maven

- **Windows**: download the binary zip from [Maven's download page](https://maven.apache.org/download.cgi), extract it (e.g. to `C:\Program Files\Maven`), then add `<extract-path>\bin` to your `PATH` environment variable (System Properties → Environment Variables → edit `Path`).
- Alternatively with `winget`:
  ```powershell
  winget install Apache.Maven
  ```

Verify:
```powershell
mvn -v
```
You should see the Maven version and the Java version it's using (should match step above).

### Create a Supabase project

1. Sign up / log in at [supabase.com](https://supabase.com) and create a new project.
2. Once created, click **Connect** (top of the project dashboard) → **Connection String** → **URI** tab → select **Session pooler** (this works over plain IPv4, unlike the direct connection which is IPv6-only on many networks).
3. You'll get something like:
   ```
   postgresql://postgres.<project-ref>:[YOUR-PASSWORD]@aws-0-<region>.pooler.supabase.com:5432/postgres
   ```
   Note the three pieces you'll need: host+port, username (`postgres.<project-ref>`), and your database password.

## 2. Clone and set up the project

```powershell
git clone https://github.com/Thamel777/Spring-MVC.git
cd Spring-MVC
```

The database credentials are **not** stored in the repo — they're read from environment variables at startup (see [application.properties](src/main/resources/application.properties)). Set them in your terminal session before running the app:

```powershell
$env:SUPABASE_DB_URL="jdbc:postgresql://aws-0-<region>.pooler.supabase.com:5432/postgres"
$env:SUPABASE_DB_USERNAME="postgres.<project-ref>"
$env:SUPABASE_DB_PASSWORD="<your-db-password>"
```

Replace `<region>`, `<project-ref>`, and `<your-db-password>` with the values from your Supabase **Connect** dialog. These `$env:` variables only last for the current terminal session — you'll need to re-set them if you open a new window (or set them permanently via System Properties → Environment Variables).

## 3. Run it

```powershell
mvn spring-boot:run
```

On first run, Hibernate auto-creates the `products` table in your Supabase database (`spring.jpa.hibernate.ddl-auto=update`). Once you see `Started ProductCrudApplication`, open:

```
http://localhost:8081
```

You'll see a page with a barcode-scan input, an add/edit form, and a product table. A USB barcode scanner behaves like a keyboard (types the code, then presses Enter), so clicking into the scan field and scanning "just works."

## 4. API endpoints

| Method | Path | Description |
|---|---|---|
| POST | `/api/products` | Create a product |
| GET | `/api/products` | List all products |
| GET | `/api/products/{id}` | Get a product by id |
| GET | `/api/products/barcode/{barcode}` | Get a product by barcode (used by the scanner) |
| PUT | `/api/products/{id}` | Update a product |
| DELETE | `/api/products/{id}` | Delete a product |

## 5. Project structure

```
src/main/java/com/bci/productcrud/
  controller/   -> @RestController - HTTP in, HTTP out, no business logic
  service/      -> business rules (e.g. "barcode must be unique")
  repository/   -> JpaRepository interfaces - DB access, no SQL written by hand
  model/        -> Product entity (JPA-mapped domain object)
  exception/    -> centralized error handling (@RestControllerAdvice)
src/main/resources/
  application.properties -> config (DB connection, port, JPA settings)
  static/                -> the frontend: index.html + app.js + style.css
```

Request flow for a barcode scan: `app.js` → `GET /api/products/barcode/{code}` → `ProductController` → `ProductService` → `ProductRepository` → Supabase, and the `Product` JSON flows back the same path in reverse.
