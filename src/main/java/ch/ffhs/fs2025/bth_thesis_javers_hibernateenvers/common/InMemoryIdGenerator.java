package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryIdGenerator implements IdentifierGenerator {

    private final Map<String, AtomicInteger> generators = new ConcurrentHashMap<>();

    @Override
    public Object generate(SharedSessionContractImplementor session, Object object) {
        return generators
                .computeIfAbsent(object.getClass().getName(), k -> new AtomicInteger(1))
                .getAndIncrement();
    }

}
