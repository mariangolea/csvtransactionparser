/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.com.mariangolea.fintracker.banks.csvparser.ui;

import com.mariangolea.fintracker.banks.csvparser.BankCsvReportsParser;
import java.awt.HeadlessException;
import javax.swing.JFrame;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author Marian
 */
public class BankCsvReportsParserTest {

    @Test
    public void testFrame() {
        BankCsvReportsParser parser = new BankCsvReportsParser();
        try {
            //POM file enforced headless to false, to allow calling this JFrame constructor only.
            JFrame frame = parser.initFrame();
            assertTrue(frame.getTitle() != null && !frame.getTitle().isEmpty());
        } catch (HeadlessException e) {
            //Current CircleCI support enforces headless mode.
            //if met, just carry on since this unit test is only here for coverage really...
            assertTrue(true);
        }
    }
}
