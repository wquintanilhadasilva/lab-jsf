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

        InputStream inputStream = new FileInputStream("D:/tmp/teste.pdf");

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

}
