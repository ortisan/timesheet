import pages.MagnaLoginPage;
import pages.MagnaPreenchimentoDadosPage;
import pages.SintoniaEscolhaDataPage;
import pages.SintoniaLoginPage;

public class TimesheetRobot {

    public static void main(String[] args) throws Exception {
        new SintoniaLoginPage().logar();
        try {
            new SintoniaEscolhaDataPage().downloadCsvPostar();
        } catch (Exception exc) {
        }
        try {
            new SintoniaEscolhaDataPage().downloadCsvRegistrar();
        } catch (Exception exc) {
        }
        new MagnaLoginPage().logar();
        new MagnaPreenchimentoDadosPage().inputarDados();
    }
}
