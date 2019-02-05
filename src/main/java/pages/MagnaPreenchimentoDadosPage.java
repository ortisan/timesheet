package pages;

import domain.Atividade;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import utils.Config;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class MagnaPreenchimentoDadosPage extends AbstractPage {

    private static Logger logger = Logger.getLogger(MagnaPreenchimentoDadosPage.class);

    private static final long SEIS_HORAS_MILIS = 21600000L;
    private static final int CUSTOM_FOLDER = 2;
    private static final String SPLIT_CHARS = "#\\|\\#";

    @FindBy(name = "data")
    private WebElement campoData;

    @FindBy(name = "flag_cliente")
    private WebElement campoFlagCliente;

    @FindBy(name = "Cliente")
    private WebElement campoCliente;

    @FindBy(name = "Cliente_Projetos")
    private WebElement campoClienteProjetos;

    @FindBy(name = "Projeto")
    private WebElement campoProjeto;

    @FindBy(name = "atividade")
    private WebElement campoAtividade;

    @FindBy(name = "OS")
    private WebElement campoOS;

    @FindBy(name = "manhainicio")
    private WebElement campoManhaInicio;

    @FindBy(name = "manhafim")
    private WebElement campoManhaFim;

    @FindBy(name = "horas")
    private WebElement campoHoras;

    @FindBy(name = "atividades")
    private WebElement campoAtividades;

    @FindBy(id = "btIncluir")
    private WebElement btIncluir;


    public MagnaPreenchimentoDadosPage() {
        config = Config.getInstance();
        PageFactory.initElements(config.driver, this);
    }

    public void inputarDados() {

        File file = this.getLastModifiedCsv();
        BufferedReader bufferCsv = null;
        Atividade ultimaAtividade = null;
        HashSet setAtividades = new HashSet();

        try {
            Pattern pattern = Pattern.compile("^[0-9]{1,2}/\\w{2,4}/[0-9]{1,2}");
            bufferCsv = new BufferedReader(new InputStreamReader(new FileInputStream(file), "ISO-8859-1"));
            String linhaArquivo = "";
            String ultimaLinha = "";

            ArrayList valores;
            Atividade atividade;
            while ((linhaArquivo = bufferCsv.readLine()) != null) {
                if (pattern.matcher(linhaArquivo).find()) {
                    if (ultimaLinha.length() > 0) {
                        ultimaLinha.replaceAll("\"", "");
                        valores = splitLinha(ultimaLinha);
                        atividade = obterAtividade(valores, (Atividade) ultimaAtividade);
                        setAtividades.add(atividade);
                    }
                    ultimaLinha = linhaArquivo;
                } else {
                    ultimaLinha = ultimaLinha + " " + linhaArquivo;
                }
            }
            valores = splitLinha(ultimaLinha);
            atividade = obterAtividade(valores, (Atividade) ultimaAtividade);
            setAtividades.add(atividade);
        } catch (IOException exc) {
            logger.error("Erro ao ler o arquivo .csv.", exc);
        } finally {
            try {
                if (bufferCsv != null) {
                    bufferCsv.close();
                }
            } catch (IOException var19) {
                logger.error("Erro ao fechar .csv.", var19);
            }
        }

        List<Atividade> listAtividades = new ArrayList();
        Iterator iteratorAtividades = setAtividades.iterator();

        while (iteratorAtividades.hasNext()) {
            Atividade atividade = (Atividade) iteratorAtividades.next();
            listAtividades.add(atividade);
            if (atividade.getDataFim() != null) {
                long diffMilli = SEIS_HORAS_MILIS - atividade.getDiffDataInicialFinal();
                if (diffMilli < 0L) {
                    Calendar dataFim = atividade.getDataFim();
                    Calendar dataFimAtividade2 = (Calendar) dataFim.clone();
                    dataFim.add(14, (int) diffMilli);
                    atividade.setDataFim(dataFim);
                    Atividade atividade2 = new Atividade();
                    atividade2.setDataInicio(dataFim);
                    atividade2.setDataFim(dataFimAtividade2);
                    atividade2.setDescricao(atividade.getDescricao());
                    listAtividades.add(atividade2);
                }
            }
        }

        Collections.sort(listAtividades, (Atividade ativ1, Atividade ativ2) -> ativ1.getDataInicio().compareTo(ativ2.getDataInicio()));
        Iterator atividadeIterator = listAtividades.iterator();

        while (atividadeIterator.hasNext()) {
            Atividade atividade = (Atividade) atividadeIterator.next();
            if (atividade.getDataFim() != null) {
                try {
                    this.inserirAtividade(atividade);
                } catch (Exception exc) {
                    logger.error(exc);
                }
            }
        }

        Config.getInstance().driver.quit();
    }

    private File getLastModifiedCsv() {
        Collection<File> files = FileUtils.listFiles(new File("./"), new String[]{"csv"}, false);
        if (files.size() == 0) {
            throw new IllegalStateException("Não foi encontrado nenhum arquivo .csv");
        } else {
            List<File> filesSorted = new ArrayList(files);
            Collections.sort(filesSorted, (File f1, File f2) -> (int) (f2.lastModified() - f1.lastModified()));
            return (File) filesSorted.get(0);
        }
    }

    private static Atividade obterAtividade(List<String> colunas, Atividade ultimaAtividade) {
        Atividade atividade = null;
        int size = colunas.size();
        if (size > 1) {
            atividade = new Atividade();
            String dataStr = (String) colunas.get(0);

            try {
                Date date = (new SimpleDateFormat("dd/MMM/yy", Locale.ENGLISH)).parse(dataStr);
                Calendar dataInicial = Calendar.getInstance();
                dataInicial.setTime(date);
                obterHoraMinuto(dataInicial, (String) colunas.get(2));
                atividade.setDataInicio(dataInicial);

                try {
                    Calendar dataFinal = (Calendar) dataInicial.clone();
                    obterHoraMinuto(dataFinal, (String) colunas.get(3));
                    atividade.setDataFim(dataFinal);
                } catch (Exception var8) {
                    logger.error("Erro ao parsear a hora final.", var8);
                }
            } catch (ParseException var9) {
                logger.error("Erro ao parsear as datas.", var9);
            }

            int idxDescricao = 10;
            if (size == 12) {
                idxDescricao = 11;
            }

            atividade.setDescricao(((String) colunas.get(idxDescricao)).trim());
        } else {
            atividade = ultimaAtividade;
            atividade.addDescricao((String) colunas.get(0));
        }

        return atividade;
    }

    private static void obterHoraMinuto(Calendar calendar, String horario) {
        String[] split = horario.split(":");
        Integer hora = Integer.parseInt(split[0], 10);
        Integer minuto = Integer.parseInt(split[1], 10);
        calendar.set(Calendar.HOUR_OF_DAY, hora);
        calendar.set(Calendar.MINUTE, minuto);
    }

    private boolean isAlertPresent() {
        try {
            config.driver.switchTo().alert();
            return true;
        } catch (Exception exc) {
            logger.error(exc);
            return false;
        }
    }

    private void inserirAtividade(Atividade atividade) {
        irPara(config.urlMagnaHome);

        if (atividade.getDataFim() == null) {
            return;
        }

        ((JavascriptExecutor) config.driver).executeScript("arguments[0].setAttribute('value', '" + atividade.getData() + "')", new Object[]{campoData});
        if (!campoFlagCliente.isSelected()) {
            campoFlagCliente.click();
            this.config.wait.until(ExpectedConditions.visibilityOf(campoClienteProjetos));
        }

        preecherCampo(campoCliente, config.clienteMagna);
        config.driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        preecherCampo(campoClienteProjetos, config.clienteMagna);
        config.driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);

        this.config.wait.until(ExpectedConditions.visibilityOf(campoProjeto));

        preecherCampo(campoProjeto, config.projetoMagna);
        this.config.wait.until(ExpectedConditions.visibilityOf(campoOS));

        if (StringUtils.isNotEmpty(config.contratoMagna)) {
            preecherCampo(campoOS, config.contratoMagna);
            this.config.wait.until(ExpectedConditions.visibilityOf(campoAtividade));
        }

        preecherCampo(campoAtividade, config.atividadeMagna);
        this.config.wait.until(ExpectedConditions.visibilityOf(campoManhaInicio));

        String horarioInicial = atividade.getHorarioInicial();
        preecherCampo(campoManhaInicio, horarioInicial);
        this.config.wait.until(ExpectedConditions.visibilityOf(campoManhaFim));

        String horarioFinal = atividade.getHorarioFinal();
        preecherCampo(campoManhaFim, horarioFinal);
        this.config.wait.until(ExpectedConditions.visibilityOf(campoHoras));

        ((JavascriptExecutor) config.driver).executeScript("CalculaHora()");
        this.config.wait.until(ExpectedConditions.visibilityOf(campoAtividades));

        String descricao = StringUtils.defaultString(config.descricaoAtividadeMagna, atividade.getDescricao());
        preecherCampo(campoAtividades, descricao);
        this.config.wait.until(ExpectedConditions.visibilityOf(btIncluir));

        btIncluir.click();
        clicarAlert();
    }

    public static ArrayList<String> splitLinha(String linha) {
        ArrayList<String> valoresColunas = new ArrayList();
        if (linha != null) {
            linha = linha.replaceAll(SPLIT_CHARS, SPLIT_CHARS);
            String[] splitData = linha.split(SPLIT_CHARS);

            for (int i = 0; i < splitData.length; ++i) {
                if (splitData[i] != null) {
                    valoresColunas.add(splitData[i].trim());
                }
            }
        }

        return valoresColunas;
    }
}




