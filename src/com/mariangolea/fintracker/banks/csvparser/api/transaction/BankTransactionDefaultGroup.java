package com.mariangolea.fintracker.banks.csvparser.api.transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Each transaction parser uses a set of hard coded strings in order to identify
 * transactions. <br>
 * This class contains a map of all found transactions during the parse of a set
 * of CSV files. Key is represented by the parsed company descriptor.
 *
 * @author mariangolea@gmail.com
 */
public class BankTransactionDefaultGroup extends BankTransactionAbstractGroup {
    
    private final Map<String, List<BankTransaction>> transactions = new HashMap<>();

    /**
     * Crate a group of similar transactions for a specific bank.
     *
     * @param transactionGroupIdentifier group identifier (null not allowed)
     * @param type transaction type
     */
    public BankTransactionDefaultGroup(final String transactionGroupIdentifier,
            final BankTransaction.Type type) {
        super(transactionGroupIdentifier, type);
    }

    @Override
    public int getTransactionsNumber() {
        int number = 0;

        number = transactions.values().stream().map((list) -> list.size()).reduce(number, Integer::sum);
        return number;
    }

    @Override
    public final void addTransactionImpl(final BankTransaction parsedTransaction) {
        List<BankTransaction> existing = transactions.get(parsedTransaction.getDescription());
        if (existing == null) {
            existing = new ArrayList<>();
            transactions.put(parsedTransaction.getDescription(), existing);
        }
        existing.add(parsedTransaction);
    }
    
    /**
     * Get a copy of the list of company descriptors (keys within the map).
     * @return 
     */
    public final List<String> getCompanyDescriptions() {
        return new ArrayList<>(transactions.keySet());
    }
    
    /**
     * Get a the list of all transactions which belong to a specific company descriptor.
     * @param companyDesc company descriptor
     * @return 
     */
    public final List<BankTransaction> getTransactionsForCompanyDesc(final String companyDesc) {
        return transactions.get(companyDesc);
    }
    
    /**
     * This method should not be invoked on this "level" of grouping.
     * @return "This type should never be displayed :)"
     */
    @Override
    public String toString() {
        return "This type should never be displayed :)";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BankTransactionDefaultGroup that = (BankTransactionDefaultGroup) o;
        return super.equals(that)
                && Objects.equals(transactions, that.transactions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), transactions);
    }
}
