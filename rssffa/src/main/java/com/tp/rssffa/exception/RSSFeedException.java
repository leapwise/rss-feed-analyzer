package com.tp.rssffa.exception;

public class RSSFeedException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private String message;

	public RSSFeedException() {
	}

	public RSSFeedException(String message) {
		this.message = message;
	}
}
