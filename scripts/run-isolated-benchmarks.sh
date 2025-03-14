#!/bin/bash

CGROUP_NAME="thesis-benchmark"

echo $$ | sudo tee /sys/fs/cgroup/$CGROUP_NAME/cgroup.procs

bash ./run-dev-benchmarks.sh
