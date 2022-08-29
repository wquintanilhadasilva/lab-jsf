package org.pgjma.spring.consumer.service.implementation;


//import br.com.oaks.ICPBravo.cms.CMSSignedData;

import lombok.extern.log4j.Log4j2;
import org.pgjma.spring.consumer.domain.enums.StatusEnum;
import org.pgjma.spring.consumer.domain.utils.PdfUtil;
import org.pgjma.spring.consumer.dto.StatusResponse;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
//@Transactional
@Log4j2
public class ConsumerServiceImpl {

    public StatusResponse obterStatusProcessamento(String id) throws IOException {
        String fileName = criarNomeArquivoStatus(id);
        Path path = Paths.get(fileName);
        StatusEnum status = StatusEnum.NAO_ENCONTRADO;
        if (Files.exists(path)) {
            String line = Files.readAllLines(path).get(0);
            if (line != null) {
                status = StatusEnum.valueOf(line);
            }
        }
        return new StatusResponse(id, status);
    }

    public InputStream download(String id) throws IOException {
        String fileName = id + ".pdf";
        checkFile(fileName);
        try {
            StatusResponse statusResponse = this.obterStatusProcessamento(id);
            if (statusResponse.getStatusEnum() == StatusEnum.CONCLUIDO) {
                return this.loadFileAsResource(id);
            } else {
                throw new FileNotFoundException("File not found: " + fileName);
            }
        } finally {
            this.deleteFile(id + ".pdf");
            this.deleteFile(id + ".status");
        }
    }

    private void checkFile(String id) throws FileNotFoundException {
        String fileName = PdfUtil.MPE_TEMP_DIR + "/" + id;
        Path filePath = Paths.get(fileName);
        if (!Files.exists(filePath)) {
            throw new FileNotFoundException("File not found: " + id);
        }
    }

    private void deleteFile(String id) throws IOException {
        try {
            checkFile(id);
            String fileName = PdfUtil.MPE_TEMP_DIR + "/" + id;
            Path filePath = Paths.get(fileName);
//            Files.delete(filePath);
        } catch (FileNotFoundException e) {
            log.debug(e);
        }
    }

    private InputStream loadFileAsResource(String id) throws FileNotFoundException {
        String fileName = PdfUtil.MPE_TEMP_DIR + "/" + id + ".pdf";
        try {
//            Path filePath = Paths.get(fileName);
            InputStream file = new FileInputStream(fileName);
            return file;
//            byte[] data = Files.readAllBytes(filePath.toAbsolutePath());
//            Resource resource = new ByteArrayResource(data);
//            return resource;
        } catch (IOException e) {
            throw new FileNotFoundException("File not found: " + fileName);
        }
    }

    private String criarNomeArquivoStatus(String id) {
        return PdfUtil.MPE_TEMP_DIR + "/" + id + ".status";
    }

    private void atualizarStatus(String idProcessamento, StatusEnum status) throws IOException {
        String fileName = criarNomeArquivoStatus(idProcessamento);
        try {
            Files.write(Paths.get(fileName), status.name().getBytes());
        } catch (Exception e) {
            log.error("Error ao gravar o log do arquivo " + fileName, e);
            throw e;
        }
    }

}
