package hr.peterlic.rss.feed.exception;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;

/**
 * Class that represents advice used for intercepting exceptions.
 * 
 * @author Ana Peterlic
 */
@Slf4j
@ControllerAdvice
public class ControllerExceptionAdvice
{

	/**
	 * Method for intercepting {@link RuntimeException}. Returns corresponding
	 * {@link ResponseEntity}.
	 *
	 * @param e
	 * @return
	 */
	@ExceptionHandler({ RuntimeException.class })
	public ResponseEntity<String> handleRunTimeException(RuntimeException e)
	{
		return error(INTERNAL_SERVER_ERROR, e);
	}

	/**
	 * Method for intercepting {@link InternalException}. Returns corresponding
	 * {@link ResponseEntity}. *
	 *
	 * @param e
	 * @return
	 */
	@ExceptionHandler({ BadRequestException.class })
	public ResponseEntity<String> handleBadRequestException(BadRequestException e)
	{
		return error(BAD_REQUEST, e);
	}

	/**
	 * Method for intercepting {@link InternalException}. Returns corresponding
	 * {@link ResponseEntity}. *
	 *
	 * @param e
	 * @return
	 */
	@ExceptionHandler({ InternalException.class })
	public ResponseEntity<String> handleInternalException(InternalException e)
	{
		return error(INTERNAL_SERVER_ERROR, e);
	}

	/**
	 * Method for intercepting {@link DatabaseException}. Returns corresponding
	 * {@link ResponseEntity}. *
	 *
	 * @param e
	 * @return
	 */
	@ExceptionHandler({ DatabaseException.class })
	public ResponseEntity<String> handleNotFoundException(DatabaseException e)
	{
		return error(NOT_FOUND, e);
	}

	/**
	 * Method for intercepting {@link IllegalArgumentException}. Returns
	 * corresponding {@link ResponseEntity}.
	 *
	 * @param e
	 * @return
	 */
	@ExceptionHandler({ IllegalArgumentException.class })
	public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e)
	{
		return error(INTERNAL_SERVER_ERROR, e);
	}

	/**
	 * Creates corresponding {@link ResponseEntity}.
	 *
	 * @param status
	 * @param e
	 * @return
	 */
	private ResponseEntity<String> error(HttpStatus status, Exception e)
	{
		log.error("Exception : ", e);
		return ResponseEntity.status(status).body(e.getMessage());
	}

}
