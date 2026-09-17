Feature:

  Background:
    Given usuario ingresa a la pagina de TicketPe
    When ingreso el correo electronico
    And ingreso el password
    And presiono el boton Ingresar
    Then valido el Login correcto de la pagina

  @ESC01_CP001
  Scenario Outline: CP01 - Usuario valida que el precio del catálogo sea igual al del checkout

