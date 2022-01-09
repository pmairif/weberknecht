/*
 * FlexRouter.java (weberknecht)
 *
 * Copyright 2015 Patrick Mairif.
 * The program is distributed under the terms of the Apache License (ALv2).
 *
 * tabstop=4, charset=UTF-8
 */
package com.github.pmairif.weberknecht.request.routing.flex;

import com.github.pmairif.weberknecht.conf.WeberknechtConf;
import com.github.pmairif.weberknecht.request.routing.ActionPath;
import com.github.pmairif.weberknecht.request.routing.RoutingTarget;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;

import static com.github.pmairif.weberknecht.test.TestUtil.readConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * testing FlexRouter
 */
@ExtendWith({MockitoExtension.class})
class FlexRouterTest {

    @Mock
    private HttpServletRequest request;

    private FlexRouter router;

    @BeforeEach
    public void setUp() throws Exception {
        GenericRoute route = new GenericRoute("foo/{id}", new ActionPath("/foo"));
        route.addParameterParser("id", new IntParameterParser());
        RouteCollection selector = new RouteCollection(route);
        router = new FlexRouter(selector, "com.example.foo");

        WeberknechtConf conf = readConfig("test-data/weberknecht-2.xml");
        router.setConfig(conf);
    }

    @Test
    void testRouteUri() {
        when(request.getServletPath()).thenReturn("foo/61");

        RoutingTarget actualTarget = router.routeUri(request);

        RoutingTarget expectedTarget = new RoutingTarget(new ActionPath("/foo"), "", null, null);
        assertEquals(expectedTarget, actualTarget);

        verify(request, times(1)).setAttribute("com.example.foo.id", 61);
    }
}
