package page;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;

public class Login extends PageObject {

    public static final Target CAMPO_CORREO = Target.the("Campo para ingresar el correo del usuario").located(By.xpath("//input[@id='correo' and @type='email']"));
    public static final Target CAMPO_CONTRASENA = Target.the("Campo para ingresar la contraseña del usuario").located(By.xpath("//input[@id='password' and @type='password']"));
    public static final Target BOTON_INGRESAR = Target.the("Boton para ingresar a la cuenta").located(By.xpath("//*[@type='submit']"));

}
