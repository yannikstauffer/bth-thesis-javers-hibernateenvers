#!/bin/bash

# Define an array of benchmark classes
benchmark_classes=(
  "EnversCreateBenchmark"
  "JaversCreateBenchmark"
  "NoversCreateBenchmark"
)

# Loop through each benchmark class and execute it with Maven
for benchmark_class in "${benchmark_classes[@]}"; do
  echo "Running benchmark: $benchmark_class"
  mvn test -Dtest=$benchmark_class
done
