package test.com.mariangolea.fintracker.banks.csvparser.ui;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.mariangolea.fintracker.banks.csvparser.api.Bank;
import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransactionAbstractGroup;
import com.mariangolea.fintracker.banks.csvparser.api.transaction.response.CsvFileParseResponse;
import com.mariangolea.fintracker.banks.csvparser.parsers.BankCSVTransactionParser;
import com.mariangolea.fintracker.banks.csvparser.ui.CsvParserUI;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.TextFlow;
import static org.junit.Assert.assertTrue;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import test.com.mariangolea.fintracker.banks.csvparser.TestUtilities;

/**
 *
 * @author Marian Golea <mariangolea@gmail.com>
 */
@RunWith(MockitoJUnitRunner.class)
public class CsvParserUICategorizerTest extends FXUITest {

    private final TestUtilities utils = new TestUtilities();

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private final LocalUI local = new LocalUI();

    @Test
    public void testSimpleDataBT() throws IOException {
        if (!fxInitialized) {
            assertTrue("Useless in headless mode", true);
        }

        Platform.setImplicitExit(false);
        File mockCSV = utils.writeCSVFile(Bank.BT, folder.newFile("testUI.csv"), utils.constructMockCSVContentForBank(Bank.BT));
        assertTrue(mockCSV != null);
        CsvFileParseResponse response = new BankCSVTransactionParser().parseTransactions(mockCSV);
        // tests in other files ensure response integrity, no need to do that in here.
        local.loadData(Arrays.asList(response));
        assertTrue(local.getInModel() != null);
        assertTrue(local.getOutModel() != null);

        MenuBar menu = local.createMenu();
        assertTrue(menu != null);

        ScrollPane feedbackScroll = local.createFeedbackView();
        assertTrue(feedbackScroll != null);

        final CountDownLatch latch = new CountDownLatch(6);
        Platform.runLater(() -> {
            local.getFeedbackPane().getChildren().addListener(new LocalListChangeListener(latch));
        });
        int initialParsedCSVFiles = local.getParsedCsvFiles().size();
        final File mockCSV2 = utils.writeCSVFile(Bank.ING, folder.newFile("test.csv"), utils.constructMockCSVContentForBank(Bank.ING));
        Platform.runLater(() -> {
            local.parseUserSelectedCSVFiles(Arrays.asList(mockCSV2));
        });
        try {
            latch.await();
        } catch (InterruptedException ex) {
            Logger.getLogger(CsvParserUICategorizerTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        //test parsed the same file content twice, so double expected values.
        assertTrue(local.getInModel() != null && local.getInModel().size() == 3);
        assertTrue(local.getOutModel() != null && local.getOutModel().size() == 3);

        assertTrue(local.getParsedCsvFiles().size() == initialParsedCSVFiles + 1);
    }

    private class LocalListChangeListener implements ListChangeListener<Node> {

        private final CountDownLatch latch;

        public LocalListChangeListener(final CountDownLatch latch) {
            this.latch = latch;
        }

        @Override
        public void onChanged(Change<? extends Node> c) {
            c.next();
            assertTrue(c.wasAdded() && c.getAddedSize() == 1 && c.getAddedSubList().size() == 1);
            Node addedNode = c.getAddedSubList().get(0);
            assertTrue(addedNode != null);
            latch.countDown();
        }
    }

    /**
    Allows accessing several protected fields and methods.
     */
    private class LocalUI extends CsvParserUI {

        @Override
        protected void loadData(List<CsvFileParseResponse> parsedTransactions) {
            super.loadData(parsedTransactions); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public MenuBar createMenu() {
            return super.createMenu(); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        protected ScrollPane createFeedbackView() {
            return super.createFeedbackView(); //To change body of generated methods, choose Tools | Templates.
        }

        public TextFlow getFeedbackPane() {
            return feedbackPane;
        }

        @Override
        protected void parseUserSelectedCSVFiles(List<File> csvFiles) {
            super.parseUserSelectedCSVFiles(csvFiles); //To change body of generated methods, choose Tools | Templates.
        }

        public ObservableList<BankTransactionAbstractGroup> getInModel() {
            return inModel;
        }

        public ObservableList<BankTransactionAbstractGroup> getOutModel() {
            return outModel;
        }

        public List<File> getParsedCsvFiles() {
            return parsedCsvFiles;
        }
    }
}
