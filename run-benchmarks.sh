#!/bin/bash

max_retries=10
count=0

until mvn test -Dtest="JmhBenchmarkRunner" || [ $count -eq $max_retries ]; do
  count=$((count + 1))
  echo ""
  echo "######################################################################"
  echo "### Benchmarks failed. Attempt $count/$max_retries. Retrying... ###"
  echo "######################################################################"
  echo ""
done

if [ $count -eq $max_retries ]; then
  echo "Reached maximum retry limit of $max_retries. Exiting."
  exit 1
else
  echo "Benchmarks completed successfully."
fi
