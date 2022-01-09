/*
 * ControllerFilterTest.java (weberknecht)
 *
 * Copyright 2013 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * tabstop=4, charset=UTF-8
 */
package com.github.pmairif.weberknecht;

import com.github.pmairif.weberknecht.conf.WeberknechtConf;
import com.github.pmairif.weberknecht.test.MockAction;
import com.github.pmairif.weberknecht.test.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author pmairif
 */
public class ControllerFilterTest {

	private ControllerFilter filter;
	
	private HttpServletRequest request;
	private HttpServletResponse response;
	private ServletContext servletContext;
	private FilterChain filterChain;
	
	@BeforeEach
	public void setUp() throws Exception {
		filter = new ControllerFilter();
		
		servletContext = mock(ServletContext.class);

		WeberknechtConf conf = TestUtil.readConfig("test-data/weberknecht-controller-test.xml");
		filter.init(servletContext, conf, null);

		request = mock(HttpServletRequest.class);
		when(request.getServletPath()).thenReturn("");
		
		response = mock(HttpServletResponse.class);
		
		filterChain = mock(FilterChain.class);
		
		MockAction.setCallCount(0);
	}

	/**
	 * action is executed
	 */
	@Test
	public void testDoFilterActionExecuted() throws IOException, ServletException {
		when(request.getPathInfo()).thenReturn("/foo.do");
		
		filter.doFilter(request, response, filterChain);
		
		//action was executed
		assertEquals(1, MockAction.getCallCount());
		
		//filter chain not continued
		verify(filterChain, times(0)).doFilter(request, response);
	}

	/**
	 * router found no target, no action executed
	 */
	@Test
	public void testDoFilterActionNotExecuted() throws IOException, ServletException {
		when(request.getPathInfo()).thenReturn("/xyz");
		
		filter.doFilter(request, response, filterChain);
		
		//no action executed
		assertEquals(0, MockAction.getCallCount());
		
		//filter chain continued
		verify(filterChain, times(1)).doFilter(request, response);
	}
}
