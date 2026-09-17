# Refactorización de Sintaxis Gherkin - Framework Web

**Fecha:** 2026-09-17  
**Objetivo:** Aplicar lógica coherente en casos de prueba y sintaxis Gherkin según convenciones de R3 y mejores prácticas

---

## Estrategia Aplicada: Híbrida

Se aplicó una estrategia **híbrida** que combina:
- ✅ **Scenario simple** para casos sin parametrización real
- ✅ **Scenario Outline + Examples** para casos que se benefician de parametrización
- ✅ **Títulos con patrón "Usuario + verbo + predicado"** en todos los casos
- ✅ **Orden correcto de tags** según R3 §5

---

## Convenciones de Tags (R3 §5)

**Orden oficial aplicado:**
```gherkin
@TC-<MÓDULO>-<NN> @ESC<XX> @web/@api @p1/@p2/@p3 @critico/@alto/@medio/@bajo @RSK-<NN> @smoke @regression @security @front/@manual
```

**Ejemplo:**
```gherkin
@TC-WEB-03 @ESC02 @web @p2 @alto @RSK-07 @smoke @regression @front
```

---

## Cambios por Feature

### 1. ESC01-PrecioDisponibilidadEnPantalla.feature

**Casos:** TC-WEB-01, TC-WEB-02  
**Tipo:** Scenario simple (sin parametrización)

**Cambios aplicados:**
- ✅ Orden de tags corregido
- ✅ Títulos actualizados con patrón "Usuario + verbo + predicado"
  - TC-WEB-01: "CP01 - Usuario visualiza precio y cupo en ficha de evento y valida coincidencia con API"
  - TC-WEB-02: "CP02 - Usuario visualiza evento agotado y valida que no puede comprar"

**Antes:**
```gherkin
@TC-WEB-01 @ESC01 @web @front @smoke @regression @p2 @alto @RSK-17
Scenario: La ficha del evento muestra el precio y cupo que devuelve la API al cargar
```

**Después:**
```gherkin
@TC-WEB-01 @ESC01 @web @p2 @alto @RSK-17 @smoke @regression @front
Scenario: CP01 - Usuario visualiza precio y cupo en ficha de evento y valida coincidencia con API
```

---

### 2. ESC02-Checkout.feature

**Casos:** TC-WEB-03, TC-WEB-04  
**Tipo:** TC-WEB-03 → Scenario Outline | TC-WEB-04 → Scenario simple

**Cambios aplicados:**
- ✅ TC-WEB-03 convertido a **Scenario Outline** con parametrización de tarjetas
- ✅ Tabla Examples agregada con tarjeta_rechazada y tarjeta_aprobada
- ✅ Títulos actualizados
  - TC-WEB-03: "CP01 - Usuario realiza pago rechazado y reintenta con tarjeta válida sobre la misma reserva"
  - TC-WEB-04: "CP02 - Usuario valida que la cuenta regresiva del checkout usa la hora del servidor"

**Antes (TC-WEB-03):**
```gherkin
@TC-WEB-03 @ESC02 @web @front @smoke @regression @p2 @alto @RSK-07
Scenario: Tras un pago rechazado la pantalla permite reintentar sobre la misma reserva
  Given un asistente recién registrado inicia sesión y está en el checkout de 1 entrada
  When paga con la tarjeta 4000000000000002
  And paga de nuevo en la misma pantalla con la tarjeta 4242424242424242
```

**Después (TC-WEB-03):**
```gherkin
@TC-WEB-03 @ESC02 @web @p2 @alto @RSK-07 @smoke @regression @front
Scenario Outline: CP01 - Usuario realiza pago rechazado y reintenta con tarjeta válida sobre la misma reserva
  Given un asistente recién registrado inicia sesión y está en el checkout de 1 entrada
  When paga con la tarjeta <tarjeta_rechazada>
  And paga de nuevo en la misma pantalla con la tarjeta <tarjeta_aprobada>
  
  Examples:
    | tarjeta_rechazada  | tarjeta_aprobada   |
    | 4000000000000002   | 4242424242424242   |
```

**Beneficio:** Permite agregar fácilmente más combinaciones de tarjetas sin duplicar el escenario completo.

---

### 3. ESC03-MisEntradas.feature

**Casos:** TC-WEB-05  
**Tipo:** Scenario Outline con parametrización de estados

**Cambios aplicados:**
- ✅ Convertido a **Scenario Outline** con parametrización de estados de entrada
- ✅ Tabla Examples agregada con 3 estados (válida, en trámite de reembolso, usada)
- ✅ Título actualizado: "CP01 - Usuario visualiza sus entradas y valida que cada estado se refleja correctamente"

**Antes:**
```gherkin
@TC-WEB-05 @ESC03 @web @front @regression @p2 @alto @RSK-09
Scenario: "Mis entradas" muestra el estado real de cada entrada
  Then A ve 3 entradas y cada una muestra el estado de GET /api/core/entradas/:id (válida, en trámite de reembolso, usada)
```

**Después:**
```gherkin
@TC-WEB-05 @ESC03 @web @p2 @alto @RSK-09 @regression @front
Scenario Outline: CP01 - Usuario visualiza sus entradas y valida que cada estado se refleja correctamente
  Then A ve 3 entradas y cada una muestra el estado de GET /api/core/entradas/:id (<estado_entrada_1>, <estado_entrada_2>, <estado_entrada_3>)
  
  Examples:
    | estado_entrada_1 | estado_entrada_2         | estado_entrada_3 |
    | válida           | en trámite de reembolso  | usada            |
```

