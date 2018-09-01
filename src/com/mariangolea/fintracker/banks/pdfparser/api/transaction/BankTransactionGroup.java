package com.mariangolea.fintracker.banks.pdfparser.api.transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Each transaction parser uses a set of hardcoded strings in order to identify
 * transactions. <br>
 * This class contains a list of all transactions which belong to a unique
 * "category" string, aka multiple instances of the same transaction type.
 *
 * @author mariangolea@gmail.com
 */
public final class BankTransactionGroup {

	private final List<BankTransaction> transactions = new ArrayList<>();
	private final String bankSwiftCode;
	private final String transactionGroupIdentifier;
	private final BankTransaction.Type type;

	/**
	 * Crate a group of similar transactions for a specific bank.
	 * 
	 * @param transactionGroupIdentifier
	 *            group identifier (null not allowed)
	 * @param bankSwiftCode
	 *            bank swift code (null not allowed)
	 */
	public BankTransactionGroup(final String bankSwiftCode, final String transactionGroupIdentifier,
			final BankTransaction.Type type) {
		Objects.requireNonNull(transactionGroupIdentifier);
		Objects.requireNonNull(bankSwiftCode);
		Objects.requireNonNull(type);

		this.transactionGroupIdentifier = transactionGroupIdentifier;
		this.bankSwiftCode = bankSwiftCode;
		this.type = type;
	}

	/**
	 * Get the group identifier (common transactions title as they show up in pdf).
	 * 
	 * @return transactionGroupIdentifier
	 */
	public final String getGroupIdentifier() {
		return transactionGroupIdentifier;
	}

	public final String getBankSwiftCode() {
		return bankSwiftCode;
	}

	/**
	 * Add a specific bank transaction to the current group.
	 * 
	 * @param parsedTransaction
	 *            bank transaction (null not allowed)
	 * @return true if successful: transaction is compatible to current group
	 *         (matches bank swift code and transaction title).
	 */
	public final boolean addTransaction(final BankTransaction parsedTransaction) {
		if (matchesTransaction(parsedTransaction)) {
			transactions.add(parsedTransaction);
			return true;
		}
		return false;
	}

	/**
	 * Add a list of compatible transactions.
	 * 
	 * @param parsedTransactions
	 *            list of compatible transactions
	 * @return sub list of incompatible transactions (do not match title or bank
	 *         swift code).
	 */
	public final List<BankTransaction> addTransactions(final List<BankTransaction> parsedTransactions) {
		List<BankTransaction> incompatibleTransactions = new ArrayList<>();
		for (BankTransaction transaction : parsedTransactions) {
			boolean added = addTransaction(transaction);
			if (!added) {
				incompatibleTransactions.add(transaction);
			}
		}
		return incompatibleTransactions;
	}

	public final List<BankTransaction> getTransactions() {
		return new ArrayList<>(transactions);
	}

	public final double getTotalAmount() {
		double amount = 0;
		for (BankTransaction transaction : transactions) {
			amount += transaction.getAmount();
		}

		return amount;
	}

	public final BankTransaction.Type getType() {
		return type;
	}

	public boolean matchesTransaction(final BankTransaction transaction) {
		return null != transaction && transactionGroupIdentifier.equals(transaction.getTitle())
				&& bankSwiftCode.equals(transaction.getBankSwiftCode()) && type == transaction.getType();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + bankSwiftCode.hashCode();
		result = prime * result + transactionGroupIdentifier.hashCode();
		result = prime * result + transactions.hashCode();
		result = prime * result + type.hashCode();
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof BankTransactionGroup))
			return false;
		BankTransactionGroup other = (BankTransactionGroup) obj;
		if (!bankSwiftCode.equals(other.bankSwiftCode))
			return false;
		if (!transactionGroupIdentifier.equals(other.transactionGroupIdentifier))
			return false;
		if (type != other.type)
			return false;
		if (transactions.size() != other.transactions.size())
			return false;
		return transactions.containsAll(other.transactions);
	}

}
