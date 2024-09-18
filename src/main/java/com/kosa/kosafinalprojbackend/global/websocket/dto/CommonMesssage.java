package com.kosa.kosafinalprojbackend.global.websocket.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CommonMesssage {
    private Long projectId;
    private String type;
}
