# Suite de Regresión Web - TicketPe Testing Framework

**Framework de pruebas automatizadas Web con soporte para Continuous Testing**

Equipo **TesTitans** · Testathon 2026

---

## 📋 Índice

1. [Estrategia de Regresión](#estrategia-de-regresión)
2. [Suites Configuradas](#suites-configuradas)
3. [Casos de Prueba Incluidos](#casos-de-prueba-incluidos)
4. [Componentes Reutilizables](#componentes-reutilizables)
5. [Guía de Ejecución](#guía-de-ejecución)
6. [Continuous Testing en CI/CD](#continuous-testing-en-cicd)
7. [Criterios de Calidad](#criterios-de-calidad)

---

## 🎯 Estrategia de Regresión

La suite de regresión está diseñada siguiendo los lineamientos de **R1 §10.4 (CI/CD y Continuous Testing)** y **R1 §10.5 (Regresión estable, no flaky)** para soportar continuous testing ante cualquier despliegue.

### Principios Fundamentales

1. **Dos niveles de ejecución:**
   - **@smoke**: Suite crítica rápida (6 casos) que se ejecuta en cada push/PR
   - **@regression**: Suite completa (9 casos) que se ejecuta en corrida nocturna programada

2. **Criterios no-flaky (R1 §10.5):**
   - ✅ Datos propios por caso creados vía API
   - ✅ Sin esperas fijas (`Thread.sleep`)
   - ✅ Sin dependencia de estado compartido entre equipos
   - ✅ Fallo real, no reintento silencioso
   - ✅ Locators y esperas explícitas en Web

3. **Patrón Screenplay de 3 capas (CTAL-TAE v2.0):**
   - **Librerías Base**: `interaction/`, `util/` (bajo nivel, sin lógica de negocio)
   - **Lógica de Negocio**: `task/`, `questions/`, `page/` (reutilizables entre escenarios)
   - **Guiones de Prueba**: `.feature` (Gherkin de R3, copiado verbatim)

---

## 🏃 Suites Configuradas

### 1. Smoke Test Suite (`@smoke`)

**Propósito:** Validación crítica rápida en cada push/PR a main

**Runner:** `CucumberTestSuite.java` filtrado con `-Dcucumber.filter.tags="@smoke"`

**Tag:** `@smoke and not @manual`

**Casos incluidos:** 6 casos críticos y rápidos

**Tiempo estimado:** < 12 minutos (< 2 min por caso)

**Cuándo se ejecuta:**
- En cada push a `main`
- En cada Pull Request
- Antes de merge

**Criterios de inclusión:**
- Ejecución rápida (< 2 min por caso)
- Críticos para el negocio o seguridad
- Sin esperas largas
- Datos propios creados por API

---

### 2. Regression Test Suite (`@regression`)

**Propósito:** Suite completa de regresión para validación exhaustiva

**Runner:** `CucumberTestSuite.java` filtrado con `-Dcucumber.filter.tags="@regression"`

**Tag:** `@regression and not @manual`

**Casos incluidos:** 9 casos (todos los automatizados)

**Tiempo estimado:** < 20 minutos

**Cuándo se ejecuta:**
- Corrida nocturna programada (`schedule`)
- Antes de releases
- Validación completa post-despliegue

**Criterios de inclusión:**
- Todos los casos automatizados Web
- Cobertura completa de flujos críticos
- Independencia entre casos

---

## 📝 Casos de Prueba Incluidos

### Suite @smoke (6 casos)

| ID | Descripción | Tags | Prioridad | Riesgo | Tiempo |
|---|---|---|---|---|---|
| **Login** | Autenticación básica | `@LOGIN @front @smoke @regression @critical` | Crítico | - | < 1 min |
| **TC-WEB-01** | Precio/disponibilidad coincide con API | `@TC-WEB-01 @ESC01 @web @front @smoke @regression @p2 @alto @RSK-17` | Alto | RSK-17 | < 2 min |
| **TC-WEB-02** | Evento agotado se muestra correctamente | `@TC-WEB-02 @ESC01 @web @front @smoke @regression @p2 @alto @RSK-17` | Alto | RSK-17 | < 2 min |
| **TC-WEB-03** | Pago rechazado permite reintentar | `@TC-WEB-03 @ESC02 @web @front @smoke @regression @p2 @alto @RSK-07` | Alto | RSK-07 | < 2 min |
| **TC-WEB-06** | Chat pide confirmación antes de reservar | `@TC-WEB-06 @ESC04 @web @front @smoke @regression @security @p1 @critico @RSK-15` | Crítico | RSK-15 | < 2 min |
| **TC-WEB-07** | Chat previene XSS | `@TC-WEB-07 @ESC04 @web @front @smoke @regression @security @p2 @critico @RSK-25` | Crítico | RSK-25 | < 2 min |

### Suite @regression (9 casos = @smoke + 3 adicionales)

Incluye todos los casos de @smoke más:

| ID | Descripción | Tags | Prioridad | Riesgo | Tiempo |
|---|---|---|---|---|---|
| **TC-WEB-04** | Cuenta regresiva usa hora del servidor | `@TC-WEB-04 @ESC02 @web @front @regression @p2 @medio @RSK-05` | Medio | RSK-05 | < 3 min |
| **TC-WEB-05** | "Mis entradas" muestra estados reales | `@TC-WEB-05 @ESC03 @web @front @regression @p2 @alto @RSK-09` | Alto | RSK-09 | < 3 min |
| **TC-WEB-08** | Valida todo el catálogo contra la API | `@TC-WEB-08 @ESC01 @web @front @regression @PIPELINE_REGRESION @p1 @critico @RSK-17` | Crítico | RSK-17 | < 3 min |

### Cobertura de Riesgos

La suite de regresión cubre los siguientes riesgos críticos de R1 §6:

- **RSK-17**: Pantalla distinta a la API (TC-WEB-01, TC-WEB-02)
- **RSK-07**: Pago rechazado cancela la reserva (TC-WEB-03)
- **RSK-05**: Reserva vencida mal gestionada (TC-WEB-04)
- **RSK-09**: Transferencia mal reflejada (TC-WEB-05)
- **RSK-15**: IA ejecuta acción sin confirmación (TC-WEB-06)
- **RSK-25**: Inyección en texto libre - XSS (TC-WEB-07)

---

## 🧩 Componentes Reutilizables

El framework está construido siguiendo el **patrón Screenplay de 3 capas** (CTAL-TAE v2.0), permitiendo máxima reutilización de componentes.

### Librerías Base (Capa 1)

**Ubicación:** `src/main/java/interaction/`, `src/main/java/util/`

| Componente | Descripción | Usado por |
|---|---|---|
| `CargarPaginaPrincipal` | Abre URL con reintentos y recarga | Todos los casos |
| `ClienteApiCore` | Cliente HTTP hacia /api/core/* | Preparación de datos |
| `ObtenerCredenciales` | Decodifica credenciales Base64 | Login, casos con autenticación |
| `FormatoConsola` | Formato de logs y banners | Todos los runners |
| `DecodificadorBase64` | Decodifica strings Base64 | Credenciales |

### Lógica de Negocio (Capa 2)

**Ubicación:** `src/main/java/task/`, `src/main/java/questions/`, `src/main/java/page/`

#### Tasks (Acciones de negocio)

| Task | Descripción | Usado por |
|---|---|---|
| `IniciarSesion` | Login completo con credenciales | TC-WEB-03, TC-WEB-05, TC-WEB-06 |
| `AbrirFichaDeEvento` | Navega a la ficha de un evento | TC-WEB-01, TC-WEB-02 |
| `CrearReservaPorApi` | Crea reserva vía API | TC-WEB-03, TC-WEB-04 |
| `ComprarEntradasPorApi` | Compra entradas vía API | TC-WEB-05 |
| `PagarConTarjeta` | Ingresa tarjeta y confirma pago | TC-WEB-03 |
| `EnviarMensajeAlChat` | Envía mensaje al chat de IA | TC-WEB-06, TC-WEB-07 |
| `EscribeTexto` | Escribe en un campo | Login, checkout |
| `DarClick` | Click en elemento con espera | Todos los casos |

#### Questions (Validaciones de negocio)

| Question | Descripción | Usado por |
|---|---|---|
| `PrecioDeTipoEntradaCoincideConApi` | Valida precio Front vs API | TC-WEB-01 |
| `DisponibleDeTipoEntradaCoincideConApi` | Valida disponible Front vs API | TC-WEB-01 |
| `EventoEstaAgotadoEnPantalla` | Valida etiqueta "Agotado" | TC-WEB-02 |
| `PagoFueRechazado` | Valida mensaje de rechazo | TC-WEB-03 |
| `FormularioDePagoSigueHabilitado` | Valida reintento habilitado | TC-WEB-03 |
| `EntradaApareceEnMisEntradas` | Valida entrada en listado | TC-WEB-03, TC-WEB-05 |
| `EstadoDeEntradaCoincideConApi` | Valida estado Front vs API | TC-WEB-05 |
| `ChatPideConfirmacionAntesDeActuar` | Valida confirmación en chat | TC-WEB-06 |
| `MensajeDelUsuarioSeMuestraComoTextoLiteral` | Valida escape de HTML/XSS | TC-WEB-07 |
| `ElementoEsVisible` | Espera y valida visibilidad | Todos los casos |
| `ElementoEsClickable` | Espera y valida clickeabilidad | Todos los casos |

#### Pages (Locators)

| Page | Descripción | Targets |
|---|---|---|
| `DashboardPage` | Página principal | BOTON_INGRESAR, CUENTA_ACCESO |
| `Login` | Formulario de login | CAMPO_CORREO, CAMPO_CONTRASENA, BOTON_INGRESAR |
| `EventoPage` | Ficha del evento | PRECIO_TIPO_ENTRADA, DISPONIBLE_TIPO_ENTRADA, ETIQUETA_AGOTADO |
| `CheckoutPage` | Página de checkout | CAMPO_NUMERO_TARJETA, BOTON_PAGAR, MENSAJE_ERROR |
| `MisEntradasPage` | Listado de entradas | ENTRADA_ITEM, ESTADO_ENTRADA |
| `ChatPage` | Chat de IA | BOTON_ABRIR_CHAT, CAMPO_MENSAJE, BOTON_ENVIAR, ULTIMA_RESPUESTA |

### Guiones de Prueba (Capa 3)

**Ubicación:** `src/test/resources/features/`

| Feature | Casos | Step Definitions |
|---|---|---|
| `Login.feature` | Login | `LoginDefinition.java` |
| `ESC01-PrecioDisponibilidadEnPantalla.feature` | TC-WEB-01, TC-WEB-02 | `PrecioDisponibilidadDefinition.java` |
| `ESC02-Checkout.feature` | TC-WEB-03, TC-WEB-04 | `CheckoutDefinition.java` |
| `ESC03-MisEntradas.feature` | TC-WEB-05 | `MisEntradasDefinition.java` |
| `ESC04-ChatWeb.feature` | TC-WEB-06, TC-WEB-07 | `ChatWebDefinition.java` |

---

## 🚀 Guía de Ejecución

### Requisitos

- **Java 17+**
- **Maven 3.9+**
- **Chrome** (versión actual)
- Variables de entorno: `CORREO` y `PASSWORD` en Base64

### Ejecución Local

#### 1. Suite de Smoke (6 casos críticos)

```bash
# Windows PowerShell
$env:CORREO = [Convert]::ToBase64String([Text.Encoding]::UTF8.GetBytes("usuario@correo.com"))
$env:PASSWORD = [Convert]::ToBase64String([Text.Encoding]::UTF8.GetBytes("mi-password"))
mvn clean test -Dcucumber.filter.tags="@smoke" -Denvironment=PRODUCCION
```

```bash
# Linux/Mac
export CORREO=$(printf 'usuario@correo.com' | base64)
export PASSWORD=$(printf 'mi-password' | base64)
mvn clean test -Dcucumber.filter.tags="@smoke" -Denvironment=PRODUCCION
```

**Tiempo estimado:** < 12 minutos

---

#### 2. Suite de Regresión Completa (9 casos)

```bash
# Windows PowerShell
mvn clean test -Dcucumber.filter.tags="@regression" -Denvironment=PRODUCCION
```

```bash
# Linux/Mac
mvn clean test -Dcucumber.filter.tags="@regression" -Denvironment=PRODUCCION
```

**Tiempo estimado:** < 20 minutos

---

#### 3. Runner General (sin filtro: usa el tag de `@CucumberOptions`, hoy `@PIPELINE_REGRESION`)

```bash
mvn clean test -Denvironment=PRODUCCION
```

---

#### 4. Ejecutar un caso específico por tag

```bash
# Solo casos de seguridad
mvn clean test -Dcucumber.filter.tags="@security" -Denvironment=PRODUCCION

# Solo un caso específico
mvn clean test -Dcucumber.filter.tags="@TC-WEB-01" -Denvironment=PRODUCCION

# Casos críticos
mvn clean test -Dcucumber.filter.tags="@critico" -Denvironment=PRODUCCION
```

---

### Reporte Serenity

Después de la ejecución, el reporte HTML se genera en:

```
target/site/serenity/index.html
```

Para abrir automáticamente (Windows):

```bash
mvn verify
# El reporte se abre automáticamente al finalizar
```

---

## 🔄 Continuous Testing en CI/CD

> **Estado real:** el único workflow implementado es [`.github/workflows/e2e.yml`](.github/workflows/e2e.yml):
> disparo **manual** (`workflow_dispatch`), ejecuta `mvn clean test -Denvironment=PRODUCCION` con el tag
> `@PIPELINE_REGRESION` (solo **TC-WEB-08**) y publica el reporte Serenity en GitHub Pages con
> `if: always()`. Los dos pipelines de abajo son **propuestas** todavía no implementadas.

### Estrategia de GitHub Actions (R1 §10.4)

El framework está preparado para integrarse con GitHub Actions siguiendo la estrategia definida en R1:

#### 1. Pipeline de Smoke (cada push/PR)

```yaml
name: Smoke Tests

on:
  push:
    branches: [ main ]
  pull_request:
    branches: [ main ]

jobs:
  smoke:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - uses: actions/setup-java@v3
        with:
          java-version: '17'
      - uses: browser-actions/setup-chrome@latest
      
      - name: Run Smoke Tests
        env:
          CORREO: ${{ vars.CORREO }}
          PASSWORD: ${{ vars.PASSWORD }}
        run: |
          mvn clean test -Dcucumber.filter.tags="@smoke" -Denvironment=PRODUCCION
      
      - name: Upload Serenity Report
        if: always()
        uses: actions/upload-artifact@v3
        with:
          name: serenity-smoke-report
          path: target/site/serenity/
```

**Características:**
- ✅ Se ejecuta en cada push/PR
- ✅ Bloquea el merge si falla
- ✅ Reporte publicado aunque falle
- ✅ Tiempo: < 15 minutos

---

#### 2. Pipeline de Regresión Completa (nocturno)

```yaml
name: Regression Tests

on:
  schedule:
    - cron: '0 2 * * *'  # 2 AM diario
  workflow_dispatch:     # Manual

jobs:
  regression:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - uses: actions/setup-java@v3
        with:
          java-version: '17'
      - uses: browser-actions/setup-chrome@latest
      
      - name: Run Regression Tests
        env:
          CORREO: ${{ vars.CORREO }}
          PASSWORD: ${{ vars.PASSWORD }}
        run: |
          mvn clean test -Dcucumber.filter.tags="@regression" -Denvironment=PRODUCCION
      
      - name: Upload Serenity Report
        if: always()
        uses: actions/upload-artifact@v3
        with:
          name: serenity-regression-report
          path: target/site/serenity/
      
      - name: Deploy to GitHub Pages
        if: always()
        uses: peaceiris/actions-gh-pages@v3
        with:
          github_token: ${{ secrets.GITHUB_TOKEN }}
          publish_dir: target/site/serenity/
```

**Características:**
- ✅ Se ejecuta diariamente a las 2 AM
- ✅ También se puede ejecutar manualmente
- ✅ Reporte publicado en GitHub Pages
- ✅ Tiempo: < 25 minutos

---

### Configuración de Variables en GitHub

1. **Settings > Secrets and variables > Actions > Variables**
2. Crear variables:
   - `CORREO`: correo en Base64
   - `PASSWORD`: password en Base64

```bash
# Generar valores Base64
echo -n "usuario@correo.com" | base64
echo -n "mi-password" | base64
```

---

## ✅ Criterios de Calidad

### Criterios No-Flaky (R1 §10.5)

Todos los casos cumplen:

| Criterio | Implementación | Verificación |
|---|---|---|
| **Datos propios por caso** | Cada caso crea su cuenta/reserva/cupón por API con UUID único | ✅ Implementado |
| **Sin esperas fijas** | Uso de `WebDriverWait` y esperas condicionadas, nunca `Thread.sleep` | ✅ Implementado |
| **Sin estado compartido** | No se usan cuentas compartidas entre equipos | ✅ Implementado |
| **Fallo real** | No hay reintentos automáticos que oculten flakiness | ✅ Implementado |
| **Locators explícitos** | Esperas basadas en estado observable (Questions) | ✅ Implementado |

### Independencia de Casos

- ✅ Cada caso es independiente y puede ejecutarse en cualquier orden
- ✅ No hay dependencias entre casos
- ✅ Cada caso limpia sus propios datos (cuando aplica)
- ✅ Ejecución paralela soportada (configurar en `pom.xml`)

### Mantenibilidad

- ✅ Patrón Screenplay de 3 capas: cambios en UI solo afectan `page/`
- ✅ Lógica de negocio reutilizable en `task/` y `questions/`
- ✅ Gherkin de R3 copiado verbatim: trazabilidad 1:1
- ✅ Componentes documentados y con responsabilidad única

---

## 📊 Métricas de la Suite

| Métrica | Valor |
|---|---|
| **Total de casos automatizados** | 8 |
| **Casos en @smoke** | 6 |
| **Casos en @regression** | 8 |
| **Casos de seguridad (@security)** | 2 |
| **Cobertura de riesgos críticos** | 6/27 (RSK-05, RSK-07, RSK-09, RSK-15, RSK-17, RSK-25) |
| **Tasks reutilizables** | 7 |
| **Questions reutilizables** | 10 |
| **Pages** | 8 |
| **Tiempo estimado @smoke** | < 12 min |
| **Tiempo estimado @regression** | < 20 min |

---

## 🎯 Próximos Pasos Recomendados

1. **Integración con GitHub Actions:**
   - Crear workflows para @smoke y @regression
   - Configurar variables de entorno
   - Publicar reportes en GitHub Pages

2. **Expansión de cobertura:**
   - Agregar casos de transferencia de entradas
   - Agregar casos de reembolso
   - Agregar casos de cupones

3. **Optimización:**
   - Configurar ejecución paralela en `pom.xml`
   - Implementar pool de datos de prueba
   - Agregar métricas de performance

4. **Monitoreo:**
   - Dashboard de métricas de ejecución
   - Alertas en caso de fallos recurrentes
   - Análisis de tendencias de estabilidad

---

## 📚 Referencias

- **R1 - Estrategia y Plan de Pruebas:** `R1-estrategia-plan-pruebas.md`
  - §10.4: CI/CD y Continuous Testing
  - §10.5: Regresión estable, no flaky
  - §11: Convención de tags

- **R3 - Diseño de Pruebas:** `R3-diseno-pruebas/casos-prueba.md`
  - Gherkin fuente de todos los casos
  - Trazabilidad a HU y riesgos

- **System Prompt:** `R8-Agente-IA-pruebas/prompts/system-prompts/web-java-screenplay-builder.md`
  - Patrón Screenplay de 3 capas
  - Técnicas de automatización CTAL-TAE v2.0

---

**Última actualización:** 2026-09-17  
**Versión del framework:** 1.0  
**Estado:** ✅ Listo para Continuous Testing
