#!/bin/bash

# Benchmarks to run
benchmark_classes=(
  "EnversCreateBenchmark"
  "JaversCreateBenchmark"
  "NoversCreateBenchmark"
)

for benchmark_class in "${benchmark_classes[@]}"; do
  echo "Running benchmark: $benchmark_class"
  mvn test -Dtest=$benchmark_class
done
