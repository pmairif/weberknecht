package com.github.pmairif.weberknecht.request.processing;

import com.github.pmairif.weberknecht.request.routing.RoutingTarget;
import com.github.pmairif.weberknecht.request.actions.ExecutableAction;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Vector;

import static org.mockito.Mockito.*;

/**
 * Created by rick on 2015-12-06.
 */
public class ProcessingChainTest {

	private HttpServletRequest request;
	private HttpServletResponse response;

	private List<Processor> processors;

	@Before
	public void setUp() throws Exception {
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);

		processors = new Vector<>();
		processors.add(new ActionExecution());
	}

	/**
	 * change action before execution and make sure the right one is executed.
	 */
	@Test
	public void testActionExecuted() throws Exception {
		ExecutableAction action1 = mock(ExecutableAction.class);
		ExecutableAction action2 = mock(ExecutableAction.class);
		RoutingTarget routingTarget = mock(RoutingTarget.class);

		ProcessingChain chain = new ProcessingChain(processors, request, response, routingTarget, action1);

		chain.setAction(action2);	//change action before execution

		chain.doContinue();

		//make sure the right action is executed
		verify(action1, never()).execute(request);
		verify(action2, times(1)).execute(request);
	}
}
