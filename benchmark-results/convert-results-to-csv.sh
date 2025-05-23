#!/bin/bash

OUTPUT="benchmark_summary.csv"
echo "versioning,crud,object_graph_size,payload_type,total_invocations,ci_95_upper,ci_95_lower,mean,median,minimum,maximum" > "$OUTPUT"

find . -type f -name '*.json' | while read -r file; do
  filename=$(basename "$file" .json)

  # Extract parameters from filename
  if [[ "$filename" =~ ^([A-Za-z]+)(Create|Read|Update|Delete)Benchmark_([A-Z]+)_([A-Z]+)$ ]]; then
    VERSIONIERUNG="${BASH_REMATCH[1]}"
    OPERATION="${BASH_REMATCH[2]}"
    GRAPH_SIZE="${BASH_REMATCH[3]}"
    PAYLOAD="${BASH_REMATCH[4]}"
  else
    echo "Cannot extract parameters: $filename" >&2
    continue
  fi

  # Convert parameters and json data to csv
  jq -r --arg version "$VERSIONIERUNG" \
        --arg operation "$OPERATION" \
        --arg graph "$GRAPH_SIZE" \
        --arg payload "$PAYLOAD" '
    .[] |
    .primaryMetric as $m |
    # Anzahl Invocations über alle Forks summieren
    ($m.rawData | flatten | add) as $invocations |
    "\($version),\($operation),\($graph),\($payload),\($invocations),\($m.CI_95[0]),\($m.CI_95[1]),\($m.score),\($m.scorePercentiles."50.0"),\($m.scorePercentiles."0.0"),\($m.scorePercentiles."100.0")"
  ' "$file" >> "$OUTPUT"
done
