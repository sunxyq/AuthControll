package com.sf.arch.udata.privilege;

import com.sf.arch.bootstrap.Boot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class UdataPrivilegeApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder app){
		return app.sources(UdataPrivilegeApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(UdataPrivilegeApplication.class, args);
	}
}
