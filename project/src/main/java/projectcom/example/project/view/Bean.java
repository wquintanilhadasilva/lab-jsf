package projectcom.example.project.view;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
		
		String id = "teste";
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

}
