package org.jpf.service;

import org.jpf.model.kafka.ConfirmCodeEvent;
import org.jpf.model.entity.User;

/**
 * Default interface service for working with entity {@link User}.
 */
public interface UserService {
    User findByEmail(String email);

    User save(ConfirmCodeEvent message);
}
