package com.amorim.reader.message;

import java.util.List;

public class ProcessorMessage {

	private String nomeArquivo;
	
	private List<String> lines;

	public String getNomeArquivo() {
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

	public List<String> getLines() {
		return lines;
	}

	public void setLines(List<String> lines) {
		this.lines = lines;
	}

}
