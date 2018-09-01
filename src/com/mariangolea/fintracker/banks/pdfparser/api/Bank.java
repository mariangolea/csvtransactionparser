package com.mariangolea.fintracker.banks.pdfparser.api;

import java.util.Locale;

/**
 * Enum of major banks (Romania for now). <br>
 * Each enum contains links to bank's swift code (needed to identify which bank
 * generated the pdf report), a dedicated parser object, and the locale used to
 * generate the report (impacts the way dates and currency amounts are parsed).
 *
 * @author mariangolea@gmail.com
 */
public enum Bank {
	ING("INGBROBU", Locale.ENGLISH, "Debit CreditDetalii tranzactieData", "\\r\\n"), BRD("BRDEROBU", Locale.ENGLISH,
			"Debit CreditDetalii tranzactieData", "\\r\\n"), BT("BTRLRO22", Locale.ENGLISH,
					"Debit CreditDetalii tranzactieData",
					"\\r\\n"), BCR("RNCBROBU", Locale.ENGLISH, "Debit CreditDetalii tranzactieData", "\\r\\n");

	public final String swiftCode;
	public final Locale locale;
	public final String relevantContentHeaderLine;
	public final String lineSeparator;

	private Bank(final String swiftCode, final Locale locale, final String relevantContentHeaderLine,
			final String lineSeparator) {
		this.swiftCode = swiftCode;
		this.locale = locale;
		this.relevantContentHeaderLine = relevantContentHeaderLine;
		this.lineSeparator = lineSeparator;
	}
}
