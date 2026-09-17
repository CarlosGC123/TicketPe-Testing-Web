package util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Clase utilitaria para impresión formateada en consola.
 * Provee diferentes estilos visuales para los logs del framework.
 *
 * Uso rápido:
 *   FormatoConsola.titulo("Mi Título");
 *   FormatoConsola.paso("Ejecutando paso 1");
 *   FormatoConsola.exito("Elemento encontrado");
 *   FormatoConsola.error("Falló la validación");
 *   FormatoConsola.advertencia("Reintentando...");
 *   FormatoConsola.info("URL: https://...");
 *   FormatoConsola.separador();
 *   FormatoConsola.tabla("Producto", "PERFUME");
 *   FormatoConsola.banner("INICIO DE SUITE");
 *   FormatoConsola.tiempo("INICIO");
 *   FormatoConsola.reintento(2, 5, "Buscador principal");
 *   FormatoConsola.escenario("Buscar perfume en Mercado Libre");
 */
public class FormatoConsola {

    // ─── Códigos de color ANSI ────────────────────────────────────────────────
    private static final String RESET    = "\u001B[0m";
    private static final String NEGRITA  = "\u001B[1m";
    private static final String VERDE    = "\u001B[32m";
    private static final String ROJO     = "\u001B[31m";
    private static final String AMARILLO = "\u001B[33m";
    private static final String CYAN     = "\u001B[36m";
    private static final String BLANCO   = "\u001B[37m";
    private static final String MAGENTA  = "\u001B[35m";
    private static final String AZUL     = "\u001B[34m";

    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm:dd");
    private static final int ANCHO = 50;

    private FormatoConsola() { }

    // ─── 1. TÍTULO con caja doble ─────────────────────────────────────────────
    /**
     * Imprime un título destacado con borde doble.
     * ╔══════════════════════════════════════════════════╗
     * ║                   MI TÍTULO                      ║
     * ╚══════════════════════════════════════════════════╝
     */
    public static void titulo(String mensaje) {
        String linea = repetir("═", ANCHO);
        System.out.println(NEGRITA + CYAN + "╔" + linea + "╗" + RESET);
        System.out.println(NEGRITA + CYAN + "║" + centrar(mensaje.toUpperCase(), ANCHO) + "║" + RESET);
        System.out.println(NEGRITA + CYAN + "╚" + linea + "╝" + RESET);
    }

    // ─── 2. BANNER con asteriscos ─────────────────────────────────────────────
    /**
     * Imprime un banner grande con asteriscos (ideal para inicio/fin de suite).
     * ****************************************************
     * *                INICIO DE SUITE                   *
     * ****************************************************
     */
    public static void banner(String mensaje) {
        String linea = repetir("*", ANCHO + 2);
        System.out.println(NEGRITA + MAGENTA + linea + RESET);
        System.out.println(NEGRITA + MAGENTA + "*" + centrar(mensaje.toUpperCase(), ANCHO) + "*" + RESET);
        System.out.println(NEGRITA + MAGENTA + linea + RESET);
    }

    // ─── 3. PASO de ejecución ─────────────────────────────────────────────────
    /**
     * Imprime un paso de ejecución con ícono de flecha.
     *   ➤ [PASO] Ejecutando paso 1
     */
    public static void paso(String mensaje) {
        System.out.println(AZUL + "  ➤ [PASO]  " + RESET + mensaje);
    }

    // ─── 4. ÉXITO ─────────────────────────────────────────────────────────────
    /**
     * Imprime un mensaje de éxito en verde.
     *   ✔ [OK]   Elemento encontrado
     */
    public static void exito(String mensaje) {
        System.out.println(VERDE + "  ✔ [OK]    " + RESET + mensaje);
    }

    // ─── 5. ERROR ─────────────────────────────────────────────────────────────
    /**
     * Imprime un mensaje de error en rojo.
     *   ✘ [ERROR] Falló la validación
     */
    public static void error(String mensaje) {
        System.out.println(ROJO + "  ✘ [ERROR] " + RESET + mensaje);
    }

    // ─── 6. ADVERTENCIA ───────────────────────────────────────────────────────
    /**
     * Imprime un mensaje de advertencia en amarillo.
     *   ⚠ [WARN]  Reintentando...
     */
    public static void advertencia(String mensaje) {
        System.out.println(AMARILLO + "  ⚠ [WARN]  " + RESET + mensaje);
    }

