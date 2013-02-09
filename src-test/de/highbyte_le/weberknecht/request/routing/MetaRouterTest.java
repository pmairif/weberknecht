/*
 * MetaRouterTest.java (weberknecht)
 *
 * Copyright 2013 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * tabstop=4, charset=UTF-8
 */
package de.highbyte_le.weberknecht.request.routing;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * testing {@link MetaRouter}
 *
 * @author pmairif
 */
public class MetaRouterTest {

	private MetaRouter router;
	
	private Router r1, r2;
	
	@Before
	public void setUp() throws Exception {
		r1 = mock(Router.class);
		r2 = mock(Router.class);
		
		router = new MetaRouter();
		router.addRouter(r1);
		router.addRouter(r2);
	}

	/**
	 * Test method for {@link de.highbyte_le.weberknecht.request.routing.MetaRouter#routeUri(java.lang.String)}.
	 */
	@Test
	public void testRouteUri() {
		RoutingTarget targetFoo = new RoutingTarget("foo", "do", null);
		RoutingTarget targetBar = new RoutingTarget("bar", "do", null);
		when(r1.routeUri("/foo")).thenReturn(targetFoo);
		when(r2.routeUri("/bar")).thenReturn(targetBar);
		
		assertEquals(targetFoo, router.routeUri("/foo"));
		assertEquals(targetBar, router.routeUri("/bar"));
		assertNull(router.routeUri("/foobar"));
		
		verify(r1, times(1)).routeUri("/foo");
		verify(r1, times(1)).routeUri("/bar");
		verify(r2, times(0)).routeUri("/foo");
		verify(r2, times(1)).routeUri("/bar");
	}
}
