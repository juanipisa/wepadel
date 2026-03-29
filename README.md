# 🏓 Marketplace de Pádel
E-commerce fullstack de productos de pádel. Backend en Spring Boot + MySQL, frontend en React con Redux en el marco de la materia Aplicaciones Interactivas, UADE.

## 🎯 Objetivo del proyecto
Construir una plataforma de venta online de productos de pádel donde los usuarios pueden navegar el catálogo sin registrarse, pero necesitan una cuenta para agregar productos al carrito y realizar compras. El sistema mantiene el carrito persistente entre sesiones y lo resetea automáticamente luego de 7 días.

## 🧠 Lógica de negocio
- Usuarios invitados pueden ver productos y categorías sin autenticarse.
- Usuarios registrados pueden gestionar su carrito y realizar compras.
- Al registrar un usuario se debe crear automáticamente su carrito asociado.
- El carrito persiste entre sesiones. Si tiene más de 7 días desde su creación, se vacía automáticamente al consultarlo.
- Al agregar un producto ya existente al carrito, se suma la cantidad en lugar de duplicar el ítem.
- Al confirmar una compra, durante el proceso de compra, se registra un snapshot del precio unitario de cada producto en ese momento, de modo que cambios futuros de precio no afecten el historial.
- Los productos y categorías tienen un flag "habilitado" para activarlos sin eliminarlos.
- El estado de una compra sigue el ciclo: `PENDIENTE → CONFIRMADA → ENVIADA → ENTREGADA` (o `CANCELADA`).

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
SPA (Single Page Application) con React + Vite. Estado global manejado con Redux Toolkit. Comunicación con el backend vía fetch/axios con manejo de promesas y asincronismo.

```
frontend/
├── src/
│   ├── components/     # Componentes reutilizables
│   ├── pages/          # Vistas principales (Home, Producto, Carrito, etc.)
│   ├── store/          # Redux: slices, reducers, middleware
│   ├── services/       # Llamadas al backend (fetch/axios)
│   └── router/         # React Router DOM
```

---

## 🗄️ Base de datos

- **Motor:** MySQL 8
- **ORM:** Spring Data JPA / Hibernate
- **DDL:** `spring.jpa.hibernate.ddl-auto=update`
- **Credenciales:** en `src/main/resources/application.properties`

> ⚠️ Las credenciales están commiteadas temporalmente. Una vez que todos las tengan configuradas localmente, sacarlas del repo y usar variables de entorno.

### Entidades

| Entidad | Descripción |
|---|---|
| `Usuario` | Nombre, mail (único), password, rol |
| `Rol` | Rol del usuario (ej: USER, ADMIN) *(a definir)* |
| `Categoria` | Descripción (única), flag habilitada |
| `Producto` | Nombre, descripción, precio, categoría, flag habilitado |
| `Stock` | Producto, cantidad, fecha de última actualización |
| `Carrito` | Usuario, fecha de creación |
| `CarritoItem` | Carrito, producto, cantidad |
| `Compra` | Usuario, dirección, CP, montos, estado, fecha |
| `CompraItem` | Compra, producto, cantidad, precioUnitario (snapshot) |
| `DatosFacturacion` | Datos de tarjeta asociados a una compra |

**EstadoCompra:** `PENDIENTE` · `CONFIRMADA` · `ENVIADA` · `ENTREGADA` · `CANCELADA`

---

## 📡 Endpoints

> Base URL: `http://localhost:8080`

### Usuarios — `/usuarios`
| Método | Path | Descripción |
|---|---|---|
| GET | `/usuarios` | Lista todos los usuarios |
| GET | `/usuarios/{id}` | Obtiene un usuario por ID |
| POST | `/usuarios` | Crea un usuario nuevo |
| PUT | `/usuarios/{id}` | Actualiza datos de un usuario |

**Body POST/PUT:**
```json
{ "nombreApellido": "Juan Pérez", "mail": "juan@mail.com", "password": "1234" }
```

---

