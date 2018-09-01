/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.com.mariangolea.fintracker.banks.pdfparser.api;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.mariangolea.fintracker.banks.pdfparser.api.Bank;
import com.mariangolea.fintracker.banks.pdfparser.api.transaction.BankTransaction;
import com.mariangolea.fintracker.banks.pdfparser.api.transaction.BankTransactionGroup;
import com.mariangolea.fintracker.banks.pdfparser.parsers.INGParser;

/**
 *
 * @author Marian Golea <marian.golea@microchip.com>
 */
public class BankTransactionGroupTest {

	@Test
	public void testGroup() {
		INGParser ing = new INGParser();
		Date date = ing.parseCompletedDate("19 august 2018");
		BankTransaction first = new BankTransaction(Bank.ING.swiftCode, "title", date, date, 1, "description",
				BankTransaction.Type.IN, Arrays.asList("one", "two"));
		BankTransaction second = new BankTransaction(Bank.ING.swiftCode, "title", date, date, 1, "description",
				BankTransaction.Type.IN, Arrays.asList("one", "two"));

		BankTransactionGroup firstGroup = new BankTransactionGroup(Bank.ING.swiftCode, "title",
				BankTransaction.Type.IN);
		firstGroup.addTransaction(first);
		firstGroup.addTransaction(second);

		assertTrue(firstGroup.getType() == BankTransaction.Type.IN);
		assertTrue(firstGroup.getGroupIdentifier().equals("title"));
		assertTrue(firstGroup.getTotalAmount() == 2);

		first = new BankTransaction(Bank.ING.swiftCode, "title", date, date, 1, "description", BankTransaction.Type.IN,
				Arrays.asList("one", "two"));
		second = new BankTransaction(Bank.ING.swiftCode, "title", date, date, 1, "description", BankTransaction.Type.IN,
				Arrays.asList("one", "two"));
		BankTransactionGroup secondGroup = new BankTransactionGroup(Bank.ING.swiftCode, "title",
				BankTransaction.Type.IN);
		secondGroup.addTransaction(first);
		secondGroup.addTransaction(second);

		assertTrue(firstGroup.equals(secondGroup));

		BankTransactionGroup thirdGroup = new BankTransactionGroup(Bank.ING.swiftCode, "title",
				BankTransaction.Type.IN);
		List<BankTransaction> incompatibleTransactions = thirdGroup.addTransactions(Arrays.asList(first, second));
		assertTrue(incompatibleTransactions != null && incompatibleTransactions.isEmpty());
		assertTrue(thirdGroup.equals(secondGroup));
	}
}
