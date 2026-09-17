package page;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;

public class YoutubePage extends PageObject {

    public static final Target CAMPO_BUSQUEDA = Target.the("Campo para buscar un video en Youtube").located(By.xpath("//input[@placeholder='Buscar' and @role='combobox']"));
    public static final Target BOTON_BUSCAR = Target.the("Boton de busqueda de video").located(By.xpath("//button[@aria-label='Search']"));
    public static final Target RESULTADOS_CONTENEDOR_COMPLETO = Target.the("Resultados encontrados en busqueda de video").located(By.xpath("//*[@id='contents' and contains(@class,'style-scope ytd-item-section-renderer')]"));
    public static final Target RESULTADO_PRIMERA_POSICION = Target.the("Primer resultado encontrado en busqueda de video").located(By.xpath("//*[@id='contents' and contains(@class,'style-scope ytd-item-section-renderer')]//ytd-video-renderer"));
}
