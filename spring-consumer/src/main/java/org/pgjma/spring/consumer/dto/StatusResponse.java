package org.pgjma.spring.consumer.dto;


import lombok.Getter;
import lombok.Setter;
import org.pgjma.spring.consumer.domain.enums.StatusEnum;

@Getter
@Setter
public class StatusResponse {

    private String idProcessamento;
    private StatusEnum statusEnum;

    public StatusResponse(String idProcessamento, StatusEnum statusEnum) {
        this.idProcessamento = idProcessamento;
        this.statusEnum = statusEnum;
    }

}
