package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import utils.Config;

public class SintoniaLoginPage extends AbstractPage {

    @FindBy(name = "usuario.login")
    private WebElement campoLogin;
    @FindBy(name = "usuario.senha")
    private WebElement campoSenha;
    @FindBy(name = "btoOk")
    private WebElement botaoOk;
    @FindBy(id = "mnSintonia")
    private WebElement menuSintonia;


    public SintoniaLoginPage() {
        config = Config.getInstance();
        PageFactory.initElements(config.driver, this);
    }

    public void logar() {
        irPara("http://10.200.35.6/sintoniaGR/");
        preecherCampo(this.campoLogin, config.usuarioSintonia);
        preecherCampo(this.campoSenha, config.senhaSintonia);
        this.botaoOk.click();
        this.config.wait.until(ExpectedConditions.visibilityOf(menuSintonia));
    }

}
