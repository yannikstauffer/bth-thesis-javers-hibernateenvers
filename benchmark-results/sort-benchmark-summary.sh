#!/bin/bash

INPUT="${1:-benchmark-summary.csv}"
OUTPUT_SORTED="benchmark-summary-sorted.tmp"

if [[ ! -f "$INPUT" ]]; then
  echo "ERROR: Input file '$INPUT' not found." >&2
  exit 1
fi

# Prepare temp file with sorting keys
TMP_WITH_KEY=$(mktemp)
TMP_SORTED=$(mktemp)

# Read header and write it to output
head -n 1 "$INPUT" > "$OUTPUT_SORTED"

# Add sort key to each data line
tail -n +2 "$INPUT" | while IFS=',' read -r crud graph payload versioning invocations mean median minimum maximum ci_lower ci_upper comparison; do
  # Define sort priority values
  case "$crud" in
    Create) crud_order=1 ;;
    Read) crud_order=2 ;;
    Update) crud_order=3 ;;
    Delete) crud_order=4 ;;
  esac
  case "$graph" in
    SINGLE) graph_order=1 ;;
    MEDIUM) graph_order=2 ;;
    HIGH) graph_order=3 ;;
  esac
  case "$payload" in
    BASIC) payload_order=1 ;;
    EXTENDED) payload_order=2 ;;
  esac
  case "$versioning" in
    Novers) versioning_order=1 ;;
    Envers) versioning_order=2 ;;
    Javers) versioning_order=3 ;;
  esac

  # Generate sortable key
  sortkey="${crud_order}${graph_order}${payload_order}${versioning_order}"

  # Write to temp file with key
  echo "$sortkey,$crud,$graph,$payload,$versioning,$invocations,$mean,$median,$minimum,$maximum,$ci_lower,$ci_upper,$comparison" >> "$TMP_WITH_KEY"
done

# Sort by sortkey, remove it, and write to output
sort -t',' -k1,1 "$TMP_WITH_KEY" | cut -d',' -f2- >> "$OUTPUT_SORTED"

# Clean up
rm "$TMP_WITH_KEY" "$TMP_SORTED"

mv -f "$OUTPUT_SORTED" "$INPUT"

echo "✔ Sorted CSV written to: $INPUT"
