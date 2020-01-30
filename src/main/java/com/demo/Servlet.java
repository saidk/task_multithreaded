package com.demo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@WebServlet("/*")
public class Servlet extends HttpServlet {
    public List<Integer> synchronizedList = Collections.synchronizedList(new ArrayList<Integer>());

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        // I take only first element and ignore if there are additional elements
        Enumeration<String> allParameterNames = request.getParameterNames();
        String inputValue = allParameterNames.nextElement();
        if (!allParameterNames.hasMoreElements()) {
            if (inputValue.equals("end")) {
                synchronized (synchronizedList) {
                    long sum = 0;
                    Iterator<Integer> iterator = synchronizedList.iterator();
                    while (iterator.hasNext()) {
                        sum += iterator.next();
                    }
                    synchronizedList = Collections.synchronizedList(new ArrayList<Integer>());
                    out.write(String.valueOf(sum));
                }
            } else {
                try {
                    synchronizedList.add(Integer.parseInt(inputValue));
                } catch (NumberFormatException e) {
                    out.write(String.valueOf("Please enter integer value or end"));
                }
            }
        }
        else{
            out.write(String.valueOf("Please enter only one element: integer value or end"));
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<h3>Hello World!!!</h3>");
    }
}
