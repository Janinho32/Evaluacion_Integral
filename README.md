[README.md](https://github.com/user-attachments/files/30293045/README.md)
# Sistema de Gestión Universitaria

Proyecto final del curso **Programación Orientada a Objetos**.  
Aplicación web en Java que gestiona estudiantes, docentes, cursos y matrículas, demostrando los principales conceptos del curso.

---

## Integrantes del equipo

| N° | Nombre                             | Código   |
|----|------------------------------------|----------|
| 1  | Alejandro Oswaldo Angulo Barranca  | 40813565 |
| 2  | Daniel Ramirez Espichan            | 73173200 |
| 3  | Sebastian Pacheco Ruiz             | 75166796 |
| 4  | Genesis Paloma Lucia Chuqqui Allca |          |

---

## Descripción del proyecto

Sistema web de gestión universitaria desarrollado con Java EE, Servlets y JSP (patrón MVC).  
Permite administrar estudiantes, docentes, cursos y matrículas con persistencia en base de datos mediante JDBC.

**Caso aplicado:** Gestión de admisión y rendimiento académico universitario.

---

## Tecnologías utilizadas

| Componente | Tecnología |
|------------|-----------|
| Lenguaje | Java 11 |
| Servidor web | Apache Tomcat 7 (vía Maven plugin) |
| Interfaz | JSP 2.3 + JSTL 1.2 + Bootstrap 5 |
| Persistencia | JDBC con H2 Database (embebida) |
| Construcción | Apache Maven 3 |
| Base de datos | H2 (archivo local, sin instalación externa) |

---

## Conceptos OOP demostrados

### 1. Herencia y Clase Abstracta
```
Persona (abstracta)
├── Estudiante      → implementa IEvaluable
└── Docente
```
`Persona` es la clase abstracta base. `Estudiante` y `Docente` la extienden y sobrescriben `getTipoPersona()`.

### 2. Interfaz
```java
public interface IEvaluable {
    double calcularPromedio();
    String getCategoria();
    boolean aprueba();
}
```
`Estudiante` implementa `IEvaluable`, demostrando polimorfismo de interfaz.

### 3. Encapsulamiento
Todos los atributos son `private` con getters y setters. La validación ocurre en los setters (`Math.max`, `Math.min`).

### 4. Polimorfismo
Al iterar una `List<Persona>`, cada objeto llama a su versión de `getTipoPersona()`, retornando `"Estudiante"` o `"Docente"` según el tipo real.

### 5. Colecciones (ArrayList, List)
- `EstudianteDAO.listarTodos()` retorna `List<Estudiante>`
- `Estudiante` mantiene `List<Double> notas` internamente
- Búsqueda, recorrido y ordenamiento con `Collections.sort()`

### 6. Clases nativas Math y String
**Math:**
- `Math.round()` — redondeo de promedios y porcentajes
- `Math.min()` / `Math.max()` — validación de rangos (notas, semestre, créditos)
- `Math.pow()` / `Math.sqrt()` — cálculo de desviación estándar
- `Math.ceil()` / `Math.floor()` — horas remuneradas, calificación

**String:**
- `String.trim()`, `String.toUpperCase()`, `String.toLowerCase()`
- `String.substring()` — capitalización, truncado de nombres
- `String.matches()` — validación de formatos de código
- `String.split()` / `String.format()` — formateo de datos
- `String.charAt()` — generación de iniciales
- `String.indexOf()` — validación de emails

### 7. Estructuras de control
- `if / else if / else` — lógica de estados, calificación de notas
- `for-each` — recorrido de colecciones
- `while` — lectura de `ResultSet` en JDBC
- `switch`-equivalente con múltiples `if` en `getCategoria()`

### 8. Persistencia con JDBC
```java
Connection conn = DBConnection.getInstance().getConexion();
PreparedStatement ps = conn.prepareStatement("INSERT INTO estudiante ...");
ps.setString(1, estudiante.getNombre());
ps.executeUpdate();
```
- Transacciones (`conn.setAutoCommit(false)`, `commit`, `rollback`)
- `PreparedStatement` para prevenir SQL injection
- Patrón DAO (Data Access Object) para separación de capas

### 9. Interfaz web con JSP/Tomcat
- Servlets como controladores (`@WebServlet`)
- JSP como vistas con JSTL
- Patrón MVC completo
- Bootstrap 5 para diseño responsive

---

## Estructura del proyecto

```
java-app/
├── pom.xml                         ← Configuración Maven
└── src/
    └── main/
        ├── java/edu/universidad/
        │   ├── model/
        │   │   ├── Persona.java     ← Clase abstracta (jerarquía base)
        │   │   ├── IEvaluable.java  ← Interfaz
        │   │   ├── Estudiante.java  ← Herencia + IEvaluable
        │   │   ├── Docente.java     ← Herencia
        │   │   ├── Curso.java       ← Entidad
        │   │   └── Matricula.java   ← Entidad relacional
        │   ├── dao/
        │   │   ├── EstudianteDAO.java
        │   │   ├── DocenteDAO.java
        │   │   ├── CursoDAO.java
        │   │   └── MatriculaDAO.java
        │   ├── servlet/
        │   │   ├── DashboardServlet.java
        │   │   ├── EstudianteServlet.java
        │   │   ├── DocenteServlet.java
        │   │   ├── CursoServlet.java
        │   │   └── MatriculaServlet.java
        │   └── util/
        │       ├── DBConnection.java     ← Singleton JDBC
        │       ├── DBInitializer.java    ← Creación de tablas + datos
        │       ├── Validador.java        ← Math + String
        │       ├── AppContextListener.java
        │       └── CharsetFilter.java
        └── webapp/
            ├── index.jsp
            └── WEB-INF/
                ├── web.xml
                └── views/
                    ├── dashboard.jsp
                    ├── estudiantes/   (lista.jsp, formulario.jsp)
                    ├── docentes/      (lista.jsp, formulario.jsp)
                    ├── cursos/        (lista.jsp, formulario.jsp)
                    └── matriculas/    (lista.jsp, formulario.jsp, nota.jsp)
```

---

## Instrucciones de ejecución

### Requisitos previos
- Java 11 o superior
- Apache Maven 3.6+

### Pasos

```bash
# 1. Clonar el repositorio
git clone https://github.com/TU_USUARIO/sistema-universitario.git
cd sistema-universitario/java-app

# 2. Compilar y ejecutar
mvn tomcat7:run

# 3. Abrir en el navegador
# http://localhost:8080/
```

> La base de datos H2 se crea automáticamente en `~/.universidad_db/`
> con tablas y datos de ejemplo al primer inicio.

### Ejecutar en un puerto diferente
```bash
mvn tomcat7:run -Dtomcat.port=9090
```

---

## Módulos del sistema

| Módulo | Ruta | Descripción |
|--------|------|-------------|
| Dashboard | `/` | Estadísticas generales |
| Estudiantes | `/estudiantes` | CRUD de estudiantes |
| Docentes | `/docentes` | CRUD de docentes |
| Cursos | `/cursos` | CRUD de cursos con cupo |
| Matrículas | `/matriculas` | Inscripciones y registro de notas |

---

## Video de exposición

🎥 [Ver video en YouTube](https://www.youtube.com/watch?v=ENLACE_AQUI)

> *Completar con el enlace real del video antes de la entrega.*

---

## Capturas del sistema

> *(Agregar capturas de pantalla del sistema funcionando antes de la entrega)*

![img_1.png](img_1.png)

![img.png](img.png)

![img_2.png](img_2.png)

![img_3.png](img_3.png)
---

## Autores

Desarrollado como Evaluación Final del curso **Programación Orientada a Objetos**.  
Institución: ISIL  
Docente: Wilder Julio Espinoza Bravo  
Ciclo: 202610
