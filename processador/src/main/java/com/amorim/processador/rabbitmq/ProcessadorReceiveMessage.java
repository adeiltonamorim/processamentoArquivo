package com.amorim.processador.rabbitmq;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.amorim.processador.domain.Cliente;
import com.amorim.processador.domain.ItemVenda;
import com.amorim.processador.domain.Venda;
import com.amorim.processador.domain.Vendedor;
import com.amorim.processador.message.ProcessadorMessage;
import com.amorim.processador.service.ClienteService;
import com.amorim.processador.service.FileService;
import com.amorim.processador.service.VendaService;
import com.amorim.processador.service.VendedorService;
import com.amorim.processador.util.Constantes;

@Component
public class ProcessadorReceiveMessage {
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private VendedorService vendedorService;
	
	@Autowired
	private VendaService vendaService;
	
	@Autowired
	private FileService fileService;
	
	@Value("${spring.jobs.processarArquivo.diretorioSaida}")
	private String diretorioSaida;
	
    @RabbitListener(queues = {"${processor.rabbitmq.vendas.queue}"})
    public void receive(@Payload ProcessadorMessage payload) {
        System.out.println(payload.toString());
        
		List<Vendedor> listaVendedores = new ArrayList<Vendedor>();
		List<Cliente> listaClientes = new ArrayList<Cliente>();
		List<Venda> listaVendas = new ArrayList<Venda>();
		
		Stream<String> lines = payload.getLines().stream();
		lines.
			forEach(line -> {
				if (line.startsWith(Constantes.ID_DADOS_VENDEDORES)) {
					Vendedor vendedor = processarDadosVendedores(line);
					listaVendedores.add(vendedor);	
				} else if(line.startsWith(Constantes.ID_DADOS_CLIENTES)) {
					Cliente cliente = processarDadosClientes(line);
					listaClientes.add(cliente);
				} else if(line.startsWith(Constantes.ID_DADOS_VENDAS)) {
					Venda venda = processarDadosVendas(line);
					listaVendas.add(venda);
				}
			});
		
//		O nome do arquivo deve seguir o padrão, {flat_file_name}.done.dat.
//		O conteúdo do arquivo de saída deve resumir os seguintes dados:
//		- Quantidade de clientes no arquivo de entrada
//		- Quantidade de vendedor no arquivo de entrada
//		- ID da venda mais cara
//		- O pior vendedor
		Vendedor piorVendedor = listaVendas.stream().min(Comparator.comparingDouble(Venda::getValorTotal)).get().getVendedor();
        Venda vendaMaisCara = listaVendas.stream().max(Comparator.comparingDouble(Venda::getValorTotal)).get();
        
		try {
			List<String> listaConteudo = new ArrayList<String>();
			listaConteudo.add("Quantidade de clientes: " + listaClientes.size());
			listaConteudo.add("Quantidade de vendedores: " + listaVendedores.size());
			listaConteudo.add("ID venda mais cara: " + vendaMaisCara.getId());
			listaConteudo.add("Pior vendedor: " + piorVendedor.getNome());
			
			fileService.createFile(payload.getNomeArquivo(), listaConteudo);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    private Vendedor processarDadosVendedores(String linhaDadosVendedores) {
    	String[] tokens = linhaDadosVendedores.split("ç");
    	return vendedorService.save(0L, tokens[1], tokens[2], new BigDecimal(tokens[3]));
    }
    
    private Cliente processarDadosClientes(String linhaDadosClientes) {
    	String[] tokens = linhaDadosClientes.split("ç");
		return clienteService.save(0L, tokens[1], tokens[2], tokens[3]);
    }
    
    private Venda processarDadosVendas(String linhaDadosVendas) {
    	String[] tokens = linhaDadosVendas.split("ç");

    	Vendedor vendedor = vendedorService.findByNome(tokens[3]).orElseThrow();
		Venda venda = Venda.of(0L, vendedor, Double.valueOf(0));
		
		//003çSale IDç[Item ID-Item Quantity-Item Price]çSalesman name
		List<String> dadosItens = Arrays.asList(tokens[2].replace("[", "").replace("]", "").split(","));
		for (String dadoItemVenda : dadosItens) {
			String[] itemVenda = dadoItemVenda.split("-");
			Long quantidade = Long.valueOf(itemVenda[1]);
			Double valor = Double.valueOf(itemVenda[2]);
			Double valorTotal = valor * quantidade; 
			
			ItemVenda item = ItemVenda.of(0L, venda, quantidade, valor, valorTotal);
			item.setVenda(venda);
			
			venda.addItemVenda(item);
			venda.setValorTotal( venda.getValorTotal() + item.getValorTotal() );
		}
		
		return vendaService.save(venda);
    }
}