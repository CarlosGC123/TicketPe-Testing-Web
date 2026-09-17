Feature: ESC02 - Checkout (HU-E3.3)

  Background:
    Given usuario ingresa a la pagina de TicketPe
    When ingreso el correo electronico
    And ingreso el password
    And presiono el boton Ingresar
    Then valido el Login correcto de la pagina

  @TC-WEB-03 @ESC02 @web @p2 @alto @RSK-07 @smoke @regression @front
  Scenario Outline: CP01 - Usuario realiza pago rechazado y reintenta con tarjeta válida sobre la misma reserva
    # Trazabilidad: HU-E3.3 L231-L232
    # Riesgo: RSK-07 pago rechazado cancela la reserva
    # Oráculo: README L231 [Back+Front] · falla = defecto
    # Técnica: tabla de decisión (tarjeta × resultado)
    # Relación: TC-API-09 (mismo criterio por API)
    # Automatizable: Sí - UI + captura de red
    Given un usuario registrado esta en el checkout con una entrada
    When intento pagar con tarjeta "<tarjeta_rechazada>"
    And veo que el pago fue rechazado
    And intento pagar nuevamente con tarjeta "<tarjeta_aprobada>"
    Then valido que el pago fue exitoso
    And el formulario de pago sigue disponible tras el rechazo
    And veo la entrada en "Mis entradas"

    Examples:
      | tarjeta_rechazada  | tarjeta_aprobada   |
      | 4000000000000002   | 4242424242424242   |

  @TC-WEB-04 @ESC02 @web @p2 @medio @RSK-05 @regression @front
  Scenario: CP02 - Usuario valida que la cuenta regresiva del checkout usa la hora del servidor
    # Trazabilidad: HU-E3.3 L235-L236
    # Riesgo: RSK-05 reserva vencida mal gestionada: el comprador pierde el cupo o paga fuera de plazo
    # Oráculo: README L235 · falla = defecto. Margen de medición ±5 s, no es requisito
    # Técnica: transición de estados (pendiente → expirada)
    # Automatizable: Sí - UI con reloj del navegador desplazado (override de Date); si la herramienta no lo permite, manual
    Given tengo una reserva pendiente
    And adelanto el reloj del navegador 10 minutos
    When abro la pagina de checkout
    And espero que la cuenta regresiva llegue a cero
    Then valido que la cuenta regresiva usa la hora del servidor
    And veo el mensaje de reserva expirada
    And el boton de pago esta deshabilitado
