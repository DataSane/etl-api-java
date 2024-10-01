public class Contato {
    private Integer idContato;
    private String email;
    private String assunto;
    private String mensagem;

    public Contato() {
    }

    public Contato(Integer idContato, String email, String assunto, String mensagem) {
        this.idContato = idContato;
        this.email = email;
        this.assunto = assunto;
        this.mensagem = mensagem;
    }

    public Integer getIdContato() {
        return idContato;
    }

    public String getEmail() {
        return email;
    }

    public String getAssunto() {
        return assunto;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setIdContato(Integer idContato) {
        this.idContato = idContato;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
