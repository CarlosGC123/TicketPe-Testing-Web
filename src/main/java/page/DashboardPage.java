package page;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;

public class DashboardPage extends PageObject {

    public static final Target CAMPO_BUSQUEDA = Target.the("Campo para buscar un video en Youtube").located(By.xpath("//input[@placeholder]"));
    public static final Target BOTON_INGRESAR = Target.the("Boton para ingresar a Login").located(By.xpath("//button[text()='Ingresar']"));
    public static final Target CUENTA_ACCESO = Target.the("Elemento de Login accedido").located(By.xpath("//*[@class='link-cuenta']"));
    public static final Target OPCION_TORNEO_AJEDREZ = Target.the("Opcion Torneo Ajedrez").located(By.xpath("//a[.//div[@class='tarjeta-imagen']]//*[contains(text(),'Torneo de Ajedrez')]"));

}
