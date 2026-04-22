# 🏓 WePadel

Backend de e-commerce de productos de pádel. Desarrollado con Spring Boot + MySQL, con autenticación JWT y autorización por roles.

**Materia:** Aplicaciones Interactivas — UADE  
**Grupo 5** 
**Repositorio:** https://github.com/juanipisa/wepadel

---

## Requisitos

- Java 17
- MySQL 8

## Configuración y ejecución

**1. Clonar el repositorio**
```bash
git clone https://github.com/juanipisa/wepadel.git
cd wepadel
```

**2. Crear la base de datos**
```sql
CREATE DATABASE wepadel;
```

**3. Configurar `src/main/resources/application.properties`**
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/wepadel
spring.datasource.username=root
spring.datasource.password=TU_PASSWORD

application.security.jwt.secretKey=TU_SECRET_KEY_BASE64
application.security.jwt.expiration=86400000
```
> Las variables de entorno recomendadas son `DB_PASSWORD`, `JWT_SECRET_KEY` y `JWT_EXPIRATION`.

**4. Levantar la aplicación**
```bash
# Mac/Linux
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
```

La API queda disponible en `http://localhost:8080`.  
Las tablas se crean automáticamente (ddl-auto=update).

---

## Flujo rápido de prueba

1. Registrar usuario → `POST /api/v1/auth/register`
2. Copiar el `token` de la respuesta
3. Usarlo en el header: `Authorization: Bearer <token>`
4. Operar endpoints protegidos normalmente

---

## Arquitectura

Controller → Service (interfaz + impl) → Repository → MySQL
Seguridad: Spring Security + JWT. Cada request pasa por `JwtAuthenticationFilter` antes de llegar al controller.

Roles: `CLIENTE` y `ADMINISTRADOR`.

---

## Colección Postman

Disponible en `postman/wepadel.json` dentro del repositorio.

---

## Requerimientos y lógica de negocio

1. Los usuarios invitados pueden ver el catálogo de productos (incluyendo imágenes y descuentos vigentes), los registrados además pueden agregar productos al carrito, ver su perfil, historial de compras y saldo de puntos.
2. El administrador tiene credenciales únicas provistas por el equipo técnico, acceso exclusivo para gestionar catálogo, stock y descuentos, y no puede realizar compras ni poseer carrito.
3. El registro de usuarios se realiza a través del endpoint /api/v1/auth/register, que devuelve un JWT válido para operar inmediatamente. El login posterior se hace en /api/v1/auth/authenticate.
4. Al registrar un usuario cliente, se crean automáticamente su carrito y su sistema de puntos.
5. El carrito del usuario registrado persiste por 7 días, si no se concreta la compra en ese plazo, se vacía automáticamente.
6. El subtotal del carrito se actualiza en tiempo real según el precio actual del producto, aplicando automáticamente el descuento vigente si existe.
7. Al agregar un producto que ya está en el carrito, se incrementa la cantidad en el ítem existente en lugar de crear uno nuevo.
8. Los puntos se suman al confirmar una orden. Si se usan como parte de pago se restan del saldo; al cancelar una orden se revierte el impacto de puntos. El CLIENTE no podrá usar puntos si su saldo es negativo.
9. La orden se genera únicamente al ejecutar POST /usuarios/{id}/ordenes, si el proceso se interrumpe antes, la orden no se crea y el carrito permanece intacto.
10. Un usuario invitado debe registrarse para poder confirmar una orden.
11. Una vez confirmada la orden, el carrito se vacía inmediatamente, incluso si la compra se cancela posteriormente.
12. Al confirmar una compra, se registra el precio unitario del momento (con descuento aplicado si corresponde) en ORDEN_ITEM para que el historial sea inalterable ante futuros cambios de precio.
13. Los clientes registrados pueden cancelar una orden dentro de las primeras 24 horas mediante PUT /usuarios/{id}/ordenes/{ordenId}/cancelar. Al cancelar, la orden pasa a estado CANCELADA, el stock se restaura y se revierte el impacto de puntos: se restan los puntos generados y se reembolsan los puntos usados. El saldo puede quedar negativo; si queda negativo, el usuario no podrá canjear puntos hasta volver a balance comprando.
14. Cada producto tiene un stock asociado que se descuenta al confirmar la orden y se actualiza manualmente por el administrador (PUT /stocks/producto/{productoId}).
15. Cada producto puede tener varias imágenes: se almacenan en base de datos como LONGBLOB, se suben por multipart/form-data vía POST /imagenes, y se exponen en las respuestas JSON en Base64 (archivoBase64) para consumo desde el frontend.
16. Un producto puede tener múltiples descuentos registrados, pero solo uno puede estar vigente a la vez. Un descuento es vigente si activo = true y la fecha actual se encuentra entre fechaInicio y fechaFin.
17. Las categorías de producto (PALETAS, ACCESORIOS, PELOTAS) son fijas, no pueden ser creadas ni modificadas.
18. Los productos pueden desactivarse mediante el flag estaHabilitado para ocultarlos del catálogo sin eliminarlos del sistema.
19. El sistema valida que el mail tenga formato válido y que la contraseña posea al menos 12 caracteres, incluyendo una mayúscula, un número y un símbolo especial.
20. Todos los endpoints protegidos devuelven 401 Unauthorized si no se provee token, y 403 Forbidden si el rol del usuario no tiene permisos suficientes.
21. Seguridad en perfiles: Un usuario con rol CLIENTE solo puede editar su propia información. Un ADMINISTRADOR puede editar perfiles de clientes (incluyendo mail y password), pero tiene prohibido editar la información de otros administradores.

