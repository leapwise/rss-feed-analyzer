package hr.peterlic.rss.feed.aspect;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * Aspect used for logging methods before and after execution.
 * 
 * @author Ana Peterlic
 */
@Slf4j
@Aspect
@Component
public class LoggerAspect
{
	/**
	 * Log before method execution
	 *
	 * @param joinPoint
	 */
	@Before("execution(* hr.peterlic.rss.feed.*.*.*(..))")
	public void logBefore(JoinPoint joinPoint)
	{
		Object[] args = joinPoint.getArgs();
		log.info("-> " + joinPoint.getSignature().getName() + " with parameters: " + arrToString(args));
	}

	/**
	 * Log after method execution
	 *
	 * @param joinPoint
	 * @param result
	 */
	@AfterReturning(pointcut = "execution(* hr.peterlic.rss.feed.*.*.*(..))", returning = "result")
	public void logAfter(JoinPoint joinPoint, Object result)
	{
		log.info("<- " + joinPoint.getSignature().getName() + (result != null ? " with result: " + result : ""));
	}

	/**
	 * Convert arguments to string
	 *
	 * @param args
	 * @return
	 */
	private String arrToString(Object[] args)
	{
		if (args == null)
			return "";
		else if (args.length == 0)
			return "";
		else if (args.length == 1)
		{
			return (args[0] != null ? args[0].toString() : "null");
		}
		else
			return Arrays.toString(args);
	}

}
