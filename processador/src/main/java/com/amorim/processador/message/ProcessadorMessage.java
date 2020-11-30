package com.amorim.processador.message;

import java.util.List;

public class ProcessadorMessage {

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

	@Override
	public String toString() {
		return "ProcessadorMessage [nomeArquivo=" + nomeArquivo + ", lines=" + lines + "]";
	}

	
}