---

## 🤝 Modo de trabajo

- **Avisar en el grupo** en qué tarea vas a trabajar antes de arrancar, para evitar conflictos.
- **Crear una branch por cada feature o fix**, con nombres descriptivos:
  ```
  feature/login-jwt
  feature/redux-carrito
  fix/descuento-stock
  ```
- **Nunca commitear directo en `main`.**
- Verificar que el proyecto compila y corre antes de hacer el PR.
- Una vez mergeada la branch, **eliminarla** para mantener el repo limpio.

---

## 🗺️ Roadmap y tareas del proyecto

### ✅ Entrega #1 — Modelo de datos y entidades `(vence 07/04)`
- [x] Entidades JPA creadas y mapeadas
- [x] Base de datos conectada (MySQL)
- [x] Endpoints básicos funcionando
- [x] DER (Diagrama Entidad-Relación)
- [ ] Diagrama UML de clases

### 🔐 Entrega #2 — Seguridad con JWT `(vence 22/04)`
- [x] Integrar Spring Security
- [x] Implementar autenticación con JWT
- [x] Endpoint de login (`POST /api/v1/auth/authenticate`)
- [x] Endpoint de registro que devuelva token (`POST /api/v1/auth/register`)
- [x] Hashear passwords con BCrypt
- [ ] Proteger endpoints según rol (CLIENTE / ADMINISTRADOR)
- [x] Implementar `UserDetails` en la entidad `Usuario`

### 🎨 Entrega #3 — Maquetación visual del frontend `(vence 20/05)`
- [ ] Definir pantallas: Home, Catálogo, Detalle de producto, Carrito, Login, Registro, Checkout
- [ ] Definir paleta de colores, tipografía y librería de estilos
- [ ] Maquetado estático en HTML/CSS

### ⚛️ Entrega #4 — Componentes y routing con React `(vence 04/06)`
- [ ] Inicializar proyecto con React + Vite
- [ ] Crear componentes principales
- [ ] Configurar React Router DOM
- [ ] Manejo de estados locales con `useState`

### 🔌 Entrega #5 — Integración frontend ↔ backend `(vence 18/06)`
- [ ] Conectar React al backend via fetch/axios
- [ ] Manejo de promesas y asincronismo (`useEffect`)
- [ ] Renderizado condicional según estado de sesión
- [ ] Flujo completo: login → carrito → checkout

### 🗃️ Entrega #6 — Redux `(vence 02/07)`
- [ ] Configurar Redux Toolkit
- [ ] Migrar estado del carrito y sesión a Redux
- [ ] Refactorizar componentes para usar el store global
- [ ] Integrar middleware si es necesario

---
