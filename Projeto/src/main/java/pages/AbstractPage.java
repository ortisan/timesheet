package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import utils.Config;

import java.util.concurrent.TimeUnit;

public abstract class AbstractPage {

    protected Config config;

    public AbstractPage() {
        config = Config.getInstance();
    }

    protected void irPara(String url) {
        config.driver.get(url);
        config.driver.manage().timeouts().implicitlyWait(5L, TimeUnit.SECONDS);
    }

    protected void focar(WebElement element) {
        if ("input".equals(element.getTagName())) {
            element.click();
            element.sendKeys("");
        } else {
            new Actions(config.driver).moveToElement(element).perform();
        }
    }

    protected void preecherCampo(WebElement element, String conteudo) {
        if (element.getTagName().equals("select")) {
            Select select = new Select(element);
            select.selectByVisibleText(conteudo);
        } else {
            element.click();
            element.clear();
            for (int i = 0; i < conteudo.length(); i++) {
                element.sendKeys(new CharSequence[]{"" + conteudo.charAt(i)});
                config.driver.manage().timeouts().implicitlyWait(100, TimeUnit.MILLISECONDS);
            }
        }
    }

    protected void clicarAlert() {
        // Espera até um alerta estar visível
        config.wait.until(ExpectedConditions.alertIsPresent());
        config.driver.switchTo().alert().accept();
        config.driver.switchTo().defaultContent();
    }
}
