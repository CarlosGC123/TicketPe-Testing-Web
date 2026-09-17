Feature: ESC00 - Autenticación (HU-E1.2)

  Como usuario quiero hacer Login en la pagina de TicketPe
  para poder acceder a mis servicios

  @LOGIN @ESC00 @web @p1 @critico @smoke @regression @critical @front
  Scenario: CP01 - Usuario ingresa credenciales válidas y accede exitosamente a la plataforma
    Given usuario ingresa a la pagina de TicketPe
    When ingreso el correo electronico
    And ingreso el password
    And presiono el boton Ingresar
    Then valido el Login correcto de la pagina




