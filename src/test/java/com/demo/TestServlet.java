package com.demo;

import static org.junit.Assert.*;

import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import org.junit.Test;
import org.mockito.Mockito;

public class TestServlet extends Mockito {

    @Test
    public void testSynchronousTwentyRequestsSum() throws Exception {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        Random rand = new Random();
        Servlet servlet = new Servlet();
        long sum = 0;
        for (int i = 0; i <= 20; i++) {
            HttpServletRequest request = mock(HttpServletRequest.class);
            HttpServletResponse response = mock(HttpServletResponse.class);
            if (i == 20) {
                when(request.getParameterNames()).thenReturn(Collections.enumeration(
                        Arrays.asList("end")));
            } else {
                int value = rand.nextInt(Integer.MAX_VALUE);
                when(request.getParameterNames()).thenReturn(Collections.enumeration(
                        Arrays.asList(String.valueOf(value))));
                sum += value;
            }
            when(response.getWriter()).thenReturn(writer);
            servlet.doPost(request, response);
        }

        writer.flush();
        long outputValue = Long.parseLong(stringWriter.toString());
        assertEquals(outputValue, sum);
    }

    @Test
    public void testAsynchronousCalls() throws Exception {
        StringWriter stringWriter = new StringWriter();
        final PrintWriter writer = new PrintWriter(stringWriter);

        final Servlet servlet = new Servlet();
        final HttpServletResponse response = mock(HttpServletResponse.class);

        Thread threadOne = new Thread(new Runnable() {
            @Override
            public void run() {
                HttpServletRequest requestThreadOne = mock(HttpServletRequest.class);

                when(requestThreadOne.getParameterNames()).thenReturn(Collections.enumeration(
                        Arrays.asList(String.valueOf(2))));

                try {
                    servlet.doPost(requestThreadOne, response);
                } catch (ServletException|IOException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread threadTwo = new Thread(new Runnable() {
            @Override
            public void run() {
                HttpServletRequest requestThreadTwo = mock(HttpServletRequest.class);

                when(requestThreadTwo.getParameterNames()).thenReturn(Collections.enumeration(
                        Arrays.asList(String.valueOf(6))));
                try {
                    servlet.doPost(requestThreadTwo, response);
                } catch (ServletException|IOException e) {
                    e.printStackTrace();
                }
            }
        });

        threadOne.start();
        threadTwo.start();
        Thread.sleep(1000);

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameterNames()).thenReturn(Collections.enumeration(
                Arrays.asList(String.valueOf("end"))));
        when(response.getWriter()).thenReturn(writer);

        servlet.doPost(request, response);

        writer.flush();
        long outputValue = Long.parseLong(stringWriter.toString());
        assertEquals(outputValue, 8L);
    }

    @Test
    public void testForWrongInput() throws Exception {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        Servlet servlet = new Servlet();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getParameterNames()).thenReturn(Collections.enumeration(
                Arrays.asList("test")));

        when(response.getWriter()).thenReturn(writer);
        servlet.doPost(request, response);

        writer.flush();
        String outputValue = stringWriter.toString();
        assertEquals(outputValue, "Please enter integer value or end");
    }

    @Test
    public void testAcceptingOnlyOneParameter() throws Exception{
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        Servlet servlet = new Servlet();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getParameterNames()).thenReturn(Collections.enumeration(
                Arrays.asList("1","2")));

        when(response.getWriter()).thenReturn(writer);
        servlet.doPost(request, response);

        writer.flush();
        String outputValue = stringWriter.toString();
        assertEquals(outputValue, "Please enter only one element: integer value or end");
    }
}
