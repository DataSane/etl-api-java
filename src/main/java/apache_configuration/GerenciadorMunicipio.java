package apache_configuration;

import client.bucketS3.ControllerBucket;
import lombok.Cleanup;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class GerenciadorMunicipio {

    /*
    Ao usar throws, você informa ao compilador e a outros desenvolvedores que devem estar cientes
    de que a operação pode falhar e que devem implementar um tratamento adequado
        ex: chamar o metodo na main dentro de um try catch
    .
     */
    List<Municipio> municipios = new ArrayList<>();

    public void criar() throws IOException {
        ControllerBucket appBucket = new ControllerBucket();

        // Recuperando arquivo xls
        @Cleanup // Essa anotation fecha o arquivo após ser executado - usada no lugar do try catch
        InputStream file = appBucket.downloadS3();

        Workbook workbook = new HSSFWorkbook(file); // Pega a planilha

        // Indica a aba da planilha
        Sheet sheet = workbook.getSheetAt(0);

        // Percorre cada linha que tem na planilha, e adiciona em uma lista
        List<Row> rows = (List<Row>) toList(sheet.iterator());
        // Remover os cabeçalhos
        rows.remove(0);


        rows.forEach(row -> {
            // Percorre cada celular da linha e, adiciona cada celula da linha atual em uma lista
            List<Cell> cells = (List<Cell>) toList(row.cellIterator());

            String nomeMunicipio = cells.get(0).getStringCellValue();
            String estadoMunicipio = cells.get(1).getStringCellValue();
            Integer populacaoMunicipio = (int) cells.get(2).getNumericCellValue();
            String planoMunicipio = cells.get(3).getStringCellValue();
            Double populacaoSemAguaMunicipio = convertTypeValue(cells.get(4));
            Double populacaoSemEsgotoMunicipio = convertTypeValue(cells.get(5));
            Double populacaoSemColetaDeLixoMunicipio = convertTypeValue(cells.get(6));
            Double domiciliosSujeitosAInundacaoMunicipio = convertTypeValue(cells.get(7));

            // Criando objetos com o total de pessoas afetadas
            Municipio municipio = Municipio.builder()
                    .municipio(nomeMunicipio)
                    .estado(estadoMunicipio)
                    .populacao(populacaoMunicipio)
                    .planoMunicipal(planoMunicipio)
                    .populacaoSemAgua(populacaoSemAguaMunicipio)// Pegando o tipo para validar o campo que está vindo da planilha
                    .populacaoSemEsgoto(populacaoSemEsgotoMunicipio)
                    .populacaoSemColetaDeLixo(populacaoSemColetaDeLixoMunicipio)
                    .domiciliosSujeitosAInundacao(domiciliosSujeitosAInundacaoMunicipio)
                    .build();

            // adiciona o objeto criado na lista de municipios
            municipios.add(municipio);
        });
    }

    public Double mediaSemColetaDeLixo() {
        Integer populacaoTotal = 0;
        Double populacaoAfetadaSemColetaDeLixo = 0.0;
        Double populacaoAfetadaSemAgua = 0.0;
        Double populacaoAfetadaSemEsgoto = 0.0;

        for (Municipio municipio : municipios) {
            populacaoTotal += municipio.getPopulacao();
            populacaoAfetadaSemColetaDeLixo += municipio.getPopulacaoSemColetaDeLixo() * municipio.getPopulacao();
            populacaoAfetadaSemAgua += municipio.getPopulacaoSemAgua() * municipio.getPopulacao();
            populacaoAfetadaSemEsgoto += municipio.getPopulacaoSemEsgoto() * municipio.getPopulacao();
        }

        Double resultado = (populacaoTotal / populacaoAfetadaSemColetaDeLixo) * 100;

        NumberFormat format = NumberFormat.getIntegerInstance(new Locale("pt", "BR"));
        String valorFormatado = format.format(resultado);
        Double mediaSemColetaDeLixo = Double.parseDouble(valorFormatado);

        return mediaSemColetaDeLixo;
    }

    // Trata os dados de string da planilha
    public Double convertTypeValue(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return 0.0;
            case NUMERIC:
                return cell.getNumericCellValue() * 100.0;
            default:
                throw new IllegalArgumentException("Tipo de célula não suportado: " + cell.getCellType());
        }
    }

    // Transforma a lista de Iterator em uma List
    public List<?> toList(Iterator<?> iterator) {
        return IteratorUtils.toList(iterator);
    }

    public void imprimir(List<Municipio> lista) {
        lista.forEach(System.out::println);
    }
}
