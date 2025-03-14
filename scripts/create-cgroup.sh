#!/bin/bash

CPU_LIMIT=400
RAM_LIMIT_GB=6
THREAD_LIMIT=1000
CGROUP_NAME="thesis-benchmark"


if ! dpkg -l | grep -q cgroup-tools; then
    apt-get update
    apt-get install -y cgroup-tools
fi

sudo mkdir -p /sys/fs/cgroup/$CGROUP_NAME

echo "Applying resource limits..."
echo "CPU Limit: $CPU_LIMIT%"
echo "$((CPU_LIMIT * 1000)) 100000" | sudo tee "/sys/fs/cgroup/$CGROUP_NAME/cpu.max"
echo "RAM Limit: $RAM_LIMIT_GB GB"
echo "$RAM_LIMIT_GB""G" | sudo tee /sys/fs/cgroup/$CGROUP_NAME/memory.max
echo "Thread Limit: $THREAD_LIMIT"
echo "$THREAD_LIMIT" | sudo tee /sys/fs/cgroup/$CGROUP_NAME/pids.max
