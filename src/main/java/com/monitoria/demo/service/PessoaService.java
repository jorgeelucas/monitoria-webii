package com.monitoria.demo.service;

import com.monitoria.demo.dto.PessoaDTO;
import com.monitoria.demo.entidade.DocumentoEntidade;
import com.monitoria.demo.entidade.Endereco;
import com.monitoria.demo.entidade.PessoaEntidade;
import com.monitoria.demo.repository.PessoaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PessoaService {

    private final PessoaRepository pessoaRepository;

    public PessoaService(PessoaRepository pessoaRepository) {
        this.pessoaRepository = pessoaRepository;
    }

    public List<PessoaEntidade> obterTodos() {
        return pessoaRepository.findAll();
    }

    public void salvar(PessoaDTO pessoaDTO) {
        // transformando entidade documento
        DocumentoEntidade documentoEntidade = new DocumentoEntidade();
        documentoEntidade.setCpf(pessoaDTO.getDoc());

        // transformando entidade pessoa
        PessoaEntidade entidade = new PessoaEntidade();
        entidade.setNome(pessoaDTO.getNome());
        entidade.setDocumento(documentoEntidade);

        // transformando enderecos
        List<Endereco> enderecos = pessoaDTO.getEnderecos().stream()
                .map(Endereco::new)
                .toList();

        entidade.setEnderecos(enderecos);

        pessoaRepository.save(entidade);
    }

}
