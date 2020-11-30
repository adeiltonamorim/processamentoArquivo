package com.amorim.reader.service;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.amorim.reader.message.ProcessorMessage;
import com.amorim.reader.rabbitmq.ProcessorSendMessage;

@Service
public class ReaderFilesService {

	private static final String ARQUIVO_DAT = ".dat";
	
	@Value("${spring.jobs.processarArquivo.diretorioEntrada}")
	private String diretorioEntrada;
	
	@Value("${spring.jobs.processarArquivo.diretorioProcessado}")
	private String diretorioProcessado;
	
	@Value("${spring.jobs.processarArquivo.diretorioErro}")
	private String diretorioErro;
	
	@Autowired
	private ProcessorSendMessage processorMessage;
	
	@Async
	public void readerFile() throws IOException {
		
		List<File> listaArquivos = getListaArquivos(diretorioEntrada, ARQUIVO_DAT);

		for (File file : listaArquivos) {
			try {
				// mover para a pasta processados
				Path destin = Path.of(diretorioProcessado + File.separatorChar + file.getName()); 
				Files.createDirectories(destin);
				Files.move(Paths.get(file.getPath()), destin, StandardCopyOption.REPLACE_EXISTING);
				
				Supplier<Stream<String>> lines = () -> {
					try {
						return java.nio.file.Files.lines(destin, StandardCharsets.UTF_8);
					} catch (IOException e) {
						e.printStackTrace();
					}
					return null;
				};
				
				ProcessorMessage message = new ProcessorMessage();
				message.setNomeArquivo(file.getName().substring(0, file.getName().lastIndexOf(".")));
				message.setLines(lines.get().collect(Collectors.toList()));
				processorMessage.sendMessage(message);
			} catch (Exception e) {
				Path error = Path.of(diretorioErro + File.separatorChar + file.getName()); 
				Files.createDirectories(error);
				Files.move(Paths.get(file.getPath()), error, StandardCopyOption.REPLACE_EXISTING);
				throw e;
			}
			
		}
		
	}
	
	public static List<File> getListaArquivos(String path, String filter) {
		File diretorio = new File(path);
		List<File> listaArquivos = new ArrayList<File>();
		FilenameFilter datFiles = (f, name) -> (name.endsWith(filter));
		
		for(File file : Arrays.asList(diretorio.listFiles(datFiles))) {
			if (file.isFile())
				listaArquivos.add(file);
		}
		return listaArquivos;
	}
	
}
