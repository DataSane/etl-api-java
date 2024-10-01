public class Municipios {

    private Integer idMunicipios;
    private String nome;
    private Integer populacaoTotal;
    private Double populacaoSemLixo;
    private Double populacaoSemAgua;
    private Double populacaoSemEsgoto;
    private Double domicilioSujeitoInundacoes;
    private String possuiPlanoMunicipal;

    public Municipios() {
    }

    public Municipios(Integer idMunicipios, String nome, Integer populacaoTotal, Double populacaoSemLixo, Double populacaoSemAgua, Double populacaoSemEsgoto, Double domicilioSujeitoInundacoes, String possuiPlanoMunicipal) {
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

    public Double getPopulacaoSemLixo() {
        return populacaoSemLixo;
    }

    public Double getPopulacaoSemAgua() {
        return populacaoSemAgua;
    }

    public Double getPopulacaoSemEsgoto() {
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

    public void setPopulacaoSemLixo(Double populacaoSemLixo) {
        this.populacaoSemLixo = populacaoSemLixo;
    }

    public void setPopulacaoSemAgua(Double populacaoSemAgua) {
        this.populacaoSemAgua = populacaoSemAgua;
    }

    public void setPopulacaoSemEsgoto(Double populacaoSemEsgoto) {
        this.populacaoSemEsgoto = populacaoSemEsgoto;
    }

    public void setDomicilioSujeitoInundacoes(Double domicilioSujeitoInundacoes) {
        this.domicilioSujeitoInundacoes = domicilioSujeitoInundacoes;
    }

    public void setPossuiPlanoMunicipal(String possuiPlanoMunicipal) {
        this.possuiPlanoMunicipal = possuiPlanoMunicipal;
    }

    //    @Override
//    public String toString() {
//        return this.nome + " (" + this.ano + ") - " + this.genero + " - " + this.diretor;
//    }
}