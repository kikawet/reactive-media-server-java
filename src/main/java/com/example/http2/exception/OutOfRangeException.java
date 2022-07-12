package com.example.http2.exception;

public class OutOfRangeException extends IllegalArgumentException {
	public OutOfRangeException(final Object value, final Object min, final Object max) {
		this(buildErrorMsg(value, min, max));
	}

	public OutOfRangeException(final String msg) {
		super(msg);
	}

	private static String buildErrorMsg(final Object value, final Object min, final Object max) {

		String minStr = min != null ? min.toString() : "-∞";
		String maxStr = max != null ? max.toString() : "+∞";

		return "Given value: " + value + " - Accepted range: (" + minStr + "," + maxStr + ")";
	}
}
