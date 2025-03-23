package org.jpf.model.kafka;

public record ConfirmCodeEvent(
        String email,
        String confirmCode
) {
}
