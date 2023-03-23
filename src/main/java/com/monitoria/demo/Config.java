package com.monitoria.demo;

import com.monitoria.demo.dto.GetNumeroAleatorio;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {


    @Bean
    public GetNumeroAleatorio getNumeroAleatorio() {
        GetNumeroAleatorio getNumeroAleatorio = new GetNumeroAleatorio(2);
        return getNumeroAleatorio;
    }


}
