package apache_configuration;

import java.io.IOException;
import java.util.List;

public class Municipio_app {
    public static void main(String[] args) {
        GerenciadorMunicipio gerenciadorMunicipio = new GerenciadorMunicipio();

        try {
            List<Municipio> municipios = gerenciadorMunicipio.criar();
            gerenciadorMunicipio.imprimir(municipios);
        }catch (IOException ex){
            System.out.println("ERRO:" + ex.getMessage());
        }
    }
}
