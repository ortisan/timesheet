import pages.MagnaLoginPage;
import pages.MagnaPreenchimentoDadosPage;
import pages.SintoniaEscolhaDataPage;
import pages.SintoniaLoginPage;

public class TimesheetRobot {

    public static void main(String[] args) throws Exception {
        new SintoniaLoginPage().logar();
        new SintoniaEscolhaDataPage().downloadCsv();
        new MagnaLoginPage().logar();
        new MagnaPreenchimentoDadosPage().inputarDados();
    }

}
