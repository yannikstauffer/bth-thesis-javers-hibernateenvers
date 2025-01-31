package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.factory;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
class PayloadService {

    public String name(ObjectGraphComplexity objectGraphComplexity, PayloadType payloadType) {
        return objectGraphComplexity.name() + "_" + payloadType.name();
    }

    public String content() {
        return UUID.randomUUID().toString();
    }

    public byte[] attachment() {
        return new byte[1000];
    }
}