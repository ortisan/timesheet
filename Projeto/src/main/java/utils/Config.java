package utils;

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
    public String importarSintonia;
    public String usuarioSintonia;
    public String senhaSintonia;
    public String horasPostadas;
    public Integer quantidadeDias;
    public String usuario;
    public String senha;
    public String cliente;
    public String projeto;
    public String atividade;
    public String contrato;
    public String descricaoAtividade;
    public WebDriver driver;
    public WebDriverWait wait;

    private Config() {
        try {
            this.properties.load(new FileInputStream(new File("config.properties")));
        } catch (IOException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
        this.importarSintonia = this.properties.getProperty("sintonia.importar");
        this.usuarioSintonia = this.properties.getProperty("sintonia.usuario");
        this.senhaSintonia = this.properties.getProperty("sintonia.senha");
        this.horasPostadas = this.properties.getProperty("sintonia.horaspostadas");

        try {
            this.quantidadeDias = Integer.parseInt(this.properties.getProperty("sintonia.diasImportar"));
        } catch (Exception var3) {
            logger.warn("Não foi encontrado a propriedade sintonia.diasImportar");
        }

        this.usuario = this.properties.getProperty("usuario");
        this.senha = this.properties.getProperty("senha");
        this.cliente = this.properties.getProperty("cliente");
        this.projeto = this.properties.getProperty("projeto");
        this.atividade = this.properties.getProperty("atividade");
        this.contrato = this.properties.getProperty("contrato");
        this.descricaoAtividade = this.properties.getProperty("descricaoAtividade");


        FirefoxOptions profile = new FirefoxOptions();
        profile.addPreference("browser.download.folderList", 2);
        profile.addPreference("browser.download.manager.showWhenStarting", false);
        File file = new File("./");
        profile.addPreference("browser.download.dir", file.getAbsolutePath());
        profile.addPreference("browser.helperApps.neverAsk.saveToDisk", "text/csv");
        profile.addPreference("intl.accept_languages", "pt-br");
        this.driver = new FirefoxDriver(profile);
        this.wait = new WebDriverWait(driver, 120);
    }

    public static Config getInstance() {
        if (instance == null) {
            instance = new Config();
        }
        return instance;
    }
}
