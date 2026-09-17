Feature: Ticket - Login

  Como usuario quiero hacer Login en la pagina de TicketPe
  para poder acceder a mis servicios

  @LOGIN
  Scenario: Usuario hace Login a la pagina de TicketPe
    Given usuario ingresa a la pagina de TicketPe
    When ingreso el correo electronico
    And ingreso el password
    And presiono el boton Ingresar
    Then valido el Login correcto de la pagina




