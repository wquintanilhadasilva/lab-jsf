package org.pgjma.spring.consumer.service;

import org.springframework.core.io.Resource;

import java.io.IOException;


public interface ConsumerService {
    Resource download(String id) throws IOException;
}