### Categorías — `/categorias`
| Método | Path | Descripción |
|---|---|---|
| GET | `/categorias` | Lista todas las categorías |
| GET | `/categorias/{id}` | Obtiene una categoría por ID |
| POST | `/categorias` | Crea una categoría nueva |
| PUT | `/categorias/{id}` | Actualiza descripción y/o estado |

**Body POST:** `{ "descripcion": "Paletas" }`  
**Body PUT:** `{ "descripcion": "Paletas profesionales", "habilitada": true }`

> Si ya existe una categoría con la misma descripción → `400 Bad Request`.

---

### Productos — `/productos`
| Método | Path | Descripción |
|---|---|---|
| GET | `/productos` | Lista todos los productos |
| GET | `/productos/{id}` | Obtiene un producto por ID |
| POST | `/productos` | Crea un producto nuevo |
| PUT | `/productos/{id}` | Actualiza datos de un producto |

**Body POST/PUT:**
```json
{
  "nombre": "Paleta Pro X",
  "descripcion": "Paleta de fibra de carbono",
  "precio": 45000.00,
  "categoriaId": 1,
  "habilitado": true
}
```

---

### Stock — `/stocks`
| Método | Path | Descripción |
|---|---|---|
| GET | `/stocks/producto/{productoId}` | Consulta stock de un producto |
| POST | `/stocks` | Crea el registro de stock |
| PUT | `/stocks/producto/{productoId}` | Actualiza cantidad en stock |

**Body POST/PUT:** `{ "productoId": 1, "cantidad": 100 }`

---

### Carrito — `/carritos`
| Método | Path | Descripción |
|---|---|---|
| GET | `/carritos/{id}` | Obtiene el carrito (resetea si expiró) |
| GET | `/carritos/{id}/items` | Lista los ítems del carrito |
| POST | `/carritos/{id}/items` | Agrega un producto al carrito |
| DELETE | `/carritos/{id}/items/{productoId}` | Elimina un producto del carrito |

**Body POST items:** `{ "productoId": 3, "cantidad": 2 }`

---

### Compras — `/compras`
| Método | Path | Descripción |
|---|---|---|
| GET | `/compras/{id}` | Obtiene una compra por ID |
| GET | `/compras/usuario/{usuarioId}` | Lista compras de un usuario |
| POST | `/compras` | Confirma una compra desde el carrito |

**Body POST:**
```json
{
  "usuarioId": 1, "carritoId": 1,
  "direccion": "Av. Corrientes 1234", "cp": "1043",
  "montoEnvio": 2500.00,
  "nroTarjeta": "4111111111111111", "vencimiento": "12/27",
  "dni": "35123456", "cvv": "123",
  "nombreTitular": "Juan Pérez", "cuotas": 3
}
```

---

## ⚙️ Stack

| Tecnología | Rol |
|---|---|
| Java 17+ | Lenguaje backend |
| Spring Boot | Framework base |
| Spring Data JPA + Hibernate | ORM y acceso a datos |
| Spring Security + JWT | Autenticación y autorización *(a implementar)* |
| MySQL 8 | Base de datos relacional |
| Lombok | Reduce boilerplate (getters/setters) |
| Maven | Build y dependencias |
| React + Vite | Framework frontend *(a construir)* |
| React Router DOM | Routing del frontend *(a construir)* |
| Redux Toolkit | Estado global del frontend *(a construir)* |

---

## 🗺️ Roadmap del proyecto

El desarrollo sigue el cronograma de la materia. Cada entrega obligatoria es un hito concreto.

### ✅ Entrega #1 — Modelo de datos y entidades `(vence 07/04)`
- [x] Entidades JPA creadas y mapeadas
- [x] Base de datos conectada (MySQL)
- [x] Endpoints básicos funcionando
- [ ] DER (Diagrama Entidad-Relación)
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

---

## 🐛 Bugs y pendientes del backend

- [ ] Crear entidades pendientes en base al nuevo DER y actualizar las existentes (Palo)
- [ ] Crear carrito automáticamente al registrar un usuario
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
