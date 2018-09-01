package test.com.mariangolea.fintracker.banks.pdfparser.api;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.mariangolea.fintracker.banks.pdfparser.api.transaction.UserDefinedTransactionGroup;

public class UserDefinedTransactionGroupTest {
	@Test
	public void testSImpleGroup() {
		UserDefinedTransactionGroup group1 = new UserDefinedTransactionGroup("useless");
		group1.addAssociation("swift", "one");
		group1.addAssociations("swift", new HashSet<>(Arrays.asList("three", "two")));
		assertTrue(group1.getSwiftCodes() != null && group1.getSwiftCodes().size() == 1
				&& group1.getSwiftCodes().contains("swift"));
		Set<String> transactions = group1.getTransactionsFor("swift");
		assertTrue(transactions != null && transactions.size() == 3);
		assertTrue(transactions.containsAll(Arrays.asList("one", "two", "three")));

		UserDefinedTransactionGroup group2 = new UserDefinedTransactionGroup("useless");
		group2.addAssociation("swift", "two");
		group2.addAssociations("swift", new HashSet<>(Arrays.asList("three", "one")));

		assertTrue(group1.equals(group2));

	}
}
