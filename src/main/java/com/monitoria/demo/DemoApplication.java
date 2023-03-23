package com.monitoria.demo;

import com.monitoria.demo.dto.GetNumeroAleatorio;
import com.monitoria.demo.dto.PessoaDTO;
import com.monitoria.demo.entidade.PessoaEntidade;
import com.monitoria.demo.service.PessoaService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SpringBootApplication
@RestController
public class DemoApplication {

	private final PessoaService pessoaService;
	private final GetNumeroAleatorio getNumeroAleatorio;

	public DemoApplication(PessoaService pessoaService, GetNumeroAleatorio getNumeroAleatorio) {
		this.pessoaService = pessoaService;
		this.getNumeroAleatorio = getNumeroAleatorio;
	}

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@GetMapping("/pessoas")
	public List<PessoaEntidade> teste() {
		List<PessoaEntidade> pessoas = pessoaService.obterTodos();
		return pessoas;
	}

	@PostMapping("/test")
	public String testePost(@RequestBody PessoaDTO pessoaDTO) {
		System.out.println("Get numero aleatorio: " + getNumeroAleatorio.getNumeroAleatorio());
		System.out.println("Recebendo entidade pessoa: " + pessoaDTO);
		pessoaService.salvar(pessoaDTO);
		return "Tudo funcionando POST!!!";
	}


}
