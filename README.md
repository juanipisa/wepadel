# 🏓 E-commerce de Pádel
E-commerce fullstack de productos de pádel. Backend en Spring Boot + MySQL, frontend en React con Redux en el marco de la materia Aplicaciones Interactivas, UADE.

## 🎯 Objetivo del proyecto
Construir una plataforma de venta online de productos de pádel donde los usuarios pueden navegar el catálogo, agregar productos al carrito y realizar compras.

## 🧠 Lógica de negocio
- Los usuarios invitados pueden ver y comprar productos; los registrados acceden a su perfil, historial de compras y beneficios de puntos.
- Si un invitado agrega productos al carrito y luego se registra, esos productos se transfieren automáticamente a su nueva cuenta.
- Si un usuario invitado no se registra, su entidad y carrito se eliminan permanentemente al cerrar la página.
- El administrador tiene credenciales únicas, proporcionadas por el equipo técnico, una vista exclusiva para gestionar stock y precios, y no puede realizar compras ni tener un carrito.
- Al registrar un usuario cliente, se crea automáticamente su carrito; para los invitados, se crea al agregar el primer producto.
- El carrito del usuario registrado persiste por 7 días; si no se concreta la compra en ese plazo, se vacía automáticamente.
- El subtotal del carrito se actualiza en tiempo real según el precio actual de los productos en la base de datos.
- Al agregar un producto que ya está en el carrito, se incrementa la cantidad en el ítem existente en lugar de crear uno nuevo.
- Exclusivo para clientes registrados; se suman puntos al confirmar una orden y se restan si se utilizan como parte de pago o si la orden se cancela.
- La orden se genera únicamente al hacer clic en "Pagar"; si el proceso se interrumpe antes, la orden no se crea y el carrito permanece intacto.
- Una vez confirmada la orden, el carrito se vacía inmediatamente, incluso si la compra se cancela posteriormente.
- Al confirmar una compra, se registra el precio unitario del momento en ORDEN_ITEM para que el historial sea inalterable ante futuros cambios de precio.
- Los clientes registrados pueden cancelar una compra y solicitar reembolso mediante formulario (email) solo dentro de las primeras 24 horas.
- Al confirmarse una cancelación, la orden pasa a estado CANCELADA, el stock de los productos se restaura y los puntos generados se eliminan.
- Cada producto tiene un stock asociado que se descuenta al confirmar la orden y se actualiza manualmente por el administrador.
- Las categorías (PALETAS, ACCESORIOS, PELOTAS) son fijas y no pueden ser modificadas ni siquiera por el administrador.
- Los productos pueden desactivarse mediante el flag estaHabilitado para ocultarlos de la venta sin borrarlos del sistema.

## 🏗️ Arquitectura

### Backend
Arquitectura en capas estándar de Spring Boot:

```
Controller → Service (interfaz + implementación) → Repository → MySQL
```

```
src/
└── main/
    └── java/com/uade/tpo/marketplace/
        ├── controllers/      # Endpoints REST (@RestController)
        ├── entity/           # Entidades JPA + DTOs de request
        │   └── dto/
        ├── service/          # Interfaces de negocio + implementaciones
        ├── repository/       # Interfaces JpaRepository
        └── exceptions/       # Excepciones con @ResponseStatus
```

### Frontend *(a construir)*
TBC
---

## 🗄️ Base de datos

- **Motor:** MySQL 8
- **ORM:** Spring Data JPA / Hibernate
- **DDL:** `spring.jpa.hibernate.ddl-auto=update`
- **Credenciales:** en `src/main/resources/application.properties`

> ⚠️ Las credenciales están commiteadas temporalmente. Una vez que todos las tengan configuradas localmente, sacarlas del repo y usar variables de entorno.

### Entidades
USUARIO
SISTEMA_PUNTOS
CARRITO
CARRITO_ITEM
ORDEN
ORDEN_ITEM
PRODUCTO
STOCK

* El diagrama entidad relación se encuentra documentado en nuestra carpeta de drive *

---

## 📡 Endpoints
# Recurso: Usuarios
- `GET /usuarios`
- `GET /usuarios/{usuarioId}`
- `POST /usuarios/invitado`
- `POST /usuarios/registrado`
- `PUT /usuarios/{usuarioId}` 

# Recurso: Carrito
- `GET /usuarios/{usuarioId}/carrito`
- `GET /usuarios/{usuarioId}/carrito/items` 
- `POST /usuarios/{usuarioId}/carrito/items` 
- `DELETE /usuarios/{usuarioId}/carrito/items/{productoId}` 
- `DELETE /usuarios/{usuarioId}/carrito` 

# Recurso: Ordenes
- `GET /usuarios/{usuarioId}/ordenes`
- `GET /usuarios/{usuarioId}/ordenes/{ordenId}` 
- `POST /usuarios/{usuarioId}/ordenes`
- `PUT /usuarios/{usuarioId}/ordenes/{ordenId}/cancelar` 

# Recurso: Productos
- `GET /productos` 
- `GET /productos/{productoId}` 
- `POST /productos` 
- `PUT /productos/{productoId}` 

# Recurso: Stocks
- `GET /stocks/producto/{productoId}`
- `POST /stocks` 
- `PUT /stocks/producto/{productoId}` 

# Recurso: Compras
- `GET /compras/{compraId}` 
- `GET /compras/usuario/{usuarioId}` 
- `POST /compras` 

# 🗺️ Roadmap y tareas del proyecto

El desarrollo sigue el cronograma de la materia. Cada entrega obligatoria es un hito concreto. Anotarse a las tareas que van a realizar. 

### ✅ Entrega #1 — Modelo de datos y entidades `(vence 07/04)`
- [x] Entidades JPA creadas y mapeadas 
- [x] Base de datos conectada (MySQL) 
- [x] Endpoints básicos funcionando 
- [x] DER (Diagrama Entidad-Relación) 
- [ ] Diagrama UML de clases

### 🔐 Entrega #2 — Seguridad con JWT `(vence 22/04)`
- [ ] Integrar Spring Security
- [ ] Implementar autenticación con JWT
- [ ] Endpoint de login (`POST /auth/login`)
- [ ] Endpoint de registro que devuelva token
- [ ] Hashear passwords con BCrypt
- [ ] Proteger endpoints según rol (USER / ADMIN)
- [ ] Agregar clase `Rol` y relacionarla con `Usuario`

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


## 🐛 Bugs y pendientes

- [ ] Actualizar documentación de endpoints de forma detallada en readme
- [ ] Crear UML
- [ ] Descontar stock al confirmar una compra
- [ ] Limpiar el carrito después de una compra exitosa
- [ ] Agregar `DELETE /categorias/{id}`
- [ ] Optimizar validación de duplicados (evitar `findAll()` en `Usuario` y `Categoria`)
- [ ] Mover credenciales de DB a variables de entorno

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
