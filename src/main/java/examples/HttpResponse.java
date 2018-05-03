package examples;

import java.io.OutputStream;
import java.io.PrintWriter;

public class HttpResponse {
    private OutputStream out;
    private PrintWriter pw;
    private String status;
    private String statusNumber;
    private String contentType;
    private String contentLength;

    public OutputStream getOut() {
        return out;
    }

    public void setOut(OutputStream out) {
        this.out = out;
    }

    public PrintWriter getPw() {
        return pw;
    }

    public void setPw(PrintWriter pw) {
        this.pw = pw;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusNumber() {
        return statusNumber;
    }

    public void setStatusNumber(String statusNumber) {
        this.statusNumber = statusNumber;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentLength() {
        return contentLength;
    }

    public void setContentLength(String contentLength) {
        this.contentLength = contentLength;
    }
}
