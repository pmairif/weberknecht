/*
 * MetaRouterTest.java (weberknecht)
 *
 * Copyright 2013 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * tabstop=4, charset=UTF-8
 */
package com.github.pmairif.weberknecht.request.routing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

/**
 * testing {@link MetaRouter}
 *
 * @author pmairif
 */
class MetaRouterTest {

	private MetaRouter router;
	
	private Router r1, r2;
	
	@BeforeEach
	public void setUp() throws Exception {
		r1 = mock(Router.class);
		r2 = mock(Router.class);
		
		router = new MetaRouter();
		router.addRouter(r1);
		router.addRouter(r2);
	}

	/**
	 * Test method for {@link MetaRouter#routeUri(java.lang.String)}.
	 */
	@Test
	public void testRouteUri() {
		HttpServletRequest foo = mock(HttpServletRequest.class);
		HttpServletRequest bar = mock(HttpServletRequest.class);
		HttpServletRequest foobar = mock(HttpServletRequest.class);
		
		RoutingTarget targetFoo = new RoutingTarget("foo", "do", null);
		RoutingTarget targetBar = new RoutingTarget("bar", "do", null);
		when(r1.routeUri(foo)).thenReturn(targetFoo);
		when(r2.routeUri(bar)).thenReturn(targetBar);
		
		assertEquals(targetFoo, router.routeUri(foo));
		assertEquals(targetBar, router.routeUri(bar));
		assertNull(router.routeUri(foobar));
		
		verify(r1, times(1)).routeUri(foo);
		verify(r1, times(1)).routeUri(bar);
		verify(r2, times(0)).routeUri(foo);
		verify(r2, times(1)).routeUri(bar);
	}
}
