Feature: ESC01 - Precio y disponibilidad en pantalla (HU-E2.2)

  Background:
    Given usuario ingresa a la pagina de TicketPe
    When ingreso el correo electronico
    And ingreso el password
    And presiono el boton Ingresar
    Then valido el Login correcto de la pagina

  @TC-WEB-01 @ESC01 @web @p2 @alto @RSK-17 @smoke @regression @front
  Scenario Outline: CP01 - Usuario visualiza precio y cupo en ficha de evento y valida coincidencia con datos del sistema
    # Trazabilidad: HU-E2.2 L205-L207
    # Riesgo: RSK-17 pantalla distinta a la API
    # Oráculo: README L205-L207 (pantalla vs respuesta cruda en el mismo instante) · falla = defecto
    # Técnica: comparación de capas (Front vs Back)
    # Automatizable: Sí - UI + captura de la respuesta de red
    Given selecciono el evento "<evento>" en el catalogo
    When abro la ficha del evento
    Then valido que el precio de la tarjeta coincide con el precio en el detalle
    And valido que la disponibilidad de la tarjeta coincide con la disponibilidad en el detalle

    Examples:
      | evento                      |
      | Encuentro de Food Trucks    |

  @TC-WEB-02 @ESC01 @web @p2 @alto @RSK-17 @smoke @regression @front
  Scenario: CP02 - Usuario visualiza evento agotado y valida que no puede comprar
    # Trazabilidad: HU-E2.2 L203-L204
    # Riesgo: RSK-17 pantalla distinta a la API
    # Oráculo: README L203 · falla = defecto
    # Técnica: partición de equivalencia (evento sin cupo)
    # Automatizable: Sí - UI; bloqueado si ningún evento tiene disponible 0 en todos sus tipos
    Given selecciono un evento agotado
    When abro la ficha del evento
    Then veo el mensaje "Agotado"
    And el boton de compra no esta disponible

  @TC-WEB-08 @ESC01 @web @p1 @critico @RSK-17 @PIPELINE_REGRESION @regression @front
  Scenario: CP03 - Usuario valida precio y disponibilidad de todos los eventos del catalogo
    # Trazabilidad: HU-E2.2 L205-L207
    # Riesgo: RSK-17 pantalla distinta a la API
    # Oráculo: README L205-L207 (pantalla vs respuesta cruda en el mismo instante) · falla = defecto
    # Técnica: comparación de capas (Front vs Back) - iteración completa del catálogo
    # Automatizable: Sí - UI con iteración sobre todos los eventos
    # Continuous Testing: Candidato fuerte para pipeline de regresión
    Given obtengo la lista de todos los eventos del catalogo
    When valido precio y disponibilidad para cada evento del catalogo
    Then todos los eventos deben tener datos consistentes entre catalogo y detalle
