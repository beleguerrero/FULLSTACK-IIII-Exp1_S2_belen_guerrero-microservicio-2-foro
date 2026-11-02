# Exp1_S2_belen_guerrero
Actividad Sumativa 1: Programando nuestro BackEnd


## Configuración
- **Puerto:** `8082`
- **URL:** `http://localhost:8082/api/mi-foro/`
- **Base de datos:** Oracle Cloud

La conexión a la base de datos debe estar configurada en `application.properties`.

### Oracle Wallet

El proyecto tiene configurada la conexión a Oracle mediante **Oracle Wallet**, ubicado en la siguiente ruta de Windows: `C:/Wallets/Wallet_MIBD_belen_guerrero`

## Endpoints

**Todos los endpoints requieren autenticación mediante **Bearer Token (JWT)** en el header `Authorization`.**

El token JWT debe obtenerse previamente desde el endpoint `/auth/login` del **microservicio de Usuarios**, enviando las credenciales correspondientes.


### Foro

| Método HTTP | Endpoint | Descripción | Body |
|---|---|---|---|
| GET | `/foros` | Obtener todos los foros activos. | No requiere |
| GET | `/foros/{id}` | Obtener un foro activo por su ID. | No requiere |
| POST | `/foros` | Crear un foro. | Sí, campos obligatorios |
| PUT | `/foros/{id}` | Actualizar un foro existente. | Sí, todos los campos son opcionales |
| DELETE | `/foros/{id}` | Eliminar un foro por su ID. | No requiere |

#### Body para crear un foro (POST)

Todos los campos son obligatorios.

```json
{
  "titulo": "Foro de prueba",
  "contenido": "El contenido de este foro está pendiente...",
  "estadoId": 1,
  "categoriaId": 1
}
```

#### Body para actualizar un foro (PUT)

Todos los campos son opcionales. Solo se actualizarán aquellos que sean enviados en el body. Los campos omitidos conservarán su valor actual.

```json
{
  "titulo": "Nuevo título del foro",
  "contenido": "Contenido actualizado"
}
```

También es posible actualizar un solo campo:

```json
{
  "estadoId": 1
}
```

O varios campos:

```json
{
  "titulo": "Nuevo título",
  "contenido": "Contenido actualizado",
  "estadoId": 1,
  "categoriaId": 2,
  "activo": 1
}
```


### Comentarios

| Método HTTP | Endpoint | Descripción | Body |
|---|---|---|---|
| GET | `/foros/{foroId}/comentarios` | Obtener todos los comentarios activos por ID del foro. | No requiere |
| POST | `/foros/{foroId}/comentarios` | Crear un comentario por ID del foro. | Sí, campos obligatorios |
| PUT | `/comentarios/{id}` | Actualizar un comentario existente. | Sí, todos los campos son opcionales |
| DELETE | `/comentarios/{id}` | Eliminar un comentario por su ID. | No requiere |

### Body para crear un comentario (POST)

Solo `contenido` es obligatorio.

```json
{
  "contenido": "comentario de prueba",
  "comentarioPadreId": null
}
```

### Body para actualizar un comentario (PUT)

Todos los campos son opcionales. Solo se actualizarán aquellos que sean enviados en el body. Los campos omitidos conservarán su valor actual.

```json
{
  "contenido": "comentario de prueba",
  "activo": true
}
```

También es posible actualizar un solo campo:

```json
{
  "contenido": "edit, de mi comentario..."
}
```





