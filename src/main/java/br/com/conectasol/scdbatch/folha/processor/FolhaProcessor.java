package br.com.conectasol.scdbatch.folha.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.conectasol.scdbatch.folha.service.FolhaService;
import br.com.conectasol.scdbatch.model.Folha;

@Service
public class FolhaProcessor implements ItemProcessor<Folha, String>{

	@Autowired
	private FolhaService folhaService;
	
	@Override
	public String process(Folha item) throws Exception {
		return folhaService.toJson(item);
	}

}
