package com.monitoria.demo.dto;

import lombok.Data;

import java.util.List;

@Data
public class PessoaDTO {

    private String nome;
    private String doc;
    private List<String> enderecos;

}
