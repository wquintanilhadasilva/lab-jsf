package org.pgjma.spring.consumer.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pgjma.spring.consumer.service.implementation.ConsumerServiceImpl;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.io.InputStream;

@RestController
@RequestMapping("arquivos")
@Slf4j
@RequiredArgsConstructor
public class ConsumerApi {

    private final ConsumerServiceImpl consumerService;

    @GetMapping("/{id}")
    public ResponseEntity<Resource> download(@PathVariable String id) throws Exception {
        log.debug(String.format("Obtendo arquivo %s", id));
        try {
            InputStream inputStream = consumerService.download(id);
            org.springframework.core.io.Resource resource = new InputStreamResource(inputStream);
            String contentType = "application/octet-stream";
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + id + "\"")
                    .body(resource);
        } catch (FileNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception ex) {
            throw ex;
        }
    }

}
