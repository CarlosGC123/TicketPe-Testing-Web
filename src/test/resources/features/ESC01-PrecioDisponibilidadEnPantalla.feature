Feature: ESC01 - Precio y disponibilidad en pantalla (HU-E2.2)

  @TC-WEB-01 @ESC01 @web @p2 @alto @RSK-17
  Scenario: La ficha del evento muestra el precio y cupo que devuelve la API al cargar
    # Trazabilidad: HU-E2.2 L205-L207
    # Riesgo: RSK-17 pantalla distinta a la API
    # Oráculo: README L205-L207 (pantalla vs respuesta cruda en el mismo instante) · falla = defecto
    # Técnica: comparación de capas (Front vs Back)
    # Automatizable: Sí - UI + captura de la respuesta de red
    Given un evento con venta abierta
    When abro su ficha en https://testathon.testingperu.com
    Then la página llama GET /api/core/eventos/:id/disponibilidad al cargar
    And para cada tipo de entrada, el precio y el disponible en pantalla son los de esa respuesta

  @TC-WEB-02 @ESC01 @web @p2 @alto @RSK-17
  Scenario: Un evento sin cupo se muestra "Agotado" y no permite comprar
    # Trazabilidad: HU-E2.2 L203-L204
    # Riesgo: RSK-17 pantalla distinta a la API
    # Oráculo: README L203 · falla = defecto
    # Técnica: partición de equivalencia (evento sin cupo)
    # Automatizable: Sí - UI; bloqueado si ningún evento tiene disponible 0 en todos sus tipos
    Given un evento cuyo GET /api/core/eventos/:id/disponibilidad tiene disponible 0 en todos los tipos
    When abro su ficha
    Then se muestra "Agotado"
    And no hay botón de compra habilitado
