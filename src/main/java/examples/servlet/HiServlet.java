package examples.servlet;

import examples.HttpRequest;
import examples.HttpResponse;

import java.io.PrintWriter;

public class HiServlet extends Servlet {
    public HiServlet() {
        System.out.println("HiServlet 생성");
    }

    @Override
    public void init() {
        System.out.println("init HiServlet");
    }

    @Override
    public void service(HttpRequest request, HttpResponse response) {
        PrintWriter pw = response.getPw();
        pw.println("HTTP/1.1 200 OK");
        pw.println("Content-Type: text/html");
        pw.println();

        for(int i=0;i<5;i++){
            for(int j=i;j<5;j++){
                pw.println("*");
            }
            pw.println("<br>");
        }

        pw.flush();
    }

    @Override
    public void doGet(HttpRequest request, HttpResponse response) {
        System.out.println("request : wanna get hi");
    }

    @Override
    public void doPost(HttpRequest request, HttpResponse response) {
        System.out.println("request : wanna post hi");
    }
}
