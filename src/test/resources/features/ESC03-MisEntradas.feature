Feature: ESC03 - Mis entradas (HU-E4.1)

  Background:
    Given usuario ingresa a la pagina de TicketPe
    When ingreso el correo electronico
    And ingreso el password
    And presiono el boton Ingresar
    Then valido el Login correcto de la pagina

  @TC-WEB-05 @ESC03 @web @p2 @alto @RSK-09 @regression @front
  Scenario Outline: CP01 - Usuario visualiza sus entradas y valida que cada estado se refleja correctamente
    # Trazabilidad: HU-E4.1 L246-L247 · ejemplo L373-L375
    # Riesgo: RSK-09 transferencia mal reflejada · RSK-10 reembolso mal reflejado
    # Oráculo: README L246, L374 + GET /api/core/entradas/:id · falla = defecto. Rótulo de la entrada recibida: OBS-13, no se evalúa
    # Técnica: transición de estados (vista de cada estado)
    # Relación: TC-API-14, TC-API-16, TC-API-17
    # Automatizable: Sí - datos por API + verificación UI
    Given el usuario A compro 4 entradas para un evento
    And modifico los estados de las entradas del usuario A
    And el usuario B recibe una entrada transferida
    When el usuario A abre "Mis entradas"
    And el usuario B abre "Mis entradas"
    Then el usuario A ve 3 entradas con estados "<estado_entrada_1>", "<estado_entrada_2>" y "<estado_entrada_3>"
    And el usuario A no ve la entrada transferida
    And el usuario B ve la entrada transferida

    Examples:
      | estado_entrada_1 | estado_entrada_2         | estado_entrada_3 |
      | válida           | en trámite de reembolso  | usada            |
