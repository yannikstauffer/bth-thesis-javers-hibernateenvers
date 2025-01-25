package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.factory;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StructureComplexity {
    SINGLE(1), MEDIUM(5), HIGH(50);

    private final int entityCount;
}