    // ─── 7. INFO ──────────────────────────────────────────────────────────────
    /**
     * Imprime un mensaje informativo.
     *   ℹ [INFO]  URL: https://...
     */
    public static void info(String mensaje) {
        System.out.println(BLANCO + "  ℹ [INFO]  " + RESET + mensaje);
    }

    // ─── 8. SEPARADOR ─────────────────────────────────────────────────────────
    /**
     * Imprime una línea separadora simple.
     *   ──────────────────────────────────────────────────
     */
    public static void separador() {
        System.out.println(CYAN + "  " + repetir("─", ANCHO) + RESET);
    }

    // ─── 9. TABLA clave → valor ───────────────────────────────────────────────
    /**
     * Imprime la cabecera de una tabla clave/valor.
     *   ┌────────────────────┬──────────────────────┐
     *   │ Clave              │ Valor                │
     *   ├────────────────────┼──────────────────────┤
     */
    public static void cabeceraTablaClave() {
        System.out.println(CYAN + "  ┌" + repetir("─", 20) + "┬" + repetir("─", 22) + "┐" + RESET);
        System.out.println(CYAN + "  │ " + NEGRITA + ajustar("Clave", 18)
                + RESET + CYAN + " │ " + NEGRITA + ajustar("Valor", 20)
                + RESET + CYAN + " │" + RESET);
        System.out.println(CYAN + "  ├" + repetir("─", 20) + "┼" + repetir("─", 22) + "┤" + RESET);
    }

    /**
     * Imprime una fila de tabla clave/valor.
     *   │ Producto           │ PERFUME              │
     */
    public static void tabla(String clave, String valor) {
        System.out.println(CYAN + "  │ " + RESET + ajustar(clave, 18)
                + CYAN + " │ " + RESET + ajustar(valor, 20)
                + CYAN + " │" + RESET);
    }

    /**
     * Imprime el pie de una tabla.
     *   └────────────────────┴──────────────────────┘
     */
    public static void pieTablaClave() {
        System.out.println(CYAN + "  └" + repetir("─", 20) + "┴" + repetir("─", 22) + "┘" + RESET);
    }

    // ─── 10. TIEMPO con marca horaria ─────────────────────────────────────────
    /**
     * Imprime la hora actual con etiqueta en formato HH:mm:dd.
     *   ⏱ [INICIO] → 14:35:17
     */
    public static void tiempo(String etiqueta) {
        String hora = LocalDateTime.now().format(FORMATO_HORA);
        System.out.println(AMARILLO + "  ⏱ [" + etiqueta.toUpperCase() + "] → " + hora + RESET);
    }

    // ─── 11. REINTENTO ────────────────────────────────────────────────────────
    /**
     * Imprime un mensaje de reintento indicando el número actual y máximo.
     *   ↺ [REINTENTO 2/5] → Buscador principal
     */
    public static void reintento(int intento, int maximo, String elemento) {
        System.out.println(AMARILLO + "  ↺ [REINTENTO " + intento + "/" + maximo + "] → " + RESET + elemento);
    }

    // ─── 12. ESCENARIO ────────────────────────────────────────────────────────
    /**
     * Imprime el nombre de un escenario encuadrado.
     *   ┌── ESCENARIO ───────────────────────────────┐
     *   │  Buscar perfume en Mercado Libre            │
     *   └─────────────────────────────────────────────┘
     */
    public static void escenario(String nombre) {
        System.out.println(NEGRITA + AZUL + "  ┌── ESCENARIO " + repetir("─", ANCHO - 13) + "┐" + RESET);
        System.out.println(NEGRITA + AZUL + "  │  " + RESET + ajustar(nombre, ANCHO - 2) + AZUL + " │" + RESET);
        System.out.println(NEGRITA + AZUL + "  └" + repetir("─", ANCHO) + "┘" + RESET);
    }

    // ─── Métodos auxiliares privados ──────────────────────────────────────────

    private static String repetir(String caracter, int veces) {
        return caracter.repeat(Math.max(0, veces));
    }

    private static String centrar(String texto, int ancho) {
        if (texto.length() >= ancho) return texto;
        int padding = (ancho - texto.length()) / 2;
        return " ".repeat(padding) + texto + " ".repeat(ancho - texto.length() - padding);
    }

    private static String ajustar(String texto, int ancho) {
        if (texto.length() >= ancho) return texto.substring(0, ancho);
        return texto + " ".repeat(ancho - texto.length());
    }
}

