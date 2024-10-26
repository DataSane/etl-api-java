package client.clientMySQL;

public class MunicipiosTratadaSelectModel {
    private Integer idMunicipios;
    private String nome;
    private Integer populacaoTotal;
    private Integer populacaoSemLixo;
    private Integer populacaoSemAgua;
    private Integer populacaoSemEsgoto;
    private Double domicilioSujeitoInundacoes;
    private String possuiPlanoMunicipal;

    public MunicipiosTratadaSelectModel() {
    }

    public MunicipiosTratadaSelectModel(Integer idMunicipios, String nome, Integer populacaoTotal, Integer populacaoSemLixo, Integer populacaoSemAgua, Integer populacaoSemEsgoto, Double domicilioSujeitoInundacoes, String possuiPlanoMunicipal) {
        this.idMunicipios = idMunicipios;
        this.nome = nome;
        this.populacaoTotal = populacaoTotal;
        this.populacaoSemLixo = populacaoSemLixo;
        this.populacaoSemAgua = populacaoSemAgua;
        this.populacaoSemEsgoto = populacaoSemEsgoto;
        this.domicilioSujeitoInundacoes = domicilioSujeitoInundacoes;
        this.possuiPlanoMunicipal = possuiPlanoMunicipal;
    }

    public Integer getIdMunicipios() {
        return idMunicipios;
    }

    public String getNome() {
        return nome;
    }

    public Integer getPopulacaoTotal() {
        return populacaoTotal;
    }

    public Integer getPopulacaoSemLixo() {
        return populacaoSemLixo;
    }

    public Integer getPopulacaoSemAgua() {
        return populacaoSemAgua;
    }

    public Integer getPopulacaoSemEsgoto() {
        return populacaoSemEsgoto;
    }

    public Double getDomicilioSujeitoInundacoes() {
        return domicilioSujeitoInundacoes;
    }

    public String getPossuiPlanoMunicipal() {
        return possuiPlanoMunicipal;
    }

    public void setId(Integer id) {
        this.idMunicipios = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setPopulacaoTotal(Integer populacaoTotal) {
        this.populacaoTotal = populacaoTotal;
    }

    public void setPopulacaoSemLixo(Integer populacaoSemLixo) {
        this.populacaoSemLixo = populacaoSemLixo;
    }

    public void setPopulacaoSemAgua(Integer populacaoSemAgua) {
        this.populacaoSemAgua = populacaoSemAgua;
    }

    public void setPopulacaoSemEsgoto(Integer populacaoSemEsgoto) {
        this.populacaoSemEsgoto = populacaoSemEsgoto;
    }

    public void setDomicilioSujeitoInundacoes(Double domicilioSujeitoInundacoes) {
        this.domicilioSujeitoInundacoes = domicilioSujeitoInundacoes;
    }

    public void setPossuiPlanoMunicipal(String possuiPlanoMunicipal) {
        this.possuiPlanoMunicipal = possuiPlanoMunicipal;
    }
}
