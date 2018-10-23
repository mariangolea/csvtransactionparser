package test.com.mariangolea.fintracker.banks.csvparser.ui;

import com.mariangolea.fintracker.banks.csvparser.BankCSVReportsCmdParser;
import com.mariangolea.fintracker.banks.csvparser.api.Bank;
import com.mariangolea.fintracker.banks.csvparser.api.transaction.response.CsvFileParseResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;
import static org.junit.Assert.assertTrue;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import test.com.mariangolea.fintracker.banks.csvparser.TestUtilities;

public class BankCsvReportsCmdParserTest {

    private final TestUtilities utils = new TestUtilities();

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void basicTest() {
        try {
            File tempFolder = folder.newFolder("test");
            assertTrue(tempFolder.exists());
            File ingFile = new File(tempFolder, "ing.csv");
            ingFile.createNewFile();
            assertTrue(ingFile.exists());
            File btFile = new File(tempFolder, "bt.csv");
            btFile.createNewFile();
            assertTrue(btFile.exists());
            utils.writeCSVFile(Bank.ING, ingFile, utils.constructMockCSVContentForBank(Bank.ING));
            utils.writeCSVFile(Bank.BT, btFile, utils.constructMockCSVContentForBank(Bank.BT));
            String[] correctArgs = {"-folder=" + tempFolder.getAbsolutePath()};
            BankCSVReportsCmdParser parser = new BankCSVReportsCmdParser();
            List<CsvFileParseResponse> responses = parser.parseInput(correctArgs);
            assertTrue(responses != null && responses.size() == 2);
            //further tests for data integrity are made on other test classes.
            
            BankCSVReportsCmdParser.main(correctArgs);
            
        } catch (IOException ex) {
            assertTrue(false);
        }
    }
}
