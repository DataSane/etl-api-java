package apache_configuration;

import lombok.*;

@ToString
@Getter
@Setter
@Builder
public class Municipio {
    private String municipio;
    private String estado;
    private Integer populacao;
    private String planoMunicipal;
    private Double populacaoSemAgua;
    private Double populacaoSemEsgoto;
    private Double populacaoSemColetaDeLixo;
    private Double domiciliosSujeitosAInundacao;
}


