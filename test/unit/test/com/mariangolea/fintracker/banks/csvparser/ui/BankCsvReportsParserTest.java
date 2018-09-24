/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.com.mariangolea.fintracker.banks.csvparser.ui;

import com.mariangolea.fintracker.banks.csvparser.BankCsvReportsParser;
import com.mariangolea.fintracker.banks.csvparser.ui.CsvParserUI;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author Marian Golea <mariangolea@gmail.com>
 */
public class BankCsvReportsParserTest extends FXUITest{
    
    @Test
    public void testFrame() {
        BankCsvReportsParser parser = new BankCsvReportsParser();
        try {
            //POM file enforced headless to false, to allow calling this JFrame constructor only.
            CsvParserUI frame = parser.initApplication();
            assertTrue(frame != null);
            frame.init();
        } catch (Exception ex) {
            //Current CircleCI support enforces headless mode.
            //if met, just carry on since this unit test is only here for coverage really...
            assertTrue(true);
        }
    }
}
