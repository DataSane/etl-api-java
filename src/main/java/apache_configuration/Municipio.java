package apache_configuration;

import lombok.Builder;
import lombok.ToString;

@ToString
@Builder
public class Municipio {

    private String cidade;
    private String estado;
    private Integer populacao;
    private String planoMunicipal;
    private Double populacaoSemAgua;
    private Double populacaoSemEsgoto;
    private Double populacaoSemColetaDeLixo;
    private Double domiciliosSujeitosAInundacao;

}
