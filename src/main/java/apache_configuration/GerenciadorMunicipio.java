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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GerenciadorMunicipio {

    /*
    Ao usar throws, você informa ao compilador e a outros desenvolvedores que devem estar cientes
    de que a operação pode falhar e que devem implementar um tratamento adequado
        ex: chamar o metodo na main dentro de um try catch
    .
     */
    public List<Municipio> criar() throws IOException {
        ControllerBucket appBucket = new ControllerBucket();

        List<Municipio> municipios = new ArrayList<>();

        // Recupeerando arquivo xls
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

            // Criando objetos com os valores de cada celula
            Municipio municipio = Municipio.builder()
                    .municipio(cells.get(0).getStringCellValue())
                    .estado(cells.get(1).getStringCellValue())
                    .populacao((int) cells.get(2).getNumericCellValue())
                    .planoMunicipal(cells.get(3).getStringCellValue())
                    .populacaoSemAgua(convertTypeValue(cells.get(4))) // Pegando o tipo para validar o campo que está vindo da planilha
                    .populacaoSemEsgoto(convertTypeValue(cells.get(5)))
                    .populacaoSemColetaDeLixo(convertTypeValue(cells.get(6)))
                    .domiciliosSujeitosAInundacao(convertTypeValue(cells.get(7)))
                    .build();

            // adiciona o objeto criado na lista de usuarios
            municipios.add(municipio);
        });
        return municipios;
    }

    // Trata os dados de string da planilha
    public Double convertTypeValue(Cell cell){
        switch (cell.getCellType()){
            case STRING:
                return 0.0;
            case NUMERIC:
                return cell.getNumericCellValue() * 100.0;
            default:
                throw new IllegalArgumentException("Tipo de célula não suportado: " + cell.getCellType());
        }
    }

    // Transforma a lista de Iterator em uma List
    public List<?> toList(Iterator<?> iterator){
        return IteratorUtils.toList(iterator);
    }

    public void imprimir(List<Municipio> lista){
        lista.forEach(System.out::println);

    }
}
