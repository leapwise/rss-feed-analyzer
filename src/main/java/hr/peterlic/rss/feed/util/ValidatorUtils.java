package hr.peterlic.rss.feed.util;

import org.apache.commons.validator.routines.UrlValidator;

/**
 * @author Ana Peterlic
 */
public class ValidatorUtils
{

	/**
	 * Checks if provided parameter is null.
	 * 
	 * @param parameter
	 * @param <T>
	 */
	public static <T> void notNull(T parameter)
	{
		if (isNull(parameter))
		{
			throwException(ConstantsUtils.ERROR_PARAMETER_MUST_NOT_BE_NULL);
		}
	}

	private static <T> boolean isNull(T parameter)
	{
		return (parameter == null);
	}

	/**
	 * Checks if provided URL is valid depending on provided schemes.
	 * 
	 * @param parameter
	 */
	public static void isUrlValid(String parameter)
	{
		String[] schemes = { "http", "https" };
		UrlValidator urlValidator = new UrlValidator(schemes);

		if (!urlValidator.isValid(parameter))
		{
			throwException(ConstantsUtils.ERROR_PARAMETER_WRONG_URL_FORMAT);
		}
	}

	private static void throwException(String message)
	{
		throw new IllegalArgumentException(message);
	}

}
