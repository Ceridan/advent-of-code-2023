#!/usr/bin/env bash

# This script generates data for the next day: code file, test file and download input.
#
# Usage:
# ./next-day-gen.sh 01

# Read args
if [ $# -eq 0 ]; then
    echo "You must provide day number"
    exit 1
fi

DAY=$1
ROOT_DIR=$(cd -- "$(dirname "$0")/.." >/dev/null 2>&1 || exit ; pwd -P)

# Create code file
code_filename="${ROOT_DIR}/src/main/kotlin/aoc2023/Day${DAY}.kt"
if [ -f "$code_filename" ]; then
    echo "File \"$code_filename\" already exists"
    exit 1
fi

cat <<EOF > "$code_filename"
package aoc2023

class Day${DAY} {
    fun part1(input: String): String {
        return input
    }

    fun part2(input: String): String {
        return input
    }
}

fun main() {
    val day${DAY} = Day${DAY}()
    val input = readInputAsString("day${DAY}.txt")

    println("${DAY}, part 1: \${day${DAY}.part1(input)}")
    println("${DAY}, part 2: \${day${DAY}.part2(input)}")
}
EOF

# Create test file
test_filename="${ROOT_DIR}/src/test/kotlin/aoc2023/Day${DAY}Test.kt"
if [ -f "$test_filename" ]; then
    echo "File \"$test_filename\" already exists"
    exit 1
fi

cat <<EOF > "$test_filename"
package aoc2023

import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test
import kotlin.test.assertEquals

class Day${DAY}Test {
    private lateinit var day${DAY}: Day${DAY}

    @BeforeEach
    fun setUp() {
        day${DAY} = Day${DAY}()
    }

    @Test
    fun \`part1 example\`() {
        assertEquals("abc", day${DAY}.part1("abc"))
    }

    @Test
    fun \`part2 example\`() {
        assertEquals("abc", day${DAY}.part2("abc"))
    }
}
EOF

# Download input
input_filename="${ROOT_DIR}/src/main/resources/aoc2023/day${DAY}.txt"
if [ -f "$input_filename" ]; then
    echo "File \"input_filename\" already exists"
    exit 1
fi

cookies=$(cat "${ROOT_DIR}"/.env)
curl https://adventofcode.com/2023/day/$((DAY))/input -b "${cookies}"  -o "${input_filename}"

# Add to git
git add "${code_filename}" "${test_filename}" "${input_filename}"
