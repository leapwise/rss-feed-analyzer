package com.tp.rssffa.util;

import java.util.List;

import com.tp.rssffa.constants.MessagesConstants;
import com.tp.rssffa.exception.RSSFeedException;

public class ValidationUtil {

	private ValidationUtil() {
	}

	public static void validateRequest(List<String> urls) {
		if (urls.isEmpty())
			throw new RSSFeedException(MessagesConstants.URL_LIST_IS_EMTPY);
		if (urls.size() <= 1)
			throw new RSSFeedException(MessagesConstants.URL_LIST_MINIMUM);
	}

}
