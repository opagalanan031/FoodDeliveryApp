package com.learning.aop;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
//import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
@Aspect
public class LogAspect {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LogAspect.class);
	
	// when the log has to be generated
	@Before("execution(* com.learning.controller.*.*(..))")	// triggering point from anywhere within the package
	public void logRestController1() {
		LOGGER.info("before advice called");
	}
	
	// when the log has to be generated
	@After("execution(* com.learning.controller.*.*(..))")	// triggering point from anywhere within the package
	public void logRestController2() {
		LOGGER.info("after advice called");
	}
	
	@Pointcut("within(@org.springframework.stereotype.Repository *)" + 
	"|| within(@org.springframework.stereotype.Service *)" + "|| within(@org.springframework.stereotype.RestController *)" )
	public void springPointCutExp() {
		
	}
}
