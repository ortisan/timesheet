package utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {

    static Logger logger = Logger.getLogger(Config.class);

    private static Config instance;

    private Properties properties = new Properties();

    public String usuarioSintonia;
    public String senhaSintonia;
    //    public String horasPostadas;
    public Integer quantidadeDias;
    public String urlMagnaHome;
    public String usuarioMagna;
    public String senhaMagna;
    public String clienteMagna;
    public String projetoMagna;
    public String atividadeMagna;
    public String contratoMagna;
    public String descricaoAtividadeMagna;
    public String urlSintoniaHome;
    public String urlSintoniaPostarHoras;
    public String urlSintoniaRegistrarHoras;
    public WebDriver driver;
    public WebDriverWait wait;

    private Config() {
        try {
            this.properties.load(new FileInputStream(new File("config.properties")));
        } catch (IOException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }

        this.usuarioSintonia = this.properties.getProperty("sintonia.usuario");
        this.senhaSintonia = this.properties.getProperty("sintonia.senha");

        try {
            this.quantidadeDias = Integer.parseInt(this.properties.getProperty("sintonia.diasImportar"));
        } catch (Exception var3) {
            logger.warn("Não foi encontrado a propriedade sintonia.diasImportar");
        }

        this.urlSintoniaHome = this.properties.getProperty("sintonia.urlHome");
        this.urlSintoniaPostarHoras = this.properties.getProperty("sintonia.urlPostarHoras");
        this.urlSintoniaRegistrarHoras = this.properties.getProperty("sintonia.urlRegistrarHoras");

        this.urlMagnaHome = this.properties.getProperty("magna.urlHome");
        this.usuarioMagna = this.properties.getProperty("magna.usuario");
        this.senhaMagna = this.properties.getProperty("magna.senha");
        this.clienteMagna = this.properties.getProperty("magna.cliente");
        this.projetoMagna = this.properties.getProperty("magna.projeto");
        this.atividadeMagna = this.properties.getProperty("magna.atividade");
        this.contratoMagna = this.properties.getProperty("magna.contrato");
        this.descricaoAtividadeMagna = this.properties.getProperty("magna.descricaoAtividade");


        FirefoxOptions profile = new FirefoxOptions();
        profile.addPreference("browser.download.folderList", 2);
        profile.addPreference("browser.download.manager.showWhenStarting", false);
        File file = new File("./");
        profile.addPreference("browser.download.dir", file.getAbsolutePath());
        profile.addPreference("browser.helperApps.neverAsk.saveToDisk", "text/csv");
        profile.addPreference("intl.accept_languages", "pt-br");
        this.driver = new FirefoxDriver(profile);

        String timeoutSeg = StringUtils.defaultString(this.properties.getProperty("timeout_espera_em_seg"), "120");
        this.wait = new WebDriverWait(driver, Integer.parseInt(timeoutSeg));
    }

    public static Config getInstance() {
        if (instance == null) {
            instance = new Config();
        }
        return instance;
    }
}
