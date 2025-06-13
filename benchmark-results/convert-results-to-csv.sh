#!/bin/bash

# Setup files
OUTPUT="benchmark-summary.csv"
RAW="benchmark-summary-RAW.csv"

# Write header for output files
echo "versioning,crud,object_graph_size,payload_type,total_invocations,ci_95_lower,ci_95_upper,mean,median,minimum,maximum" > "$RAW"
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
    echo "WARNING: Cannot extract parameters: $filename" >&2
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
  ' "$file" >> "$RAW"
done

# Compute 'comparison' column and write final CSV output
tail -n +2 "$RAW" | while IFS=',' read -r versioning crud graph payload invocations ci_lower ci_upper mean median minimum maximum; do
  if [[ "$versioning" == "Novers" ]]; then
    comparison=$(printf "%.2f" 100)
  else
    # Fetch Novers CI bounds
    novers_ci_lower=$(awk -F',' -v op="$crud" -v g="$graph" -v p="$payload" '$1=="Novers" && $2==op && $3==g && $4==p {print $6; exit}' "$RAW")
    novers_ci_upper=$(awk -F',' -v op="$crud" -v g="$graph" -v p="$payload" '$1=="Novers" && $2==op && $3==g && $4==p {print $7; exit}' "$RAW")

    if [[ -n "$novers_ci_lower" && -n "$novers_ci_upper" ]]; then
      # Primary case: novers_ci_lower > current ci_upper
      if [[ $(echo "$novers_ci_lower > $ci_upper" | bc -l) -eq 1 ]]; then
        comparison=$(echo "scale=4; 100 * $ci_upper / $novers_ci_lower" | bc)
      else
        # Fallback logic
        if [[ $(echo "$novers_ci_upper > $ci_lower" | bc -l) -eq 1 ]]; then
          comparison=100
        else
          comparison=$(echo "scale=4; 100 * $ci_lower / $novers_ci_upper" | bc)
        fi
      fi
      comparison=$(printf "%.2f" "$comparison")
    else
      comparison=""
    fi
  fi

  # Round numeric values to 2 decimal places
  invocations_fmt=$(printf "%.2f" "$invocations")
  ci_lower_fmt=$(printf "%.2f" "$ci_lower")
  ci_upper_fmt=$(printf "%.2f" "$ci_upper")
  mean_fmt=$(printf "%.2f" "$mean")
  median_fmt=$(printf "%.2f" "$median")
  minimum_fmt=$(printf "%.2f" "$minimum")
  maximum_fmt=$(printf "%.2f" "$maximum")

  echo "$versioning,$crud,$graph,$payload,$invocations_fmt,$ci_lower_fmt,$ci_upper_fmt,$mean_fmt,$median_fmt,$minimum_fmt,$maximum_fmt,$comparison" >> "$OUTPUT"
done
