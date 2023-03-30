# Spring Security I


Para iniciar com o _Spring security_ devemos simplesmente adicionar a dependencia do do módulo spring-security

com `GRADLE`:

```groovy
dependency {

  implementation 'org.springframework.boot:spring-boot-starter-security'
  
  // para os testes
  testImplementation 'org.springframework.security:spring-security-test'
}
```


Com `MAVEN`

```xml

<dependencies>
  
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
  </dependency>
  
  //para os testes
  <dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-test</artifactId>
    <scope>test</scope>
  </dependency>
  
</dependencies>

```



Após adicionar a devida dependencia o próprio spring já ativa a segurança, porém ele ativa o mais básico possível, o `HTTP BASIC` e não conseguimos mais fazer nossas chamadas sem uma autorização.

```json
{
    "error": "Unauthorized",
    "path": "/students",
    "status": 401,
    "timestamp": "2022-03-01T04:30:54.731+00:00"
}

```

Porém como ainda não temos nenhum usuário cadastrado, o spring gera um usuário inicial pra gente de _username_ `user` e uma senha temporária, que vai ser mostrada no console da aplicação. Algo como:

```sh

Using generated security password: 8ff527e7-0464-4443-98ae-dca68620b7db

This generated password is for development use only. Your security configuration must be updated before running your application in production.

```

Esse valor é a senha do nosso _user_; `8ff527e7-0464-4443-98ae-dca68620b7db`.

### http basic

O _http basic_ não é um dos melhores métodos de segurança, é uma forma básica de ter alguma segurança. Ele Serve para quando queremos criar algo rápido e seguro como um MVP ou uma POC (prova de conceito) pra validar nossa ideia, sem ter que configurar toda uma segurança e estratégia para a nossa aplicação.

Ele usa os dados `encodados`, ou seja, diferente de criptografados. O _encode_ é só uma estratégia para transformar um texto para poder ser lido por outro sistema, os algoritmos de _encode_ são publicos então qualquer coisa _encodada_ pode, facilmente, ser _desencodada_.

Um desses algoritmos é o base 64, nesse site, [base64encoder](https://www.base64encode.org/), podemos encodar facilmente nossa string para o base64.

Para autenticação, o _http basic_ espera os valores de usuário e senha encodados em _base64_. O formato esperado, pelo spring, é `usuario:senha`.

### enviando credenciais para autorização

Agora se quisermos fazer uma chamada, precisamos enviar as nossas credenciais no `HEADER` da requisição com a chave `Authorization`.

![image](https://user-images.githubusercontent.com/21045123/199312096-c7a0583c-f93e-43bb-9cb8-14c9e17d4e24.png)


### Customizando a segurança

para podermos customizar o _spring security_ da nossa maneira começaremos com uma classe `SegurancaConfig.java` no pacote `config`;

```java
public class SegurancaConfig {
}
```

Devemos anotar essa classe com a anotação `@Configuration` do _spring_, para informar que ela agora é uma classe de configuração;

```java
import org.springframework.context.annotation.Configuration;

@Configuration
public class SegurancaConfig {
}
```

e também a anotação `@EnableWebSecurity` também para informar para o spring ativar a segurança web, e que essa classe vai controlar.

```java
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class SegurancaConfig {
}
```

Agora é só criar um `Bean` que retorna `SecurityFilterChain` para o _spring_ usar;

```java
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class SegurancaConfig {

    @Bean
    public SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable()) // desabilita o csrf
                .authorizeRequests(auth -> { // configura cada request
                    // permite todos do / sem autorizacao
                    auth.antMatchers("/").permitAll();
                    // espera que todas as chamadas para /students tenham tenham a seja autorizado e tenha a role STUDENT
                    auth.antMatchers("/students").hasRole("ROLE_STUDENT");
                    // espera que todas as chamadas para /students tenham tenham a seja autorizado e tenha a role TEACHER
                    auth.antMatchers("/teachers").hasRole("ROLE_TEACHER");
                })
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }
}
```


### Configurando um usuário em memória

Um problema dessa abordagem é que essa senha é temporária, ou seja, toda vez que reiniciarmos nossa aplicação teremos que encodar o token novamente com a nova senha gerada, e isso pode ser um trabalho.

Podemos então já começar a configurar nossos usuário, a priori, em memória mesmo.

Para isso, na classe de configuração definimos os usuários da seguinte forma:

```java
@Bean
    public InMemoryUserDetailsManager userDetailsManager() {
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("pass")
                .roles("USER")
                .build();

        UserDetails student = User.withDefaultPasswordEncoder()
                .username("student")
                .password("pass")
                .roles("STUDENT")
                .build();

        UserDetails teacher = User.withDefaultPasswordEncoder()
                .username("teacher")
                .password("pass")
                .roles("TEACHER")
                .build();


        return new InMemoryUserDetailsManager(user, student, teacher);
    }
```
