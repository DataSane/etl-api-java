package client.bucketS3;

import logs_config.LogHandler;
import logs_config.LogSlack;
import logs_config.Main;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;


import java.io.InputStream;
import java.util.List;

public class ControllerBucket {
    // Instanciando o cliente S3 via S3Provider
    S3Client s3Client;
    String bucketName = "s3-challenge-lab";

    public InputStream downloadS3() {
        // *************************************
        // *   Fazendo download de arquivos    *
        // *************************************
        InputStream inputStream = null;
        s3Client = new ConnectionBucket().getS3Client();

        LogSlack slack = new LogSlack();
        LogHandler terminal = new LogHandler();

        terminal.setLog(3, "Connecting S3 Bucket...", ControllerBucket.class.getName());
        slack.setLog(3, "Connecting S3 Bucket...", ControllerBucket.class.getName());

        try {
            List<S3Object> objects = s3Client.listObjects(ListObjectsRequest.builder().bucket(bucketName).build()).contents();
            for (S3Object object : objects) {
                if (object.key().endsWith(".xls")) {
                    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                            .bucket(bucketName)
                            .key(object.key())
                            .build();

                    // Arquivo que o apache poi vai ler
                    inputStream = s3Client.getObject(getObjectRequest, ResponseTransformer.toInputStream());
                    terminal.setLog(3, "Getting CSV file from S3 Bucket", ControllerBucket.class.getName());
                    slack.setLog(3, "Getting CSV file from S3 Bucket", ControllerBucket.class.getName());

                    return inputStream;
                }
            }
        } catch (S3Exception e) {
            System.err.println("Erro ao fazer download dos arquivos: " + e.getMessage());
        }

        return inputStream;
    }
}