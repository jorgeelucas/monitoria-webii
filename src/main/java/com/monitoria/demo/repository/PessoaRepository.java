package com.monitoria.demo.repository;

import com.monitoria.demo.entidade.PessoaEntidade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PessoaRepository extends JpaRepository<PessoaEntidade, Long> {
}
