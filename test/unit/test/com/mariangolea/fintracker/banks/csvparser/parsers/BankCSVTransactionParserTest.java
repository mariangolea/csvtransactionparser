package test.com.mariangolea.fintracker.banks.csvparser.parsers;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.mariangolea.fintracker.banks.csvparser.api.Bank;
import com.mariangolea.fintracker.banks.csvparser.parsers.BankCSVTransactionParser;
import java.util.List;

import test.com.mariangolea.fintracker.banks.csvparser.TestUtilities;

/**
 *
 * @author Marian Golea <mariangolea@gmail.com>
 */
public class BankCSVTransactionParserTest {

    private final BankCSVTransactionParser parser = new BankCSVTransactionParser();
    private final TestUtilities utils = new TestUtilities();
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void testBasicRoundTripText() throws IOException {
        // will be written to and read from a csv file.
        String[] mockLines = {"One", "Two"};
        File csvFile = utils.writeCSVFile(Bank.BT, folder.newFile("file.csv"), mockLines);
        assertTrue(csvFile != null);

        List<String> readString = parser.loadCSVFile(csvFile);
        String[] expected = {Bank.BT.relevantContentHeaderLine, "One", "Two"};
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
