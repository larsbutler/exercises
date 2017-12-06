package main

import (
	"fmt"
	"io/ioutil"
	"strconv"
	"strings"
)

func toIntArray(data []string) []int {
	var n int = len(data)
	var ints []int = make([]int, n)
	for i, s := range data {
		ints[i], _ = strconv.Atoi(s)
	}
	return ints
}

func indexOfMax(data []int) int {
	var imax int

	if len(data) == 0 {
		// There is no "index of max" if the array is empty
		return -1
	}

	imax = 0
	for i, _ := range data {
		if data[i] > data[imax] {
			imax = i
		}
	}
	return imax
}

func toStringArray(data []int) []string {
	var n int = len(data)
	var strs []string = make([]string, n)
	for i, num := range data {
		strs[i] = strconv.Itoa(num)
	}
	return strs
}

func memoryFingerprint(memory []int) string {
	var fp string = strings.Join(toStringArray(memory), "-")
	return fp
}

// Counter that rolls over when it overflows.
type Counter struct {
	Current int
	Min int  // Inclusive
	Max int  // Inclusive
}

func (c *Counter) Incr() {
	c.Current++
	if c.Current == c.Max {
		c.Current = c.Min
	}
}

func redistribute(memory []int) {
	var imax int
	var value int
	var n int = len(memory)
	var counter Counter

	fmt.Printf("Redistributing: %+v\n", memory)

	// Locate the largest block:
	imax = indexOfMax(memory)
	// Grab the value from that memory location:
	value = memory[imax]
	// Zero out that memory location:
	memory[imax] = 0

	// Start redistributing at imax+1:
	counter = Counter{Current: imax, Min: 0, Max: n}
	counter.Incr()

	for ; value > 0; value-- {
		fmt.Printf("Current counter: %d\n", counter.Current)
		memory[counter.Current]++
		counter.Incr()
	}
}

func solve(data []string) (part1Cycles int, part2Cycles int) {
	var memory []int = toIntArray(data)
	// Memory fingerprint
	var fp string
	// Keys: memory fingerprint, Values: times seen
	var seen map[string]int = make(map[string]int)
	// Repeated fingerprint for part2
	var repeatFp string
	// State flag to indicate when part2 has started:
	var part2 bool = false

	// To start with, get the initial fingerprint:
	fp = memoryFingerprint(memory)
	seen[fp] = 1

	part1Cycles = 0
	part2Cycles = 0
	for {
		// redistribute
		redistribute(memory)

		// Increment the right cycle counter:
		if part2 {
			fmt.Println("Incr part 2")
			part2Cycles++
		} else {
			part1Cycles++
		}

		// check if fingerprint has been seen before
		fp = memoryFingerprint(memory)
		if _, hasFp := seen[fp]; hasFp {
			seen[fp]++
			fmt.Printf("Repeated memory config: %s\n", fp)
			if !part2 {
				// If we've seen it before and we're still in part 1,
				// begin part2:
				fmt.Println("beginning part 2")
				part2 = true
				// Note this memory fingerprint so we can look for the next instance of it:
				repeatFp = fp
				continue
			} else {
				// If we've seen the same memory again (the one which moved us to part2),
				// we're done.
				if fp == repeatFp {
					break
				}
			}
		} else {
			// Put in the new fingerprint:
			seen[fp] = 1
			fmt.Printf("New memory config: %s\n", fp)
		}
	}

	return
}

func main() {
	var data []byte
	var strData []string
	var part1Cycles int
	var part2Cycles int

	data, _ = ioutil.ReadFile("input-day6.txt")
	// It's a single line of text with numbers, each separated by whitespace:
	strData = strings.Fields(string(data))
	part1Cycles, part2Cycles = solve(strData)
	fmt.Printf("Part 1 cycles: %d\n", part1Cycles)
	fmt.Printf("Part 2 cycles: %d\n", part2Cycles)
}
