/*
 * Copyright (c) 2018 by sf-express
 */

package com.sf.arch.udata.privilege.common.filter;

import com.sf.arch.udata.privilege.common.ErrorMsg;
import com.sf.arch.udata.privilege.common.util.AppUtil;
import com.sf.arch.udata.privilege.common.util.PrivilegeUtil;
import com.sf.arch.udata.privilege.pojo.PrivilegeDO;
import com.sf.arch.udata.privilege.service.AccountService;
import net.sf.json.JSONObject;
import org.apache.catalina.filters.RemoteIpFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;


@Configuration
public class PrivilegeFilter {
    private static final Logger logger = LoggerFactory.getLogger(PrivilegeFilter.class);

    @Bean
    public RemoteIpFilter remoteIpFilter() {
        return new RemoteIpFilter();
    }

    @Autowired
    private AccountService service;

    @Bean
    public FilterRegistrationBean testFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new ControllerFilter());//set filter
        registration.addUrlPatterns("/*");//set all path to be filtered
        registration.setName("PrivilegeFilter");//set name
        registration.setOrder(1);//set order
        return registration;
    }

    public class ControllerFilter implements Filter {
        @Override
        public void destroy() {
        }

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
                throws IOException, ServletException {
            Long start = System.currentTimeMillis();
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            String uri = httpServletRequest.getRequestURI();
            String uid = AppUtil.getUidFromCookie((HttpServletRequest)request);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            JSONObject obj;
            if(uid == null){
                obj = JSONObject.fromObject(AppUtil.responseData(ErrorMsg.NOT_LOGIN_ERROR));
                response.getWriter().print(obj.toString());
                return;
            }
            logger.info("Welcome " + uid + " !");

            //if work status is off or not in account list
            if(!PrivilegeUtil.hasCommonPrivilege(uri)) {
                List<String> list = service.findPrivileges(uid, uri);
                if (list == null || list.size() == 0) {
                    obj = JSONObject.fromObject(AppUtil.responseData(ErrorMsg.NO_PRIVILEGE_ERROR));
                    response.getWriter().print(obj.toString());
                    logger.info("Current URL is :" + uri);
                    return;
                }
            }
            Long end = System.currentTimeMillis();
            logger.info("Execute time: " + (end - start));
            filterChain.doFilter(request, response);
        }

        @Override
        public void init(FilterConfig arg0) throws ServletException {
        }
    }
}