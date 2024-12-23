package apache_configuration;

import client.bucketS3.ControllerBucket;
import logs_config.LogHandler;
import logs_config.LogSlack;
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
    List<Municipio> municipios = new ArrayList<>();

    LogSlack slack = new LogSlack();
    LogHandler terminal = new LogHandler();

    public List<Municipio> getMunicipios() {
        return municipios;
    }

    public void criar() {
        try {
            ControllerBucket appBucket = new ControllerBucket();

            // Recuperando arquivo xls
            @Cleanup // Essa anotation fecha o arquivo após ser executado - usada no lugar do try catch
            InputStream file = appBucket.downloadS3();

            terminal.setLog(3, "Downloading CSV file from S3 Bucket", GerenciadorMunicipio.class.getName());
            slack.setLog(3, "Downloading CSV file from S3 Bucket", GerenciadorMunicipio.class.getName());

            Workbook workbook = new HSSFWorkbook(file); // Pega a planilha

            terminal.setLog(3, "Reading CSV file ", GerenciadorMunicipio.class.getName());
            slack.setLog(3, "Reading CSV file ", GerenciadorMunicipio.class.getName());

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

                // Criando objetos
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


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public Double calculateAverage(String areaSaneamentoBasico, String tipoMunicipio) {
        Integer populacaoTotal = 0;
        Double porcentagemArea = 0.0;
        Double populacaoAfetada = 0.0;
        Boolean applyValue = false;

        for (Municipio municipio : municipios) {
            switch (areaSaneamentoBasico) {
                case "populacaoSemColetaDeLixo":
                    if (tipoMunicipio.equals("geral")) {
                        porcentagemArea = municipio.getPopulacaoSemColetaDeLixo();
                        applyValue = true;
                        break;
                    } else if (tipoMunicipio.equals("pequeno")) {
                        if (municipio.getPopulacao() <= 50_000) {
                            porcentagemArea = municipio.getPopulacaoSemColetaDeLixo();
                            applyValue = true;
                            break;
                        }
                        break;
                    } else if (tipoMunicipio.equals("medio")) {
                        if (municipio.getPopulacao() >= 50_001 && municipio.getPopulacao() <= 200_000) {
                            porcentagemArea = municipio.getPopulacaoSemColetaDeLixo();
                            applyValue = true;
                            break;
                        }
                        break;
                    } else if (tipoMunicipio.equals("grande")) {
                        if (municipio.getPopulacao() >= 200_001) {
                            porcentagemArea = municipio.getPopulacaoSemColetaDeLixo();
                            applyValue = true;
                            break;
                        }
                        break;
                    }
                case "populacaoSemAgua":
                    if (tipoMunicipio.equals("geral")) {
                        porcentagemArea = municipio.getPopulacaoSemAgua();
                        applyValue = true;
                        break;
                    } else if (tipoMunicipio.equals("pequeno")) {
                        if (municipio.getPopulacao() <= 50_000) {
                            porcentagemArea = municipio.getPopulacaoSemAgua();
                            applyValue = true;
                            break;
                        }
                        break;
                    } else if (tipoMunicipio.equals("medio")) {
                        if (municipio.getPopulacao() >= 50_001 && municipio.getPopulacao() <= 200_000) {
                            porcentagemArea = municipio.getPopulacaoSemAgua();
                            applyValue = true;
                            break;
                        }
                        break;
                    } else if (tipoMunicipio.equals("grande")) {
                        if (municipio.getPopulacao() >= 200_001) {
                            porcentagemArea = municipio.getPopulacaoSemAgua();
                            applyValue = true;
                            break;
                        }
                        break;
                    }
                case "populacaoSemEsgoto":
                    if (tipoMunicipio.equals("geral")) {
                        porcentagemArea = municipio.getPopulacaoSemEsgoto();
                        applyValue = true;
                        break;
                    } else if (tipoMunicipio.equals("pequeno")) {
                        if (municipio.getPopulacao() <= 50_000) {
                            porcentagemArea = municipio.getPopulacaoSemEsgoto();
                            applyValue = true;
                            break;
                        }
                        break;
                    } else if (tipoMunicipio.equals("medio")) {
                        if (municipio.getPopulacao() >= 50_001 && municipio.getPopulacao() <= 200_000) {
                            porcentagemArea = municipio.getPopulacaoSemEsgoto();
                            applyValue = true;
                            break;
                        }
                        break;
                    } else if (tipoMunicipio.equals("grande")) {
                        if (municipio.getPopulacao() >= 200_001) {
                            porcentagemArea = municipio.getPopulacaoSemEsgoto();
                            applyValue = true;
                            break;
                        }
                        break;
                    }
            }

            populacaoTotal += applyValue ? municipio.getPopulacao() : 0;
            populacaoAfetada += applyValue ? porcentagemArea * municipio.getPopulacao() : 0.0;

            applyValue = false;
        }

        return (populacaoTotal / populacaoAfetada) * 100;
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
}
