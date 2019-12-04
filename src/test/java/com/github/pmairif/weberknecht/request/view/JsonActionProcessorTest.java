package com.github.pmairif.weberknecht.request.view;

import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JsonActionProcessorTest {

    private JsonActionProcessor processor;

    private JsonView jsonView;

    private HttpServletRequest request;
    private HttpServletResponse response;

    @Before
    public void setUp() {
        processor = new JsonActionProcessor();

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        jsonView = writer -> {
            writer.object();
            writer.key("foo").value("bar");
            writer.endObject();
        };
    }

    @Test
    public void processView() throws IOException {
        StringWriter writer = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        processor.processView(request, response, jsonView);
        assertEquals("{\"foo\":\"bar\"}", writer.getBuffer().toString());
    }
}
