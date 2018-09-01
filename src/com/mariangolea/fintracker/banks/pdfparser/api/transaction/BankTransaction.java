package com.mariangolea.fintracker.banks.pdfparser.api.transaction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Container class for all relevant fields of a specific bank transaction. This
 * is the container of raw pdf data for any transaction.
 *
 * @author mariangolea@gmail.com
 */
public final class BankTransaction implements Serializable {

	public enum Type {
		IN, OUT
	}

	// Date when the transaction has been initiated by a client.
	private final Date startDate;
	// Date when the transaction has been settled by the bank. Same as startDate in
	// most cases.
	private final Date completedDate;
	// amount of currency used in this transaction.
	private final float amount;
	// description of the transaction. useful for a client to recategorize a certain
	// transaction.
	private final String description;
	// type of transaction.
	private final Type type;
	// title as it appears in pdf
	private final String title;
	// bank swift code
	private final String bankSwiftCode;

	// pdf content lines read from pdf file, before they were parsed.
	private final List<String> pdfContent = new ArrayList<>();

	/**
	 * Constructs a transaction instance.
	 *
	 * @param title
	 * @param startDate
	 *            date when transaction has been initiated by the client.
	 * @param completedDate
	 *            date of transaction settlement.
	 * @param amount
	 *            currency amount
	 * @param description
	 *            transaction description (useful for a client to re categorize a
	 *            certain transaction).
	 * @param type
	 *            transaction type
	 * @param pdfContent
	 *            original pdf content.
	 */
	public BankTransaction(final String bankSwiftCode, final String title, final Date startDate,
			final Date completedDate, float amount, final String description, final Type type,
			final List<String> pdfContent) {
		super();
		Objects.requireNonNull(bankSwiftCode);
		Objects.requireNonNull(title);
		Objects.requireNonNull(startDate);
		Objects.requireNonNull(completedDate);
		Objects.requireNonNull(amount);
		Objects.requireNonNull(description);
		Objects.requireNonNull(type);
		Objects.requireNonNull(pdfContent);

		this.bankSwiftCode = bankSwiftCode;
		this.title = title;
		this.startDate = startDate;
		this.completedDate = completedDate;
		this.amount = amount;
		this.description = description;
		this.type = type;
		this.pdfContent.addAll(pdfContent);
	}

	public final Date getStartDate() {
		return startDate;
	}

	public final Date getCompletedDate() {
		return completedDate;
	}

	public final float getAmount() {
		return amount;
	}

	public final String getDescription() {
		return description;
	}

	public final Type getType() {
		return type;
	}

	public final String getTitle() {
		return title;
	}

	public final String getBankSwiftCode() {
		return bankSwiftCode;
	}

	public final List<String> getPdfContent() {
		return pdfContent;
	}

	public final int getOriginalPDFContentLinesNumber() {
		return pdfContent.size();
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
		result = prime * result + Float.floatToIntBits(amount);
		result = prime * result + ((bankSwiftCode == null) ? 0 : bankSwiftCode.hashCode());
		result = prime * result + ((completedDate == null) ? 0 : completedDate.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((pdfContent == null) ? 0 : pdfContent.hashCode());
		result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof BankTransaction)) {
			return false;
		}
		BankTransaction other = (BankTransaction) obj;
		if (Float.floatToIntBits(amount) != Float.floatToIntBits(other.amount)) {
			return false;
		}
		if (bankSwiftCode == null) {
			if (other.bankSwiftCode != null) {
				return false;
			}
		} else if (!bankSwiftCode.equals(other.bankSwiftCode)) {
			return false;
		}
		if (completedDate == null) {
			if (other.completedDate != null) {
				return false;
			}
		} else if (!completedDate.equals(other.completedDate)) {
			return false;
		}
		if (description == null) {
			if (other.description != null) {
				return false;
			}
		} else if (!description.equals(other.description)) {
			return false;
		}
		if (pdfContent == null) {
			if (other.pdfContent != null) {
				return false;
			}
		} else if (!pdfContent.equals(other.pdfContent)) {
			return false;
		}
		if (startDate == null) {
			if (other.startDate != null) {
				return false;
			}
		} else if (!startDate.equals(other.startDate)) {
			return false;
		}
		if (title == null) {
			if (other.title != null) {
				return false;
			}
		} else if (!title.equals(other.title)) {
			return false;
		}
		if (type != other.type) {
			return false;
		}
		return true;
	}

}
