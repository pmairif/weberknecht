package com.github.pmairif.weberknecht.request.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JsonActionProcessorTest {

    private JsonActionProcessor processor;

    private JsonView jsonView;

    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
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
