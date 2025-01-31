package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.factory;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ObjectGraphComplexity {
    SINGLE(1), MEDIUM(3), HIGH(10);

    private final int entityCount;
}