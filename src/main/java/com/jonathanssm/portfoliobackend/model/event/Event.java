package com.jonathanssm.portfoliobackend.model.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    private String eventKey;
    private Object payload;
    private Instant timestamp;

}
