# Manual de Usuario — App Android Loop

**Versión:** 1.0
**Plataforma:** Android
**Dirigido a:** Usuario final
**Fecha:** Marzo 2026

--- 

## Índice

- [Manual de Usuario — App Android Loop](#manual-de-usuario--app-android-loop)
  - [Índice](#índice)
  - [1. Introducción](#1-introducción)
  - [2. Instalación y primera apertura](#2-instalación-y-primera-apertura)
    - [2.1 Requisitos previos](#21-requisitos-previos)
    - [2.2 Instalación del APK](#22-instalación-del-apk)
    - [2.3 Permisos solicitados](#23-permisos-solicitados)
    - [2.4 Conexión al servidor](#24-conexión-al-servidor)
  - [3. Inicio de sesión](#3-inicio-de-sesión)
    - [3.1 Pantalla de login](#31-pantalla-de-login)
    - [3.2 Mantener la sesión iniciada](#32-mantener-la-sesión-iniciada)
    - [3.3 Error de login](#33-error-de-login)
  - [4. Registro de nueva cuenta](#4-registro-de-nueva-cuenta)
  - [5. Pantallas y acciones](#5-pantallas-y-acciones)
    - [5.1 Perfil de usuario](#51-perfil-de-usuario)
    - [5.2 Listado de productos y búsqueda](#52-listado-de-productos-y-búsqueda)
    - [5.3 Detalle de producto](#53-detalle-de-producto)
    - [5.4 Crear producto (alta)](#54-crear-producto-alta)
    - [5.5 Carrito de compra](#55-carrito-de-compra)
    - [5.7 Ajustes](#57-ajustes)
  - [6. Casos típicos paso a paso](#6-casos-típicos-paso-a-paso)
    - [6.1 Crear un producto con foto](#61-crear-un-producto-con-foto)
    - [6.2 Editar etiquetas de un producto](#62-editar-etiquetas-de-un-producto)
  - [7. Cerrar sesión](#7-cerrar-sesión)
  - [8. Problemas frecuentes](#8-problemas-frecuentes)
    - [Sin conexión a Internet](#sin-conexión-a-internet)
    - [Token caducado / Sesión expirada](#token-caducado--sesión-expirada)
    - [Error al subir imagen](#error-al-subir-imagen)
    - [La app se cierra inesperadamente](#la-app-se-cierra-inesperadamente)
    - [No aparecen productos en el listado](#no-aparecen-productos-en-el-listado)

---

## 1. Introducción

**Loop** es una aplicación de marketplace de segunda mano que te permite comprar y vender artículos desde tu móvil Android. Puedes publicar tus productos con fotos, filtrar el catálogo, añadir artículos al carrito y gestionar tu perfil de vendedor.

---

## 2. Instalación y primera apertura

### 2.1 Requisitos previos

| Requisito | Detalle |
|-----------|---------|
| Sistema operativo | Android 7.0 (API 24) o superior |
| Conexión | Wi-Fi o datos móviles activos |
| Espacio | ~30 MB libres |
| Permisos necesarios | Acceso a Internet, acceso a almacenamiento (para imágenes) |

### 2.2 Instalación del APK

> **Nota:** Loop se distribuye actualmente mediante archivo `.apk` (instalación manual). No está disponible en Google Play Store.

**Pasos:**

1. Descarga el archivo `Loop.apk` en tu dispositivo Android.
2. Abre el explorador de archivos y localiza el APK descargado.
3. Pulsa sobre el archivo `Loop.apk`.
4. Si aparece el aviso *"Instalar aplicaciones de fuentes desconocidas"*, ve a **Ajustes → Seguridad → Fuentes desconocidas** y activa la opción para tu navegador o gestor de archivos.
5. Pulsa **Instalar** en el diálogo que aparece.
6. Una vez completada la instalación, pulsa **Abrir**.

<p align="center">
  <img src="capturas/instalacion.png" width="500"><br>
  <em>Figura 2.1 — Diálogo de instalación del APK</em>
</p>

### 2.3 Permisos solicitados

Al abrir Loop por primera vez o al añadir imágenes, la app puede solicitar:

- **Acceso a Internet** — necesario para conectar con el servidor.
- **Acceso a archivos / galería** — necesario al subir fotos de producto. Pulsa **Permitir** cuando aparezca el diálogo.

### 2.4 Conexión al servidor

La app se conecta al servidor Odoo de Loop. Asegúrate de estar en la misma red Wi-Fi que el servidor durante las pruebas o de que el servidor sea accesible desde tu red móvil.

> Si usas el **emulador de Android Studio**, el servidor debe estar disponible en `http://10.0.2.2:8069`.
> Si usas un **dispositivo físico**, el servidor debe estar en la IP local de tu ordenador (p. ej. `http://192.168.X.X:8069`).

---

## 3. Inicio de sesión

### 3.1 Pantalla de login

Al abrir Loop aparece directamente la pantalla de inicio de sesión.

<p align="center">
  <img src="capturas/inicio.jpeg" width="250"><br>
  <em>Figura 3.1 — Pantalla de inicio de sesión</em>
</p>

**Pasos para iniciar sesión:**

1. Escribe tu **nombre de usuario** en el primer campo.
2. Escribe tu **contraseña** en el segundo campo (los caracteres se ocultan automáticamente).
3. Pulsa **INICIAR SESIÓN**.
4. Si los datos son correctos, serás redirigido a tu **perfil de usuario**.

> La contraseña se cifra con SHA-256 antes de enviarse al servidor. Nunca se transmite en texto plano.

### 3.2 Mantener la sesión iniciada

Loop guarda automáticamente tu sesión (token de autenticación) en el dispositivo. La próxima vez que abras la app, podrás continuar sin necesidad de volver a introducir tus credenciales, siempre que el token no haya caducado.

### 3.3 Error de login

Si las credenciales son incorrectas, aparecerá un mensaje de error en pantalla. Verifica que:
- El nombre de usuario sea correcto (sin espacios extra).
- La contraseña sea la registrada inicialmente.
- Tu dispositivo tenga conexión a Internet.

---

## 4. Registro de nueva cuenta

Si aún no tienes cuenta en Loop:

1. En la pantalla de login, pulsa **"Crear nueva cuenta"**.
2. Rellena el formulario de registro:

<p align="center">
  <img src="capturas/registro.jpeg" width="250"><br>
  <em>Figura 4.1 — Pantalla de registro</em>
</p>

| Campo | Descripción |
|-------|-------------|
| Nombre completo | Tu nombre real |
| Nombre de usuario | Identificador único (sin espacios) |
| Correo electrónico | Email válido |
| Contraseña | Mínimo segura; se almacenará cifrada |

1. Pulsa **REGISTRARSE**.
2. Si el registro es exitoso, verás el mensaje *"Registro realizado correctamente"* y serás redirigido a la pantalla de login.
3. Inicia sesión con las credenciales que acabas de crear.

---

## 5. Pantallas y acciones

### 5.1 Perfil de usuario

Una vez iniciada sesión, accedes a tu **Perfil de usuario**.

<p align="center">
  <img src="capturas/ingreso.jpeg" width="250"><br>
  <em>Figura 5.1 — Pantalla de perfil de usuario</em>
</p>


**Elementos de la pantalla:**

| Elemento | Acción |
|----------|--------|
| Icono ⚙️ (esquina superior derecha) | Navega a **Ajustes** |
| Foto de perfil + ✏️ | Permite cambiar la foto de avatar |
| Pestaña **"En venta"** | Muestra tus productos publicados |
| Pestaña **"Reseñas"** | Muestra las valoraciones recibidas |
| Icono 🏠 (barra inferior) | Vuelve al Perfil |
| Icono 🛍️ (barra inferior) | Navega a la **Tienda** (listado de productos) |
| Icono ➕ (barra inferior) | Navega a **Crear producto** |

---

### 5.2 Listado de productos y búsqueda

Accede pulsando el icono 🛍️ de la barra de navegación inferior.

<p align="center">
  <img src="capturas/productos.png" width="250"><br>
</p>

*Figura 5.2 — Pantalla de listado de productos*

**Buscar / Filtrar:**

1. Pulsa en la barra de búsqueda 🔍 en la parte superior.
2. Escribe el nombre del producto o la categoría.
3. El listado se filtra automáticamente en tiempo real (sin necesidad de pulsar "Buscar").
4. Borra el texto para volver a ver todos los productos.

**Añadir al carrito:**

- Pulsa el botón **[ + ]** en la tarjeta del producto para añadirlo al carrito.
- El contador del icono 🛒 (esquina superior derecha) se actualiza.

**Ver detalle:**

- Pulsa sobre cualquier tarjeta de producto para ver su información completa.

---

### 5.3 Detalle de producto

Al pulsar sobre un producto accedes a su pantalla de detalle.

<p align="center">
  <img src="capturas/producto_detalle.png" width="250"><br>
</p>

*Figura 5.3 — Pantalla de detalle de producto*

**Carrusel de imágenes:**
- Desliza horizontalmente para ver todas las fotos del producto.
- Los puntos indicadores muestran en qué imagen estás y cuántas hay en total.
- La imagen principal (marcada por el vendedor) aparece siempre en primer lugar.

**Información mostrada:**

| Campo | Descripción |
|-------|-------------|
| Precio | En euros, con 2 decimales |
| Estado | Nuevo / Segunda mano / Reacondicionado |
| Ubicación | Ciudad o zona del vendedor |
| Categoría | Categoría del artículo |
| Vendedor | Nombre del usuario que vende |
| Antigüedad | Fecha de adquisición del artículo |
| Etiquetas | Palabras clave del producto |

**Añadir al carrito:** Pulsa el botón **"AÑADIR AL CARRITO"** al final de la pantalla para añadir el artículo y ser redirigido al carrito.

---

### 5.4 Crear producto (alta)

Accede pulsando el icono **➕** de la barra de navegación inferior.

<p align="center">
  <img src="capturas/crar_producto2.png" width="250"><br>
</p>


<p align="center">
  <img src="capturas/crear_Producto1.png" width="250"><br>
</p>


*Figura 5.4 — Pantalla de creación de producto*

**Campos del formulario:**

| Campo | Obligatorio | Descripción |
|-------|-------------|-------------|
| Fotos | Sí (mín. 1) | Imágenes del artículo desde la galería |
| Nombre | Sí | Nombre del producto |
| Descripción | No | Descripción detallada |
| Precio | Sí | Precio en euros |
| Ubicación | No | Ciudad o zona |
| Estado | Sí | Nuevo / Segunda mano / Reacondicionado |
| Etiquetas | No | Múltiples etiquetas mediante casillas |
| Categoría | Sí | Electrónica / Ropa / Hogar |
| Antigüedad | Sí | Fecha mediante selector de calendario |

**Añadir fotos:**
1. Pulsa el botón **[ + ]** en la sección de fotos.
2. Se abrirá el selector de archivos del sistema.
3. Selecciona una o varias imágenes de tu galería.
4. Las fotos seleccionadas aparecen en la fila horizontal. **La primera imagen será la principal.**
5. Para eliminar una foto, pulsa la **X** que aparece sobre ella.

**Seleccionar etiquetas:**
- Marca las casillas de las etiquetas que mejor describan tu producto.
- Puedes seleccionar varias a la vez, como maximo 5.

**Guardar el producto:**
1. Rellena todos los campos obligatorios.
2. Selecciona la antigüedad en el selector de fecha.
3. Pulsa **GUARDAR PRODUCTO**.
4. Si todo es correcto, verás el mensaje *"Producto creado correctamente"* y volverás al listado.

<p align="center">
  <img src="capturas/productoGuardado.png" width="250"><br>
</p>

*Figura 5.5 — Pantalla Producto Guardado correctamente*

### 5.5 Carrito de compra

Accede pulsando el icono 🛒 en la pantalla de Tienda.

<p align="center">
  <img src="capturas/carritoCompras.png" width="250"><br>
</p>

*Figura 5.5 — Carrito de Compra*

**Acciones disponibles:**

| Acción | Descripción |
|--------|-------------|
| 🗑 (icono papelera) | Elimina el artículo del carrito |
| **CONTINUAR AL PAGO** | Inicia el proceso de pago (próximamente) |
| **Seguir comprando** | Vuelve al listado de productos |

> El total se calcula automáticamente sumando el precio de todos los artículos del carrito.
> No se pueden añadir artículos duplicados al carrito.


### 5.7 Ajustes

Accede pulsando el icono ⚙️ en la pantalla de Perfil.


La pantalla de ajustes está disponible para futuras configuraciones de la aplicación.

---

## 6. Casos típicos paso a paso

### 6.1 Crear un producto con foto

**Objetivo:** Publicar un artículo en Loop con imágenes.

1. En la barra de navegación inferior, pulsa **➕** para ir a *Crear producto*.
2. Pulsa el botón **[ + ]** en la sección *Fotos*.
3. Se abre el selector de archivos. Selecciona **una o varias fotos** de tu galería.
4. Comprueba que las imágenes aparecen en la fila horizontal. Puedes eliminar alguna pulsando **X** sobre ella.
5. Rellena el campo **Nombre** (p. ej. *"Zapatillas Nike talla 42"*).
6. Añade una **Descripción** opcional.
7. Introduce el **Precio** (p. ej. *25*).
8. Escribe la **Ubicación** (p. ej. *Valencia*).
9. Selecciona el **Estado**: *Segunda mano*.
10. Marca las **Etiquetas** relevantes.
11. Selecciona la **Categoría**: *Ropa*.
12. Pulsa sobre el campo **Antigüedad** y elige la fecha en el calendario.
13. Pulsa **GUARDAR PRODUCTO**.
14. Aparece el mensaje *"Producto creado correctamente"* y vuelves al listado.

> La primera foto que seleccionas se convierte automáticamente en la **imagen principal** del producto.

---

### 6.2 Editar etiquetas de un producto

> **Nota:** Actualmente la edición de productos existentes se realiza desde la pantalla de creación (nueva publicación). La edición directa de un producto ya publicado estará disponible en próximas versiones.

**Para cambiar las etiquetas al crear un producto:**

1. Ve a *Crear producto* (icono ➕).
2. En la sección **Etiquetas**, marca o desmarca las casillas según las palabras clave que mejor describan tu artículo.
3. Puedes seleccionar varias etiquetas simultáneamente.
4. Completa el resto del formulario y pulsa **GUARDAR PRODUCTO**.


## 7. Cerrar sesión

Actualmente la sesión se mantiene activa mientras el token del servidor no expire. Para cerrar sesión manualmente:

1. Ve a la pantalla de **Ajustes** (icono ⚙️ en el Perfil).
2. La opción de *Cerrar sesión* estará disponible en próximas versiones.

**Alternativa manual:**
- Ve a **Ajustes del sistema Android → Aplicaciones → Loop → Borrar datos**.
- Esto elimina el token guardado y la próxima apertura pedirá login.

> El token de sesión caduca automáticamente en el servidor. Cuando esto ocurre, la app te pedirá que inicies sesión de nuevo.

---

## 8. Problemas frecuentes

### Sin conexión a Internet

**Síntoma:** La pantalla no carga productos o muestra un error al iniciar sesión.

**Solución:**
1. Comprueba que tienes Wi-Fi o datos móviles activos.
2. Verifica que puedes acceder a otras páginas web desde el mismo dispositivo.
3. Asegúrate de que la app tiene permiso de acceso a Internet (Ajustes Android → Aplicaciones → Loop → Permisos).
4. Si usas un dispositivo físico en red local, comprueba que el servidor Odoo está encendido y accesible.
5. Cierra la app completamente y vuelve a abrirla.

---

### Token caducado / Sesión expirada

**Síntoma:** Aparece un error de autenticación al intentar realizar cualquier acción (cargar productos, crear producto, etc.) aunque hayas iniciado sesión anteriormente.

**Solución:**
1. Sal de la app y vuelve a abrirla.
2. Si aparece la pantalla de login, introduce tus credenciales de nuevo.
3. Si el problema persiste, ve a **Ajustes Android → Aplicaciones → Loop → Borrar datos** y vuelve a iniciar sesión.

> Los tokens tienen un tiempo de vida limitado. Es normal tener que volver a hacer login de vez en cuando.

---

### Error al subir imagen

**Síntoma:** Al crear un producto, las fotos no se suben correctamente o aparece un mensaje de error.

**Posibles causas y soluciones:**

| Causa | Solución |
|-------|----------|
| La app no tiene permiso de acceso a archivos | Ve a **Ajustes Android → Aplicaciones → Loop → Permisos → Almacenamiento** y actívalo |
| El archivo de imagen es demasiado grande | Reduce el tamaño de la imagen antes de seleccionarla (usa cualquier app de galería para redimensionarla) |
| Formato de imagen no compatible | Usa imágenes en formato **JPG** o **PNG** |
| Sin conexión durante la subida | Comprueba tu conexión y vuelve a intentarlo |
| Servidor no disponible | Espera unos minutos y vuelve a intentarlo |

**Pasos de diagnóstico:**
1. Verifica que la imagen es visible en tu galería antes de seleccionarla.
2. Intenta con una foto diferente (de menor tamaño).
3. Comprueba tu conexión a Internet.
4. Cierra la app y vuelve a intentar crear el producto.

---

### La app se cierra inesperadamente

**Solución:**
1. Cierra la app completamente (eliminándola del gestor de tareas).
2. Reinicia la app.
3. Si el problema persiste, ve a **Ajustes Android → Aplicaciones → Loop → Forzar detención** y luego **Borrar caché**.

---

### No aparecen productos en el listado

**Síntoma:** El listado de la tienda está vacío o muestra *"No se encontraron productos"*.

**Causas y soluciones:**
- Si hay texto en la barra de búsqueda: bórralo para ver todos los productos.
- Si el listado está vacío sin filtro activo: puede que el servidor no tenga productos cargados o no haya conexión. Cierra la app y vuelve a abrirla.

---

*Manual elaborado para el proyecto Loop — DAM 2025-26.*
*Autoras: Andrea · Fabiana · Nayara*
