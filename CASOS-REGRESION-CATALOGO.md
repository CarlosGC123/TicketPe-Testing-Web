# Casos Simples de Regresión - Catálogo Web

## Casos Identificados por Inspección del Catálogo

### TC-REG-01: Catálogo carga y muestra eventos
**Prioridad:** Alta | **Automatizable:** Sí

**Descripción:** Verificar que el catálogo carga correctamente y muestra al menos un evento

**Pasos:**
1. Abrir https://testathon.testingperu.com/catalogo
2. Esperar carga completa

**Resultado esperado:**
- La página carga sin errores
- Se muestran tarjetas de eventos
- Cada tarjeta tiene: nombre, imagen, fecha, precio

**Componentes reutilizables:**
- `CargarPaginaPrincipal` (interaction)
- `ElementoEsVisible` (questions)
- Crear: `CatalogoPage` (page)

---

### TC-REG-02: Click en evento navega a detalle
**Prioridad:** Alta | **Automatizable:** Sí

**Descripción:** Verificar que al hacer click en un evento se navega a su página de detalle

**Pasos:**
1. Abrir catálogo
2. Click en primer evento visible
3. Verificar URL y contenido

**Resultado esperado:**
- URL cambia a /eventos/{id}
- Se muestra detalle del evento
- Botón "Comprar" visible

**Componentes reutilizables:**
- `DarClick` (task)
- `AbrirFichaDeEvento` (task) - ya existe
- `ElementoEsVisible` (questions)

---

### TC-REG-03: Búsqueda de eventos funciona
**Prioridad:** Media | **Automatizable:** Sí (si existe campo búsqueda)

**Descripción:** Verificar que la búsqueda filtra eventos correctamente

**Pasos:**
1. Abrir catálogo
2. Escribir nombre de evento en búsqueda
3. Verificar resultados

**Resultado esperado:**
- Solo eventos que coinciden se muestran
- Búsqueda vacía muestra todos

**Componentes reutilizables:**
- `EscribeTexto` (task)
- Crear: `BuscarEvento` (task)

---

### TC-REG-04: Eventos agotados se marcan visualmente
**Prioridad:** Media | **Automatizable:** Sí

**Descripción:** Verificar que eventos sin cupo muestran etiqueta "Agotado"

**Pasos:**
1. Abrir catálogo
2. Identificar evento agotado (disponible=0)
3. Verificar etiqueta visual

**Resultado esperado:**
- Etiqueta "Agotado" visible
- Botón comprar deshabilitado o ausente

**Componentes reutilizables:**
- `EventoEstaAgotadoEnPantalla` (questions) - ya existe
- `ClienteApiCore.disponibilidad()` (util)

---

### TC-REG-05: Precios en catálogo coinciden con API
**Prioridad:** Alta | **Automatizable:** Sí

**Descripción:** Verificar consistencia Front/Back de precios en catálogo

**Pasos:**
1. Obtener eventos de API
2. Abrir catálogo
3. Comparar precios mostrados vs API

**Resultado esperado:**
- Precios en catálogo = precios en API
- Sin discrepancias

**Componentes reutilizables:**
- `ClienteApiCore.get("/eventos")` (util)
- `PrecioDeTipoEntradaCoincideConApi` (questions) - adaptar

---

## Resumen de Implementación

**Total casos:** 5
**Automatizables:** 5
**Componentes nuevos necesarios:** 2-3
**Componentes reutilizables:** 8+

**Prioridad de implementación:**
1. TC-REG-01 (base para otros)
2. TC-REG-02 (navegación crítica)
3. TC-REG-05 (consistencia Front/Back)
4. TC-REG-04 (estado agotado)
5. TC-REG-03 (búsqueda, si existe)
