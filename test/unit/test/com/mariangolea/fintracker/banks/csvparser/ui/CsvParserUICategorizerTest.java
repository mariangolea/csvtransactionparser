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
import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransactionCompanyGroup;
import com.mariangolea.fintracker.banks.csvparser.api.transaction.response.CsvFileParseResponse;
import com.mariangolea.fintracker.banks.csvparser.parsers.BankCSVParserFactory;
import com.mariangolea.fintracker.banks.csvparser.parsers.BankCSVTransactionParser;
import com.mariangolea.fintracker.banks.csvparser.ui.CsvParserUI;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import static org.junit.Assert.assertTrue;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import test.com.mariangolea.fintracker.banks.csvparser.TestUtilities;

@RunWith(MockitoJUnitRunner.class)
public class CsvParserUICategorizerTest extends FXUITest {

    private final TestUtilities utils = new TestUtilities();

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void testSimpleDataBT() throws IOException {
        if (!fxInitialized) {
            assertTrue("Useless in headless mode", true);
            return;
        }

        Platform.setImplicitExit(false);
        File mockCSV = utils.writeCSVFile(Bank.BT, folder.newFile("testUI.csv"), utils.constructMockCSVContentForBank(Bank.BT));
        assertTrue(mockCSV != null);
        CsvFileParseResponse response = new BankCSVTransactionParser().parseTransactions(mockCSV);
        // tests in other files ensure response integrity, no need to do that in here.
        LocalUI local = new LocalUI();
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

    @Test
    public void testAppendMessages() {
        if (!fxInitialized) {
            assertTrue("Useless in headless mode", true);
            return;
        }

        boolean result;
        LocalUI local = new LocalUI();
        local.createFeedbackView();
        try {
            result = local.appendHyperlinkToFile("a", null, "b");
            assertTrue(!result);
            result = local.appendHyperlinkToFile("a", folder.newFile(), "b");
            assertTrue(result);
            assertTrue(local.getFeedbackPane().getChildren().size() == 3);
        } catch (IOException ex) {
            Logger.getLogger(CsvParserUICategorizerTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        assertTrue(!local.appendReportMessage(null));
        assertTrue(local.appendReportMessage(""));

        local.getFeedbackPane().getChildren().clear();
        CsvFileParseResponse res = new CsvFileParseResponse(BankCSVParserFactory.getInstance(Bank.BT), 1, 1, new File("mock"), Collections.EMPTY_LIST, Collections.EMPTY_LIST);
        local.appendReportText(res);
        assertTrue(local.getFeedbackPane().getChildren().size() == 4);
        local.getFeedbackPane().getChildren().clear();
        res = new CsvFileParseResponse(BankCSVParserFactory.getInstance(Bank.BT), 1, 1, new File("mock"), Collections.EMPTY_LIST, Arrays.asList("ddd"));
        local.appendReportText(res);
        assertTrue(local.getFeedbackPane().getChildren().size() == 4);
        local.getFeedbackPane().getChildren().clear();
        res = new CsvFileParseResponse(BankCSVParserFactory.getInstance(Bank.BT), 0, 1, new File("mock"), Collections.EMPTY_LIST, Arrays.asList("ddd"));
        local.appendReportText(res);
        assertTrue(local.getFeedbackPane().getChildren().size() == 4);
        local.getFeedbackPane().getChildren().clear();
        res = new CsvFileParseResponse(BankCSVParserFactory.getInstance(Bank.BT), 1, 0, new File("mock"), Collections.EMPTY_LIST, Arrays.asList("ddd"));
        local.appendReportText(res);
        assertTrue(local.getFeedbackPane().getChildren().size() == 4);
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

        public ObservableList<BankTransactionCompanyGroup> getInModel() {
            return inModel;
        }

        public ObservableList<BankTransactionCompanyGroup> getOutModel() {
            return outModel;
        }

        public List<File> getParsedCsvFiles() {
            return parsedCsvFiles;
        }

        @Override
        public void start(Stage primaryStage) throws Exception {
            super.start(primaryStage);
        }

        @Override
        protected void appendReportText(CsvFileParseResponse fileResponse) {
            super.appendReportText(fileResponse); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        protected boolean appendReportMessage(String message) {
            return super.appendReportMessage(message); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        protected boolean appendHyperlinkToFile(String pre, File sourceFile, String post) {
            return super.appendHyperlinkToFile(pre, sourceFile, post); //To change body of generated methods, choose Tools | Templates.
        }

    }
}