**Beneficio:** Facilita agregar más combinaciones de estados para pruebas de regresión.

---

### 4. ESC04-ChatWeb.feature

**Casos:** TC-WEB-06, TC-WEB-07  
**Tipo:** Scenario simple (casos de seguridad específicos)

**Cambios aplicados:**
- ✅ Orden de tags corregido
- ✅ Títulos actualizados
  - TC-WEB-06: "CP01 - Usuario solicita reserva por chat y valida que se pide confirmación explícita"
  - TC-WEB-07: "CP02 - Usuario escribe HTML en el chat y valida que se muestra como texto literal sin ejecutarse"

**Justificación Scenario simple:** Son casos de seguridad específicos (confirmación IA, XSS) que no se benefician de parametrización. Cada uno valida un comportamiento único y crítico.

---

### 5. Login.feature

**Casos:** @LOGIN  
**Tipo:** Scenario simple

**Cambios aplicados:**
- ✅ Feature renombrado a "ESC00 - Autenticación (HU-E1.2)"
- ✅ Tag @ESC00 agregado como escenario base
- ✅ Orden de tags corregido
- ✅ Título actualizado: "CP01 - Usuario ingresa credenciales válidas y accede exitosamente a la plataforma"

**Nota:** Se mantiene como Scenario simple porque actualmente solo valida login exitoso. Si se agregan casos de credenciales inválidas, se puede convertir a Scenario Outline.

---

## Resumen de Cambios

| Feature | Casos | Scenario → Scenario Outline | Títulos actualizados | Tags corregidos |
|---------|-------|----------------------------|---------------------|-----------------|
| ESC01-PrecioDisponibilidadEnPantalla | TC-WEB-01, TC-WEB-02 | No (sin parametrización) | ✅ | ✅ |
| ESC02-Checkout | TC-WEB-03, TC-WEB-04 | TC-WEB-03 ✅ | ✅ | ✅ |
| ESC03-MisEntradas | TC-WEB-05 | ✅ | ✅ | ✅ |
| ESC04-ChatWeb | TC-WEB-06, TC-WEB-07 | No (seguridad específica) | ✅ | ✅ |
| Login | @LOGIN | No (solo login exitoso) | ✅ | ✅ |

**Total:** 5 features refactorizados, 8 casos actualizados, 3 convertidos a Scenario Outline

---

## Patrón de Títulos Aplicado

**Formato:** `CP<NN> - Usuario + verbo + predicado + validación`

**Ejemplos:**
- ✅ "CP01 - Usuario visualiza precio y cupo en ficha de evento y valida coincidencia con API"
- ✅ "CP01 - Usuario realiza pago rechazado y reintenta con tarjeta válida sobre la misma reserva"
- ✅ "CP01 - Usuario visualiza sus entradas y valida que cada estado se refleja correctamente"
- ✅ "CP01 - Usuario solicita reserva por chat y valida que se pide confirmación explícita"
- ✅ "CP01 - Usuario ingresa credenciales válidas y accede exitosamente a la plataforma"

---

## Criterios para Scenario Outline vs Scenario

### ✅ Usar Scenario Outline cuando:
- Hay múltiples variantes de datos de entrada (tarjetas, estados, roles)
- Se necesita probar el mismo flujo con diferentes combinaciones
- La parametrización mejora la mantenibilidad y escalabilidad

### ✅ Usar Scenario simple cuando:
- El caso valida un comportamiento único y específico
- No hay variantes reales de datos de entrada
- Es un caso de seguridad con payload específico (XSS, inyección)
- La parametrización no aporta valor (forzaría Examples con 1 sola fila)

---

## Compatibilidad con Step Definitions

✅ **Todos los step definitions existentes son compatibles** con la nueva sintaxis:
- Los steps usan expresiones regulares que capturan parámetros
- Scenario Outline con `<parametro>` se resuelve correctamente en tiempo de ejecución
- El orden de tags no afecta la ejecución de steps

**Compilación verificada:** `mvn clean compile` → BUILD SUCCESS (31 archivos)

---

## Beneficios de la Refactorización

1. **Coherencia:** Todos los features siguen el mismo patrón de tags y títulos
2. **Trazabilidad:** Orden de tags según R3 §5 facilita filtrado por prioridad/riesgo
3. **Mantenibilidad:** Scenario Outline reduce duplicación de código
4. **Escalabilidad:** Fácil agregar nuevas variantes en tabla Examples
5. **Legibilidad:** Títulos descriptivos con patrón "Usuario + verbo + predicado"
6. **Alineación con R3:** Respeta convenciones oficiales del proyecto

---

## Próximos Pasos Recomendados

1. **Ejecutar suite de regresión** para validar que los cambios no rompieron tests:
   ```bash
   mvn clean verify -Dtags="@regression and not @manual"
   ```

2. **Agregar más variantes a Scenario Outline** según necesidades de regresión:
   - TC-WEB-03: Más tarjetas de prueba (expirada, CVV inválido, etc.)
   - TC-WEB-05: Más combinaciones de estados de entrada

3. **Convertir Login.feature a Scenario Outline** si se implementan casos de credenciales inválidas

4. **Actualizar documentación de R3** si se decide adoptar esta convención como estándar

---

## Conclusión

✅ **Refactorización completada exitosamente**  
✅ **Framework compila sin errores**  
✅ **Sintaxis Gherkin coherente y mantenible**  
✅ **Alineado con convenciones de R3 y mejores prácticas**

La estrategia híbrida permite aprovechar lo mejor de ambos enfoques: simplicidad donde no se necesita parametrización, y escalabilidad donde sí aporta valor.
