package examples.servlet;

import examples.HttpRequest;
import examples.HttpResponse;

import java.io.*;

public class DefaultServlet extends Servlet {
    @Override
    public void service(HttpRequest request, HttpResponse response) {
        String baseDir = "/tmp/wasroot";
        String fileName = request.getPath();
        if("/".equals(fileName)){
            fileName = "/index.html";
        }
        fileName = baseDir + fileName;

        String contentType = "text/html; charset=UTF-8";
        if(fileName.endsWith(".png")){
            contentType = "image/png";
        }

        File file = new File(fileName);

        OutputStream out = response.getOut();
        PrintWriter pw = response.getPw();

        if(!file.exists()){
            fileName = baseDir + "/404.html";
            file = new File(fileName);
            pw.println("HTTP/1.1 404 Not Found");
            pw.println("Content-Type: text/html");
        }else{
            pw.println("HTTP/1.1 200 OK");
            pw.println("Content-Type: "+ contentType);
        }
        long fileLength = file.length();
        pw.println("Content-Length: " + fileLength);
        pw.println();
        pw.flush();

        try {
            FileInputStream fis = new FileInputStream(fileName);
            byte[] buffer = new byte[1024];
            int readCount = 0;
            while ((readCount = fis.read(buffer)) != -1){
                out.write(buffer, 0, readCount);
            }
            out.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() {
        System.out.println("default servlet destroy!!!");
    }
}
