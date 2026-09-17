Feature: ESC02 - Checkout (HU-E3.3)

  @TC-WEB-03 @ESC02 @web @p2 @alto @RSK-07 @smoke @regression @front
  Scenario Outline: CP01 - Usuario realiza pago rechazado y reintenta con tarjeta válida sobre la misma reserva
    # Trazabilidad: HU-E3.3 L231-L232
    # Riesgo: RSK-07 pago rechazado cancela la reserva
    # Oráculo: README L231 [Back+Front] · falla = defecto
    # Técnica: tabla de decisión (tarjeta × resultado)
    # Relación: TC-API-09 (mismo criterio por API)
    # Automatizable: Sí - UI + captura de red
    Given un asistente recién registrado inicia sesión y está en el checkout de 1 entrada
    When paga con la tarjeta <tarjeta_rechazada>
    And paga de nuevo en la misma pantalla con la tarjeta <tarjeta_aprobada>
    Then tras el primer intento la pantalla informa que el pago fue rechazado y el formulario de pago sigue habilitado
    And ambos intentos llaman POST /api/core/reservas/:id/pago con el mismo id
    And la entrada aparece en "Mis entradas"

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
    Given un asistente con una reserva pendiente creada por API
    And el reloj del navegador adelantado 10 minutos
    When abre el checkout de esa reserva
    And espera a que llegue expira_en
    Then la cuenta regresiva muestra expira_en menos la hora del servidor (cabecera Date), ±5 s
    And al llegar expira_en la pantalla avisa que la reserva expiró y no permite pagar
