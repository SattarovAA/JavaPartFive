package org.jpf.service.kafka;

import org.jpf.model.kafka.ConfirmCodeEvent;

public interface ConfirmCodeEventService {
    void send(ConfirmCodeEvent confirmCode);
}
