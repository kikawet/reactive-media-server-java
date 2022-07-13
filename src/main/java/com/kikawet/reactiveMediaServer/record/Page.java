package com.kikawet.reactiveMediaServer.record;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import com.kikawet.reactiveMediaServer.exception.OutOfRangeException;

public record Page(
		@Min(1) int currentPage,
		@Min(1) @Max(200) int pageSize) {

	public static Page from(int currentPage, int pageSize) {
		if (currentPage < 1) throw new OutOfRangeException(currentPage, 1, null);
		if (pageSize < 1 || pageSize > 200) throw new OutOfRangeException(pageSize, 1, 200);

		return new Page(currentPage, pageSize);
	}

	public int limit() {
		return pageSize;
	}

	public int skip() {
		return pageSize * (currentPage - 1);
	}

}
