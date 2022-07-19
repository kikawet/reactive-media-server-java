package com.kikawet.reactiveMediaServer.beans;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

@Component
public class PageableMapper {

	public static final Pageable DEFAULT_PAGE_REQUEST = PageRequest.of(0, 20);

	public Pageable getPageable(ServerRequest serverRequest) {
		int page = Integer.parseInt(
				serverRequest.queryParam("page").orElse(Integer.toString(DEFAULT_PAGE_REQUEST.getPageNumber())));
		int size = Integer.parseInt(
				serverRequest.queryParam("size").orElse(Integer.toString(DEFAULT_PAGE_REQUEST.getPageSize())));

		return PageRequest.of(page, size);
	}

}
