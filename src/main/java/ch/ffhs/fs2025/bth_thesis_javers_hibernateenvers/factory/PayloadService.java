package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.factory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;

@Service
class PayloadService {

    @Value("${thesis.payload.size:200}")
    private int payloadSize;

    private final Random random = new Random(2025);

    public String name(ObjectGraphComplexity objectGraphComplexity, PayloadType payloadType) {
        return objectGraphComplexity.name() + "_" + payloadType.name();
    }

    public String content() {
        return this.randomUuid().toString();
    }

    public byte[] attachment() {
        byte[] attachment = new byte[payloadSize];
        random.nextBytes(attachment);
        return attachment;
    }

    /**
     * @see java.util.UUID#randomUUID()
     */
    private UUID randomUuid() {
        byte[] randomBytes = new byte[16];
        random.nextBytes(randomBytes);
        randomBytes[6]  &= 0x0f;  /* clear version        */
        randomBytes[6]  |= 0x40;  /* set to version 4     */
        randomBytes[8]  &= 0x3f;  /* clear variant        */
        randomBytes[8]  |= (byte) 0x80;  /* set to IETF variant  */
        return uuidFrom(randomBytes);
    }

    /**
     * @see java.util.UUID#UUID(byte[])
     */
    private UUID uuidFrom(byte[] data) {
        long msb = 0;
        long lsb = 0;
        assert data.length == 16 : "data must be 16 bytes in length";
        for (int i=0; i<8; i++)
            msb = (msb << 8) | (data[i] & 0xff);
        for (int i=8; i<16; i++)
            lsb = (lsb << 8) | (data[i] & 0xff);
        return new UUID(msb, lsb);
    }
}
