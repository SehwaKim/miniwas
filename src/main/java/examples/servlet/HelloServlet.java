package examples.servlet;

import examples.HttpRequest;
import examples.HttpResponse;

public class HelloServlet extends Servlet {
    public HelloServlet() {
        System.out.println("HelloServlet 생성자");
    }

    @Override
    public void init() {
        System.out.println("init HelloServlet");
    }

    @Override
    public void doGet(HttpRequest request, HttpResponse response) {
        System.out.println("request : wanna get hello");
    }

    @Override
    public void doPost(HttpRequest request, HttpResponse response) {
        System.out.println("request : wanna post hello");
    }
}
