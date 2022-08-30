package projectcom.example.project.view;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

@Named
@RequestScoped
public class Bean {

	private String input;
	private String output;
	private String status;
	
	private String id = "teste";

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void submit() throws IOException {
		output = "Hello World! You have typed: " + input;
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public String getOutput() {
		return output;
	}

	public void download() throws IOException {
		
		FacesContext ctx = FacesContext.getCurrentInstance();
        ExternalContext ec = ctx.getExternalContext();
        HttpServletResponse response = (HttpServletResponse) ec.getResponse();
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=teste.pdf");
        response.setContentType("application/pdf");

		OutputStream responseOutputStream = response.getOutputStream();

        InputStream inputStream = new FileInputStream("/home/basis/Downloads/teste.pdf");

		// Lê o conteúdo do PDF e grava para saída
		byte[] bytesBuffer = new byte[2048];
		int nread = 0;
		int bytesRead;
		while ((bytesRead = inputStream.read(bytesBuffer)) > 0) {
			responseOutputStream.write(bytesBuffer, 0, bytesRead);
			nread += bytesRead;
		}
		response.setContentLength(nread);
		responseOutputStream.flush();

		// Fecha os streams
		inputStream.close();
		responseOutputStream.close();
		ctx.responseComplete();
	}
	
	public void getRemoteFile() throws IOException {
		
		FacesContext ctx = FacesContext.getCurrentInstance();
        ExternalContext ec = ctx.getExternalContext();
        HttpServletResponse response = (HttpServletResponse) ec.getResponse();
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=teste.pdf");
        response.setContentType("application/pdf");

		OutputStream responseOutputStream = response.getOutputStream();
		
		
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet("http://localhost:9532/arquivos/" + id);
        try {
            httpGet.getRequestLine();
            HttpResponse rs = httpClient.execute(httpGet);
            if (rs.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            	InputStream is = rs.getEntity().getContent();
            	byte[] bytesBuffer = new byte[2048];
        		int nread = 0;
        		int bytesRead;
        		while ((bytesRead = is.read(bytesBuffer)) > 0) {
        			responseOutputStream.write(bytesBuffer, 0, bytesRead);
        			nread += bytesRead;
        		}
        		response.setContentLength(nread);
        		responseOutputStream.flush();

        		// Fecha os streams
        		is.close();
        		responseOutputStream.close();
        		ctx.responseComplete();

            } else {
                rs.getEntity().getContent().close();
                throw new IOException(rs.getStatusLine().getReasonPhrase());
            }
            
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
	}
	
	public void consultar() {
		status = "Consultar status";
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet("http://localhost:9532/arquivos/status/" + id);
        httpGet.setHeader("Content-type", "application/json");
        try {
            httpGet.getRequestLine();
            HttpResponse response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                InputStream is = response.getEntity().getContent();
                int bufferSize = 1024;
                char[] buffer = new char[bufferSize];
                StringBuilder out = new StringBuilder();
                Reader in = new InputStreamReader(is, StandardCharsets.UTF_8);
                for (int numRead; (numRead = in.read(buffer, 0, buffer.length)) > 0; ) {
                    out.append(buffer, 0, numRead);
                }
                status = out.toString();
                in.close();
                is.close();
            } else {
                response.getEntity().getContent().close();
                status = response.getStatusLine().getReasonPhrase();
            }
            return;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        status = "ERRO";
    }

}
