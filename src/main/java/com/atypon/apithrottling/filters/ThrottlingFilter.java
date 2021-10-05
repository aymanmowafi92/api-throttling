package com.atypon.apithrottling.filters;

import java.io.IOException;
import java.time.LocalDateTime;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import com.atypon.apithrottling.handlers.ThrottlingHandler;

public class ThrottlingFilter implements Filter {

	private static final String IP_PARAMETER_NAME = "ip";

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		String ip = request.getParameter(IP_PARAMETER_NAME);
		boolean serviceAvailable = ThrottlingHandler.getInstance().applyThrottlingRule(ip);
		if(serviceAvailable) {
			chain.doFilter(request,response);
		} else {
			((HttpServletResponse) response).sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE, "Unavailable Service. Please try again later" + LocalDateTime.now());
		}
	}

}
