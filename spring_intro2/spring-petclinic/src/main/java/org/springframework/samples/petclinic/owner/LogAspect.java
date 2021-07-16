package org.springframework.samples.petclinic.owner;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Component
@Aspect      // @LogExecutionTime Annotation이 있는 곳에 적용
public class LogAspect {

	Logger logger = LoggerFactory.getLogger(LogAspect.class);

	// @Around Annotation을 사용하는 메소드 안에서 즉, @LogExecutionTime이 붙어 있는 메소드로부터 joinPoint를 받음.
	@Around("@annotation(LogExecutionTime)")
	public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		// joinPoint.proceed()를 통해 실제 target 메소드 호출
		Object proceed = joinPoint.proceed();

		stopWatch.stop();
		logger.info(stopWatch.prettyPrint());

		return proceed;
	}
}
