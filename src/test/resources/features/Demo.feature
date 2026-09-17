Feature: Demo

  Background:
    Given usuario ingresa a la pagina de Youtube

  @DEMO
  Scenario Outline: Usuario puede buscar un video en Youtube

    When usuario busca el video "<Video>"
    And presiono el boton buscar
    Then valido resultados encontrados

    Examples:
      | Video |
      | ISTQB |



