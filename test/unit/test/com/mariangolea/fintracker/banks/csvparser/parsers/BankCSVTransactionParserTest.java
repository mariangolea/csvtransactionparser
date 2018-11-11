package test.com.mariangolea.fintracker.banks.csvparser.parsers;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.mariangolea.fintracker.banks.csvparser.api.parser.CsvFileParseResponse;
import com.mariangolea.fintracker.banks.csvparser.impl.parsers.BankTransactionsParser;
import java.util.List;

import test.com.mariangolea.fintracker.banks.csvparser.Utilities;

public class BankCSVTransactionParserTest {

    private final BankTransactionsParser parser = new BankTransactionsParser();
    private final Utilities utils = new Utilities();
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void testBasicRoundTripText() throws IOException {
        CsvFileParseResponse response = parser.parseTransactions(null);
        assertTrue(response == null);
        
        // will be written to and read from a csv file.
        String[] mockLines = {"One", "Two"};
        File csvFile = utils.writeCSVFile(folder.newFile("file.csv"), mockLines);
        assertTrue(csvFile != null);

        List<String> readString = parser.loadCSVFile(csvFile);
        String[] expected = {"One", "Two"};
        assertTrue(areStringArraysEqual(readString.toArray(new String[readString.size()]), expected));
    }

    private boolean areStringArraysEqual(String[] first, String[] second) {
        if (first.length != second.length) {
            return false;
        }

        for (int i = 0; i < first.length; i++) {
            if (!first[i].equals(second[i])) {
                return false;
            }
        }

        return true;
    }
}
