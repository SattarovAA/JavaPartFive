package org.jpf.service.kafka;

import org.jpf.model.dto.security.RegistrationRequest;

public interface RegistrationEventService {
    void send(RegistrationRequest user);
}
