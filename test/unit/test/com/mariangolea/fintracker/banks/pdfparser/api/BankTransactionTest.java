/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.com.mariangolea.fintracker.banks.pdfparser.api;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Date;

import org.junit.Test;

import com.mariangolea.fintracker.banks.pdfparser.api.Bank;
import com.mariangolea.fintracker.banks.pdfparser.api.transaction.BankTransaction;
import com.mariangolea.fintracker.banks.pdfparser.parsers.INGParser;

/**
 *
 * @author Marian Golea <marian.golea@microchip.com>
 */
public class BankTransactionTest {

	@Test
	public void testBankTransaction() {
		INGParser ing = new INGParser();
		Date date = ing.parseCompletedDate("19 august 2018");
		BankTransaction first = new BankTransaction(Bank.ING.swiftCode, "title", date, date, 0, "description",
				BankTransaction.Type.IN, Arrays.asList("one", "two"));
		String toString = first.toString();
		assertTrue(toString != null);
		assertTrue(first.getAmount() == 0);
		assertTrue(first.getBankSwiftCode() == Bank.ING.swiftCode);
		assertTrue(first.getCompletedDate() == date);
		assertTrue(first.getStartDate() == date);
		assertTrue(first.getDescription().equals("description"));
		assertTrue(first.getTitle().equals("title"));
		assertTrue(first.getType() == BankTransaction.Type.IN);
		assertTrue(first.getPdfContent().equals(Arrays.asList("one", "two")));
		assertTrue(first.getOriginalPDFContentLinesNumber() == 2);

		BankTransaction second = new BankTransaction(Bank.ING.swiftCode, "title", date, date, 0, "description",
				BankTransaction.Type.IN, Arrays.asList("one", "two"));

		assertTrue(first.equals(second));
	}
}
