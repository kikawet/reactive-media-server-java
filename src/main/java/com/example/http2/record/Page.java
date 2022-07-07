package com.example.http2.record;

import javax.validation.constraints.Min;

public record Page(@Min(1) int currentPage,@Min(1) int pageSize) {

	public static Page from(int currentPage, int pageSize) {
		return new Page(currentPage, pageSize);
	}

	public int limit() {
		return pageSize;
	}

	public int skip() {
		return pageSize * (currentPage - 1);
	}

}
