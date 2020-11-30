package com.amorim.reader.jobs;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.amorim.reader.service.ReaderFilesService;

@Component
public class JobReaderFiles {
	
	@Autowired
	private ReaderFilesService service;
	
	@Scheduled(cron = "${spring.jobs.processarArquivo.cronExpression}")
	public void readerFile() {
		
		ExecutorService executor = Executors.newFixedThreadPool(4);
		CompletableFuture.runAsync(() -> {
			try {
				service.readerFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}, executor);
	
	}
	
}
