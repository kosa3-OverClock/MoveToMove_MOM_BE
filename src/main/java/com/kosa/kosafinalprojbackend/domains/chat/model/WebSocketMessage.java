package com.kosa.kosafinalprojbackend.domains.chat.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WebSocketMessage {

    private String content; // 메시지의 내용
    private String recipient; // 수신자
}
