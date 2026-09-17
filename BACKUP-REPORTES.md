# Sistema de Backup Automático de Reportes

## Descripción

Este proyecto incluye un sistema automático de backup de reportes de pruebas que se ejecuta al finalizar la ejecución de las pruebas. El backup incluye todos los reportes generados con un timestamp único para diferenciar cada ejecución.

## ¿Qué se respalda?

El sistema crea un backup de los siguientes reportes:

1. **Reportes de Serenity BDD** (`target/site/serenity/`)
   - Reportes HTML interactivos con capturas de pantalla
   - Gráficos de resultados y tendencias
   - Detalles de cada escenario ejecutado

2. **Reportes de Surefire** (`target/surefire-reports/`)
   - Reportes XML y TXT de JUnit
   - Resultados detallados de cada test
   - Stack traces de errores

3. **Reportes de Cucumber** (`target/cucumber/`)
   - Reportes JSON de Cucumber
   - Datos estructurados de escenarios y steps

## ¿Cómo funciona?

El sistema utiliza el plugin `maven-antrun-plugin` configurado en el `pom.xml` que:

1. Se ejecuta automáticamente en la fase `verify` del ciclo de vida de Maven
2. Genera un timestamp con formato `yyyyMMdd-HHmmss` (ejemplo: `20260917-145030`)
3. Crea un directorio de backup: `reportes-backup/backup-{timestamp}/`
4. Copia todos los reportes a subdirectorios organizados:
   - `reportes-backup/backup-{timestamp}/serenity-reports/`
   - `reportes-backup/backup-{timestamp}/surefire-reports/`
   - `reportes-backup/backup-{timestamp}/cucumber-reports/`

## ¿Cómo usar el backup?

### Ejecución estándar con backup

Para ejecutar las pruebas y generar el backup automáticamente:

```bash
mvn clean verify
```

Este comando:
1. Limpia el directorio `target/`
2. Compila el código fuente
3. Ejecuta las pruebas
4. Genera los reportes de Serenity
5. **Crea el backup automáticamente**
6. Abre el reporte de Serenity en el navegador

### Ejecución sin backup

Si solo quieres ejecutar las pruebas sin generar backup:

```bash
mvn clean test
```

**Nota:** El comando `mvn test` NO ejecuta el backup porque la fase `verify` no se alcanza.

## Estructura del directorio de backup

```
reportes-backup/
├── backup-20260917-145030/
│   ├── serenity-reports/
│   │   ├── index.html
│   │   ├── css/
│   │   ├── js/
│   │   └── ...
│   ├── surefire-reports/
│   │   ├── TEST-runner.CucumberTestSuite.xml
│   │   ├── runner.CucumberTestSuite.txt
│   │   └── ...
│   └── cucumber-reports/
│       └── cucumber.json
├── backup-20260917-160245/
│   └── ...
└── backup-20260918-093015/
    └── ...
```

## Ventajas del sistema

✅ **Automático**: No requiere intervención manual  
✅ **Organizado**: Cada ejecución tiene su propio directorio con timestamp  
✅ **Completo**: Incluye todos los tipos de reportes generados  
✅ **Seguro**: Usa `failonerror="false"` para no fallar si algún directorio no existe  
✅ **Trazable**: El timestamp permite identificar exactamente cuándo se ejecutó cada prueba  
✅ **Histórico**: Mantiene un historial completo de todas las ejecuciones  

## Gestión de backups

### Limpieza manual

Los backups se acumulan en el directorio `reportes-backup/`. Para limpiar backups antiguos:

```bash
# Windows PowerShell - Eliminar backups de más de 30 días
Get-ChildItem "reportes-backup" -Directory | Where-Object { $_.CreationTime -lt (Get-Date).AddDays(-30) } | Remove-Item -Recurse -Force
```

### Exclusión de Git

El directorio `reportes-backup/` debe estar en `.gitignore` para no versionar los reportes:

```
# Backups de reportes
reportes-backup/
```

## Configuración avanzada

### Cambiar el formato del timestamp

Edita el `pom.xml` en la línea del formato:

```xml
<format property="backup.timestamp" pattern="yyyyMMdd-HHmmss"/>
```

Formatos disponibles:
- `yyyyMMdd-HHmmss` → `20260917-145030`
- `yyyy-MM-dd_HH-mm-ss` → `2026-09-17_14-50-30`
- `yyyyMMdd` → `20260917` (solo fecha)

### Cambiar la ubicación del backup

Edita el `pom.xml` en la línea de la propiedad `backup.dir`:

```xml
<property name="backup.dir" value="${project.basedir}/reportes-backup/backup-${backup.timestamp}"/>
```

Puedes cambiar `reportes-backup` por cualquier otra ruta.

### Agregar más directorios al backup

Para incluir directorios adicionales, agrega un nuevo bloque `<copy>` en el `pom.xml`:

```xml
<!-- Copiar logs de la aplicación -->
<copy todir="${backup.dir}/logs" failonerror="false">
    <fileset dir="${project.build.directory}/logs" erroronmissingdir="false"/>
</copy>
```

## Solución de problemas

### El backup no se genera

**Causa**: Ejecutaste `mvn test` en lugar de `mvn verify`  
**Solución**: Usa `mvn clean verify` para activar la fase de backup

### El directorio de backup está vacío

**Causa**: Los reportes no se generaron antes del backup  
**Solución**: Verifica que las pruebas se ejecutaron correctamente y que los reportes existen en `target/`

### Error de permisos al crear el backup

**Causa**: No tienes permisos de escritura en el directorio del proyecto  
**Solución**: Ejecuta Maven con permisos adecuados o cambia la ubicación del backup

## Integración con CI/CD

### Jenkins

```groovy
stage('Test & Backup') {
    steps {
        sh 'mvn clean verify'
        archiveArtifacts artifacts: 'reportes-backup/**/*', fingerprint: true
    }
}
```

### GitLab CI

```yaml
test:
  script:
    - mvn clean verify
  artifacts:
    paths:
      - reportes-backup/
    expire_in: 30 days
```

### GitHub Actions

```yaml
- name: Run tests and backup reports
  run: mvn clean verify
  
- name: Upload backup
  uses: actions/upload-artifact@v3
  with:
    name: test-reports-backup
    path: reportes-backup/
    retention-days: 30
```

## Mensajes de consola

Durante la ejecución, verás mensajes como:

```
========================================
Generando backup de reportes de pruebas
Timestamp: 20260917-145030
Destino: C:\SCRIPTS REPOSITORY\CONCURSO TESTATHON\TicketPe-Testing-Web\reportes-backup\backup-20260917-145030
========================================
[copy] Copying 245 files to C:\...\reportes-backup\backup-20260917-145030\serenity-reports
[copy] Copying 3 files to C:\...\reportes-backup\backup-20260917-145030\surefire-reports
[copy] Copying 1 file to C:\...\reportes-backup\backup-20260917-145030\cucumber-reports
========================================
Backup completado exitosamente
Ubicación: C:\SCRIPTS REPOSITORY\CONCURSO TESTATHON\TicketPe-Testing-Web\reportes-backup\backup-20260917-145030
========================================
```

## Mantenimiento

- **Revisar backups periódicamente**: Asegúrate de que los backups se están generando correctamente
- **Limpiar backups antiguos**: Evita que el directorio crezca indefinidamente
- **Verificar espacio en disco**: Los reportes de Serenity pueden ocupar varios MB por ejecución
- **Documentar ejecuciones importantes**: Renombra los directorios de backups importantes para identificarlos fácilmente

---

**Última actualización**: 2026-09-17  
**Versión del plugin**: maven-antrun-plugin 3.1.0
