package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import utils.Config;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class SintoniaEscolhaDataPage extends AbstractPage {

    @FindBy(name = "dataDe")
    private WebElement dataDe;

    @FindBy(name = "dataAte")
    private WebElement dataAte;

    @FindBy(className = "btPesquisaUser")
    private WebElement botaoPesquisar;

    @FindBy(css = "a[href$='metodo=verificarAtividadesUsuario']")
    private WebElement botaoDownloadCsv;

    public SintoniaEscolhaDataPage() {
        config = Config.getInstance();
        PageFactory.initElements(config.driver, this);
    }

    public void downloadCsv() {
        config.driver.get("http://10.200.35.6/sintoniaGR/jsp/cliente/acompanharAtividades/acompanharHoras.jsp");
        config.wait.until(ExpectedConditions.visibilityOf(dataDe));

        if (config.quantidadeDias != null) {
            if (config.quantidadeDias > 0) {
                config.quantidadeDias = config.quantidadeDias * -1;
            }

            Calendar instance = Calendar.getInstance();
            instance.add(Calendar.DAY_OF_YEAR, config.quantidadeDias);

            String dataInicial = (new SimpleDateFormat("ddMMyyyy")).format(instance.getTime());
            preecherCampo(dataDe, dataInicial);

            String dataFinal = (new SimpleDateFormat("ddMMyyyy")).format(Calendar.getInstance().getTime());
            preecherCampo(dataAte, dataFinal);

            botaoPesquisar.click();

            config.wait.until(ExpectedConditions.visibilityOf(botaoDownloadCsv));
        }

        botaoDownloadCsv.click();
        config.driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }
}
