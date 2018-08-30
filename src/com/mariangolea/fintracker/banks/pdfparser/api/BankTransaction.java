package com.mariangolea.fintracker.banks.pdfparser.api;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Container class for all relevant fields of a specific bank transaction.
 *
 * @author mariangolea@gmail.com
 */
public final class BankTransaction implements Serializable {

    public enum Type {
        IN, OUT
    }

    // Date when the transaction has been initiated by a client.
    public final Date startDate;
    // Date when the transaction has been settled by the bank. Same as startDate in
    // most cases.
    public final Date completedDate;
    // amount of currency used in this transaction.
    public final float amount;
    // description of the transaction. useful for a client to recategorize a certain
    // transaction.
    public final String description;
    // type of transaction.
    public final Type type;
    // title as it appears in pdf
    public final String title;

    private final List<String> pdfContent = new ArrayList<>();

    /**
     * Constructs a transaction instance.
     *
     * @param title
     * @param startDate date when transaction has been initiated by the client.
     * @param completedDate date of transaction settlement.
     * @param amount currency amount
     * @param description transaction description (useful for a client to re
     * categorize a certain transaction).
     * @param type transaction type
     * @param pdfContent original pdf content.
     */
    public BankTransaction(final String title, final Date startDate, final Date completedDate, float amount,
            final String description, final Type type, final List<String> pdfContent) {
        super();
        Objects.nonNull(title);
        
        this.startDate = startDate;
        this.completedDate = completedDate;
        this.amount = amount;
        this.description = description;
        this.type = type;
        this.title = title;
        this.pdfContent.addAll(pdfContent);
    }
    
    public List<String> getOriginalPDFContent(){
        return new ArrayList<>(pdfContent);
    }
    
    public int getOriginalPDFContentLinesNumber(){
        return pdfContent.size();
    }
    
    @Override
    public String toString() {
        String string = "";

        string = string.concat(title).concat(",");
        string = string.concat(type.name()).concat(",");
        string = string.concat(description).concat(",");
        string = string.concat(DateFormat.getInstance().format(startDate)).concat(",");
        string += amount;

        return string;
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj || !(obj instanceof BankTransaction)) {
            return false;
        }
        BankTransaction other = (BankTransaction) obj;
        return title.equals(other.title) && type == other.type && amount == other.amount
                && startDate.equals(other.startDate) && completedDate.equals(other.completedDate)
                && description.equals(other.description);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.title);
        hash = 89 * hash + Objects.hashCode(this.startDate);
        hash = 89 * hash + Objects.hashCode(this.completedDate);
        hash = 89 * hash + (int) (Double.doubleToLongBits(this.amount) ^ (Double.doubleToLongBits(this.amount) >>> 32));
        hash = 89 * hash + Objects.hashCode(this.description);
        hash = 89 * hash + Objects.hashCode(this.type);
        return hash;
    }
}
