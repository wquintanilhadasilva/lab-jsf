package main;

import java.util.Arrays;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;

@ManagedBean(name = "helloWord")
@SessionScoped
public class HelloWord {
	
	private List<String> values = Arrays.asList("OPÇÃO 1", "OPÇÃO 2", "OPÇÃO 3");
	
	private String value;
	

	public HelloWord() {
		System.out.println("New Hello!");
	}

	public String getMessage() {
		return "Hello Word!";
	}

	public List<String> getValues() {
		return values;
	}

	public void setValues(List<String> values) {
		this.values = values;
	}
	
	public void valueChangeMethod(ValueChangeEvent e){
		System.out.println("valueChangeMethod");
		System.out.println("Antigo: " + e.getOldValue() + "::Novo : " + e.getNewValue());
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public void submit() {
		System.out.println("Valor no submit: " + this.value);
	}

}
