package com.amorim.processador.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FileService {

	@Value("${spring.jobs.processarArquivo.diretorioSaida}")
	private String diretorioSaida;
	
	public void createFile(String nomeArquivo, List<String> listaConteudo) throws IOException {

		Path arquivo = Path.of(diretorioSaida + File.separatorChar + nomeArquivo + ".done.dat");
		FileWriter fileWriter = new FileWriter(arquivo.toFile());
		PrintWriter printWriter = new PrintWriter(fileWriter);
		try {
			listaConteudo.stream().forEach(conteudo -> {
					printWriter.println(conteudo);
			});
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			printWriter.close();
		}
		
	}
	
}
