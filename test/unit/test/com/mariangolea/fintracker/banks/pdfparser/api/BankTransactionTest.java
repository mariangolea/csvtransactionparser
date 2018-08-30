/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.com.mariangolea.fintracker.banks.pdfparser.api;

import com.mariangolea.fintracker.banks.pdfparser.api.BankTransaction;
import com.mariangolea.fintracker.banks.pdfparser.parsers.INGParser;
import java.util.Arrays;
import java.util.Date;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author Marian Golea <marian.golea@microchip.com>
 */
public class BankTransactionTest {
    
    @Test
    public void testBankTransaction(){
        INGParser ing = new INGParser();
        Date date = ing.parseCompletedDate("19 august 2018");
        BankTransaction first = new BankTransaction("title", date, date, 0, "description", BankTransaction.Type.IN, Arrays.asList("one","two"));
        String toString = first.toString();
        assertTrue(toString != null);
        
        BankTransaction second = new BankTransaction("title", date, date, 0, "description", BankTransaction.Type.IN, Arrays.asList("one","two"));
        
        assertTrue(first.equals(second));
    }
}
