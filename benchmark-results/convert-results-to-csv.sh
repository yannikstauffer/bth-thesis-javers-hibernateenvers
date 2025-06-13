#!/bin/bash

# Setup files
OUTPUT="benchmark-summary.csv"
TMP="benchmark-tmp.csv"

# Write header for temporary and final output files
echo "versioning,crud,object_graph_size,payload_type,total_invocations,ci_95_lower,ci_95_upper,mean,median,minimum,maximum" > "$TMP"
echo "versioning,crud,object_graph_size,payload_type,total_invocations,ci_95_lower,ci_95_upper,mean,median,minimum,maximum,comparison" > "$OUTPUT"

# Extract benchmark results from JSON files in selected subdirectories
find envers javers novers -type f -name '*.json' | while read -r file; do
  filename=$(basename "$file" .json)

  # Extract parameters from filename
  if [[ "$filename" =~ ^([A-Za-z]+)(Create|Read|Update|Delete)Benchmark_([A-Z]+)_([A-Z]+)$ ]]; then
    VERSIONING="${BASH_REMATCH[1]}"
    OPERATION="${BASH_REMATCH[2]}"
    GRAPH_SIZE="${BASH_REMATCH[3]}"
    PAYLOAD="${BASH_REMATCH[4]}"
  else
    echo "Cannot extract parameters: $filename" >&2
    continue
  fi

  # Convert JSON data to CSV format
  jq -r --arg version "$VERSIONING" \
        --arg operation "$OPERATION" \
        --arg graph "$GRAPH_SIZE" \
        --arg payload "$PAYLOAD" '
    .[] |
    .primaryMetric as $m |
    ($m.rawData | flatten | add) as $invocations |
    "\($version),\($operation),\($graph),\($payload),\($invocations),\($m.CI_95[0]),\($m.CI_95[1]),\($m.score),\($m.scorePercentiles."50.0"),\($m.scorePercentiles."0.0"),\($m.scorePercentiles."100.0")"
  ' "$file" >> "$TMP"
done

# Compute 'comparison' column and write final CSV output
tail -n +2 "$TMP" | while IFS=',' read -r versioning crud graph payload invocations ci_lower ci_upper mean median minimum maximum; do
  if [[ "$versioning" == "Novers" ]]; then
    comparison=100
  else
    # Lookup the ci_95_lower value from the corresponding Novers entry
    novers_ci_lower=$(awk -F',' -v op="$crud" -v g="$graph" -v p="$payload" '
      $1 == "Novers" && $2 == op && $3 == g && $4 == p { print $6; exit }
    ' "$TMP")

    if [[ -n "$novers_ci_lower" ]]; then
      # Only compute comparison if Novers lower CI is greater than current upper CI
      if [[ $(echo "$novers_ci_lower > $ci_upper" | bc -l) -eq 1 ]]; then
        comparison=$(echo "scale=2; 100 * $ci_upper / $novers_ci_lower" | bc)
      else
        comparison="N/A"
      fi
    else
      comparison="N/A"
    fi
  fi

  echo "$versioning,$crud,$graph,$payload,$invocations,$ci_lower,$ci_upper,$mean,$median,$minimum,$maximum,$comparison" >> "$OUTPUT"
done

# Clean up temporary file
rm "$TMP"

echo "IMPORTANT: Any entries where the ci_95_lower of Novers is lower than the ci_95_upper of another versioning system are not included in the comparison column."
echo "These values have been set to N/A and have to be handled manually."
