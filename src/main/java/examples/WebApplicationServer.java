package examples;

import examples.servlet.DefaultServlet;
import examples.servlet.Servlet;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class WebApplicationServer implements Runnable {
    private int port;
    private DefaultServlet defaultServlet;
    private Map<String, RequestMapping> map; // path, RequestMapping

    public WebApplicationServer(int port) {
        this.port = port;
        this.defaultServlet = new DefaultServlet();

        initServlet(); // map 초기화
    }

    private void initServlet() {
        // servlet.properties에서 정보를 읽어들여 map을 초기화 한다
        map = new HashMap<>();
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream propstream = classLoader.getResourceAsStream("servlet.properties");

        Properties prop = new Properties();

        try {
            prop.load(propstream);

            Set<String> set = prop.stringPropertyNames();

            for(String path : set){
                String className = prop.getProperty(path);
                RequestMapping requestMapping = new RequestMapping();

                Class clazz = Class.forName(className);
                Servlet s = (Servlet) clazz.newInstance();

                requestMapping.setPath(path);
                requestMapping.setServletClassName(className);
                requestMapping.setServlet(s);

                s.init();
                map.put(path, requestMapping);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                propstream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    public void run() {
        ServerSocket listener = null;
        try {
            listener = new ServerSocket(port);

            while (true) {
                Socket client = listener.accept();
                new Thread(()->{
                    try {
                        handleSocket(client);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                listener.close();
            }catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void handleSocket(Socket client) throws IOException {
        // 여기선 받은 socket을 처리handling해야지...
        // HttpRequest, HttpResponse 객체 생성
        // 해당 서블릿 실행하고 응답보내기

        OutputStream out = client.getOutputStream();
        PrintWriter pw = new PrintWriter(out);
        HttpResponse response = new HttpResponse();
        response.setOut(out);
        response.setPw(pw);

        InputStream in = client.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        HttpRequest request = new HttpRequest();

        String line = "";
        line = br.readLine();
        String[] firstline = line.split(" ");
        request.setMethod(firstline[0]);
        request.setPath(firstline[1]);

        while ((line = br.readLine()) != null){
            if("".equals(line)){
                break;
            }
            String[] headerArray = line.split(" ");
            if(headerArray[0].startsWith("Host:")){
                request.setHost(headerArray[1].trim());
            }else if(headerArray[0].startsWith("Content-Length:")){
                request.setContentLength(Integer.parseInt(headerArray[1].trim()));
            }else if(headerArray[0].startsWith("Content-Type:")){
                request.setContentType(headerArray[1].trim());
            }else if(headerArray[0].startsWith("User-Agent:")){
                request.setUserAgent(headerArray[1].trim());
            }
        }
        System.out.println(request);

        // 사용자가 요청한 path에 mapping된 서블릿 존재하면 그거 실행
        // 아니면 default servlet 실행

        if(map.containsKey(request.getPath())){
            RequestMapping mapping = map.get(request.getPath());
            Servlet servlet = mapping.getServlet();
            servlet.service(request, response);
        }else {
            defaultServlet.service(request, response);
        }
        out.close();
        in.close();
        client.close();
    }
}
