package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import utils.Config;

import java.util.concurrent.TimeUnit;

public class MagnaLoginPage extends AbstractPage {

    @FindBy(name = "Username")
    private WebElement campoLogin;
    @FindBy(name = "Password")
    private WebElement campoSenha;
    @FindBy(css = "input[type=submit]")
    private WebElement botaoOk;
    @FindBy(name = "data")
    private WebElement campoData;


    public MagnaLoginPage() {
        config = Config.getInstance();
        PageFactory.initElements(config.driver, this);
    }

    public void logar() {
        irPara(config.urlMagnaHome);
        config.driver.manage().timeouts().implicitlyWait(5L, TimeUnit.SECONDS);
        preecherCampo(this.campoLogin, config.usuarioMagna);
        preecherCampo(this.campoSenha, config.senhaMagna);
        this.botaoOk.click();
        this.config.wait.until(ExpectedConditions.visibilityOf(campoData));
    }
}
