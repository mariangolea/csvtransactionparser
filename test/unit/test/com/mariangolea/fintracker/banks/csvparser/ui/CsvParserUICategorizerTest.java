package test.com.mariangolea.fintracker.banks.csvparser.ui;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;


import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.mariangolea.fintracker.banks.csvparser.api.Bank;
import com.mariangolea.fintracker.banks.csvparser.api.transaction.response.CsvFileParseResponse;
import com.mariangolea.fintracker.banks.csvparser.parsers.BankCSVTransactionParser;
import com.mariangolea.fintracker.banks.csvparser.ui.CsvParserUI;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.control.MenuBar;
import javafx.scene.text.Text;

import test.com.mariangolea.fintracker.banks.csvparser.TestUtilities;

/**
 *
 * @author Marian Golea <mariangolea@gmail.com>
 */
public class CsvParserUICategorizerTest extends CsvParserUI {

    private final TestUtilities utils = new TestUtilities();

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void testSimpleDataBT() throws IOException {
        // Initializes the enum values, helps unit test batch cover the momentarily dumb
        // enum.
        File mockCSV = utils.writeCSVFile(Bank.BT, folder.newFile("testUI.csv"), utils.constructMockCSVContentForBank(Bank.BT));
        assertTrue(mockCSV != null);
        CsvFileParseResponse response = new BankCSVTransactionParser().parseTransactions(mockCSV);
        // tests in other files ensure response integrity, no need to do that in here.
        loadData(Arrays.asList(response));
        assertTrue(inModel != null);
        assertTrue(outModel != null);

        MenuBar menu = createMenu();
        assertTrue(menu != null);

        final StopCondition stopCondition = new StopCondition();
        feedbackPane.getChildrenUnmodifiable().addListener(new ListChangeListener<Node>() {
            int tick = 0;

            @Override
            public void onChanged(ListChangeListener.Change<? extends Node> c) {
                assertTrue(c.wasAdded() && c.getAddedSize() == 1 && c.getAddedSubList().size() == 1);
                Node addedNode = c.getAddedSubList().get(0);
                assertTrue(addedNode != null);
                switch (tick) {
                    case 0:
                        assertTrue(addedNode instanceof Text);
                        Text textS = (Text) addedNode;
                        assertTrue(textS.getText().contains(START_PARSE_MESSAGE));
                        break;
                    case 1:
                        assertTrue(addedNode instanceof Text);
                        Text textF = (Text) addedNode;
                        assertTrue(textF.getText().contains(FINISHED_PARSING_CSV_FILES));
                        break;
                    case 5:
                        //2 more ticks expected for a single file.
                        feedbackPane.getChildrenUnmodifiable().removeListener(this);
                        stopCondition.stop = true;
                        break;
                    default:
                        break;
                }

                tick++;
            }
        });
        int initialParsedCSVFiles = parsedCsvFiles.size();
        mockCSV = utils.writeCSVFile(Bank.ING, folder.newFile("test.csv"), utils.constructMockCSVContentForBank(Bank.ING));
        parseUserSelectedCSVFiles(Arrays.asList(mockCSV));
        while (!stopCondition.stop) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException ex) {
                Logger.getLogger(CsvParserUICategorizerTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        //test parsed the same file content twice, so double expected values.
        assertTrue(inModel != null && inModel.size() == 3);
        assertTrue(outModel != null && outModel.size() == 3);

        assertTrue(parsedCsvFiles.size() == initialParsedCSVFiles + 1);
    }

    private class StopCondition {

        private boolean stop = false;
    }
}
