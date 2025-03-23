package org.jpf.service;

import org.jpf.model.kafka.RegistrationEvent;

public interface ConfirmService {
    void work(RegistrationEvent message);
}
