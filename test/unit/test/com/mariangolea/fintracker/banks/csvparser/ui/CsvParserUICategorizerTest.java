package test.com.mariangolea.fintracker.banks.csvparser.ui;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.mariangolea.fintracker.banks.csvparser.api.parser.Bank;
import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransaction;
import com.mariangolea.fintracker.banks.csvparser.api.parser.CsvFileParseResponse;
import com.mariangolea.fintracker.banks.csvparser.impl.parsers.BankTransactionsParser;
import com.mariangolea.fintracker.banks.csvparser.impl.parsers.bancatransilvania.BTParser;
import com.mariangolea.fintracker.banks.csvparser.impl.preferences.UserPreferencesHandlerFactory;
import com.mariangolea.fintracker.banks.csvparser.impl.ui.CsvParserUI;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import static org.junit.Assert.assertTrue;

import test.com.mariangolea.fintracker.banks.csvparser.TestUtilities;
import test.com.mariangolea.fintracker.banks.csvparser.UserPreferencesTestFactory;

public class CsvParserUICategorizerTest extends FXUITest {

    private final TestUtilities utils = new TestUtilities();

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    private final UserPreferencesTestFactory factory = new UserPreferencesTestFactory();
    @Test

    public void testSimpleDataBT() throws IOException {
        if (!fxInitialized) {
            assertTrue("Useless in headless mode", true);
            return;
        }

        Platform.setImplicitExit(false);
        File mockCSV = utils.writeCSVFile(folder.newFile("testUI.csv"), utils.constructMockCSVContentForBT());
        assertTrue(mockCSV != null);
        CsvFileParseResponse response = new BankTransactionsParser().parseTransactions(mockCSV);
        // tests in other files ensure response integrity, no need to do that in here.
        LocalUI local = new LocalUI();
        local.createTableView();
        local.createUncategorizedView();
        local.loadData(Arrays.asList(response));
        assertTrue(local.getModel() != null);

        MenuBar menu = local.createMenu();
        assertTrue(menu != null);

        ScrollPane feedbackScroll = local.createFeedbackView();
        assertTrue(feedbackScroll != null);

        final CountDownLatch latch = new CountDownLatch(6);
        Platform.runLater(() -> {
            local.getFeedbackPane().getChildren().addListener(new LocalListChangeListener(latch));
        });
        int initialParsedCSVFiles = local.getParsedCsvFiles().size();
        final File mockCSV2 = utils.writeCSVFile(folder.newFile("test.csv"), utils.constructMockCSVContentForBT());
        Platform.runLater(() -> {
            local.parseUserSelectedCSVFiles(Arrays.asList(mockCSV2));
        });
        try {
            latch.await();
        } catch (InterruptedException ex) {
            Logger.getLogger(CsvParserUICategorizerTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        //test parsed the same file content twice, so double expected values.
        assertTrue(local.getModel() != null && local.getModel().size() == 6);

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
        CsvFileParseResponse res = new CsvFileParseResponse(new BTParser(), 1, 1, new File("mock"), Collections.EMPTY_LIST, Collections.EMPTY_LIST);
        local.appendReportText(res);
        assertTrue(local.getFeedbackPane().getChildren().size() == 4);
        local.getFeedbackPane().getChildren().clear();
        res = new CsvFileParseResponse(new BTParser(), 1, 1, new File("mock"), Collections.EMPTY_LIST, Arrays.asList("ddd"));
        local.appendReportText(res);
        assertTrue(local.getFeedbackPane().getChildren().size() == 4);
        local.getFeedbackPane().getChildren().clear();
        res = new CsvFileParseResponse(new BTParser(), 0, 1, new File("mock"), Collections.EMPTY_LIST, Arrays.asList("ddd"));
        local.appendReportText(res);
        assertTrue(local.getFeedbackPane().getChildren().size() == 4);
        local.getFeedbackPane().getChildren().clear();
        res = new CsvFileParseResponse(new BTParser(), 1, 0, new File("mock"), Collections.EMPTY_LIST, Arrays.asList("ddd"));
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

        public LocalUI() {
            super(factory);
        }

        @Override
        protected void loadData(List<CsvFileParseResponse> parsedTransactions) {
            super.loadData(parsedTransactions);
        }

        @Override
        public MenuBar createMenu() {
            return super.createMenu();
        }

        @Override
        protected ScrollPane createFeedbackView() {
            return super.createFeedbackView();
        }

        @Override
        protected void createTableView() {
            super.createTableView();
        }

        public TextFlow getFeedbackPane() {
            return feedbackPane;
        }

        @Override
        protected void parseUserSelectedCSVFiles(List<File> csvFiles) {
            super.parseUserSelectedCSVFiles(csvFiles);
        }

        public Collection<BankTransaction> getModel() {
            return model;
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
            super.appendReportText(fileResponse);
        }

        @Override
        protected boolean appendReportMessage(String message) {
            return super.appendReportMessage(message);
        }

        @Override
        protected boolean appendHyperlinkToFile(String pre, File sourceFile, String post) {
            return super.appendHyperlinkToFile(pre, sourceFile, post);
        }

        @Override
        protected void createUncategorizedView() {
            super.createUncategorizedView();
        }
    }
}
