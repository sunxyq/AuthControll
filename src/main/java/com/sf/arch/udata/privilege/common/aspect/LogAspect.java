package com.sf.arch.udata.privilege.common.aspect;


import com.sf.arch.udata.privilege.common.annotation.ControllerLog;
import com.sf.arch.udata.privilege.common.annotation.ServiceLog;
import com.sf.arch.udata.privilege.common.util.AppUtil;
import com.sf.arch.udata.privilege.common.util.DateUtil;
import com.sf.arch.udata.privilege.common.util.IPUtil;
import com.sf.arch.udata.privilege.dao.AccountDAO;
import com.sf.arch.udata.privilege.pojo.AccountDO;
import com.sf.arch.udata.privilege.pojo.LogDO;
import com.sf.arch.udata.privilege.service.AccountService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.List;

import com.sf.arch.udata.privilege.service.LogService;
import com.sf.arch.udata.privilege.pojo.ParamData;

@Aspect
@Component
/* 
 * @author Eddy Xiang
 * @date 2018/4/2 下午3:08
 * @class LogAspect
 * @description 
 */
public class LogAspect {
	private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

	@Autowired
	private LogService logService;

	@Autowired
	private AccountDAO dao;

	@Pointcut("@annotation(com.sf.arch.udata.privilege.common.annotation.ServiceLog)")
	public void serviceAspect() {
	}

	@Pointcut("@annotation(com.sf.arch.udata.privilege.common.annotation.ControllerLog)")
	public void controllerAspect() {
	}


	@Before("controllerAspect()")
	/* 
	 * @author Eddy Xiang
	 * @date 2018/4/2 下午2:57
	 * @param [joinPoint] 
	 * @return void 
	 */
	public void doBefore(JoinPoint joinPoint) {
		getLog(joinPoint, null);
	}

	@Around("serviceAspect()")
	public Object doAround(ProceedingJoinPoint pjp) throws Throwable{
		String method = pjp.getSignature().getDeclaringTypeName() + "." + pjp.getSignature().getName();
		Object[] args = pjp.getArgs();

		long beginTime = System.currentTimeMillis();
		Object returnValue = pjp.proceed();
		long endTime = System.currentTimeMillis();

		long executeTime = endTime - beginTime;
		logger.info("method:{}, args:{}, begin time:{}, end time:{}, execute time:{}, return:{}", method, args, beginTime, endTime, executeTime, returnValue);
		return returnValue;
	}

	@AfterThrowing(pointcut = "serviceAspect()", throwing = "e")
	public void doAfterThrowing (JoinPoint joinPoint, Throwable e) throws Throwable {
		getLog(joinPoint, e);
	}
	
	/*
	 * @author Eddy Xiang
	 * @date 2018/4/2 下午2:57
	 * @param
	 * @return
	 */
	private LogDO getLog(JoinPoint joinPoint, Throwable e){
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		String uid = AppUtil.getUidFromCookie(request);
		String userName = "";
		LogDO log = new LogDO();
		log.setUid(uid);
		List<AccountDO> list = null;
		if(uid != null)
			list = dao.findByUid(uid);
		if(list != null && list.size() > 0){
			userName = list.get(0).getUname();
		}
		ParamData params = new ParamData();
		log.setUname(userName);
		log.setType(1);
		log.setUrl(request.getRequestURI());
		log.setMethod((joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()"));
		log.setParams(params.toString());
		log.setIp(IPUtil.getIpAdd(request));
		log.setCtime(DateUtil.getDateInt());
		try {
			if(null == e){
				ControllerLog logAnnotation = getControllerAnnotation(joinPoint);
				log.setDescription(logAnnotation.value());
				log.setType(logAnnotation.type());
				log.setDetail("No error log!");
			}else{
				ServiceLog logAnnotation = getServiceAnnotation(joinPoint);
				log.setDescription(logAnnotation.value());
				log.setType(logAnnotation.type());
				log.setDetail(e.getMessage());
			}
			logService.saveLog(log);
		} catch (Exception ex) {
			logger.error("Save Log Error:{}", ex.getMessage());
		}
		return log;
	}

	@SuppressWarnings("rawtypes")
	public static ControllerLog getControllerAnnotation(JoinPoint joinPoint) throws Exception {
		String targetName = joinPoint.getTarget().getClass().getName();
		String methodName = joinPoint.getSignature().getName();
		Object[] arguments = joinPoint.getArgs();
		Class targetClass = Class.forName(targetName);
		Method[] methods = targetClass.getMethods();
		ControllerLog logAnnotation = null;
		for (Method method : methods) {
			if (method.getName().equals(methodName)) {
				Class[] clazzs = method.getParameterTypes();
				if (clazzs.length == arguments.length) {
					logAnnotation = method.getAnnotation(ControllerLog.class);
					break;
				}
			}
		}
		return logAnnotation;
	}
	
	@SuppressWarnings("rawtypes")
	public static ServiceLog getServiceAnnotation(JoinPoint joinPoint) throws Exception {
		String targetName = joinPoint.getTarget().getClass().getName();
		String methodName = joinPoint.getSignature().getName();
		Object[] arguments = joinPoint.getArgs();
		Class targetClass = Class.forName(targetName);
		Method[] methods = targetClass.getMethods();
		ServiceLog logAnnotation = null;
		for (Method method : methods) {
			if (method.getName().equals(methodName)) {
				Class[] clazzs = method.getParameterTypes();
				if (clazzs.length == arguments.length) {
					logAnnotation = method.getAnnotation(ServiceLog.class);
					break;
				}
			}
		}
		return logAnnotation;
	}
}