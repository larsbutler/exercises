package main

import (
	//"errors"
	"fmt"
	"io/ioutil"
	"math"
	"strings"
)

func simplifyOpposites(vectors map[string]int, left string, right string) {
	var leftVal int
	var rightVal int

	leftVal, leftExists := vectors[left]
	rightVal, rightExists := vectors[right]
	if !leftExists || !rightExists {
		// We're missing at least one; dont' do anything.
		return
	}

	var diff int = leftVal - rightVal
	var absDiff int = int(math.Abs(float64(diff)))
	var min int = int(math.Min(float64(leftVal), float64(rightVal)))

	if diff == 0 {
		// They perfectly cancelled out
		fmt.Printf("%s and %s cancelled out completely\n", left, right)
		vectors[left] = 0
		vectors[right] = 0
	} else if diff > 0 {
		// We have more on the left hand side
		vectors[left] = diff
		vectors[right] = 0
	} else if diff < 0 {
		// We have more on the right hand side
		// Take absolute value since it's negative
		vectors[left] = 0
		vectors[right] = absDiff
	}
	fmt.Printf("simplifyOpposites(%s, %s) eliminated %d items\n", left, right, min)
}

func simplifyVector(vectors map[string]int, left string, right string, repl string) {
	leftVal, leftExists := vectors[left]
	rightVal, rightExists := vectors[right]
	if !leftExists || !rightExists {
		// We're missing at least one; dont' do anything.
		return
	}

	var diff int = leftVal - rightVal
	var absDiff int = int(math.Abs(float64(diff)))
	var min int = int(math.Min(float64(leftVal), float64(rightVal)))

	if diff == 0 {
		// Zero out left and right
		vectors[left] = 0
		vectors[right] = 0
		// Add the number of left+right combinations to the repl key,
		// thus replacing N left+right combinations with N repl entries.
		vectors[repl] = vectors[repl] + min
	} else if diff > 0 {
		// We have more on the left hand side
		vectors[left] = diff
		vectors[repl] = vectors[repl] + vectors[right]
		vectors[right] = 0
	} else if diff < 0 {
		// We have more on the right hand side
		// Take absolute value since it's negative
		vectors[right] = absDiff
		vectors[repl] = vectors[repl] + vectors[left]
		vectors[left] = 0
	}
	fmt.Printf("simplifyVector(%s, %s, %s) eliminated %d items\n", left, right, repl, min)
}

func simplifyVectors(vectors map[string]int) {
	// Simplify further, replacing certain combinations of vectors with a simpler one
	simplifyVector(vectors, "n", "sw", "nw")
	simplifyVector(vectors, "n", "se", "ne")
	simplifyVector(vectors, "ne", "s", "se")
	simplifyVector(vectors, "nw", "s", "sw")
	simplifyVector(vectors, "ne", "nw", "n")
	simplifyVector(vectors, "se", "sw", "s")
}

func simplifyOppositeVectors(vectors map[string]int) {
	// Simplify/cancel out opposites
	simplifyOpposites(vectors, "ne", "sw")
	simplifyOpposites(vectors, "nw", "se")
	simplifyOpposites(vectors, "n", "s")
}

func solve(commands []string) {
	//var top string
	// Pre-load the first command onto the stack:
	var cmd string = commands[0]
	var vectors map[string]int = make(map[string]int)
	var xdir int = 0
	var ydir int = 0

	hexDistance := func(x, y int) int {
		// Moves on x and y are counted "double",
		// i.e., n and s are counted as two steps on the y axis
		// while ne/nw/se/sw are counted as 1 step on y and 1 step on x
		absX := math.Abs(float64(x))
		absY := math.Abs(float64(y))

		return int(absY / 2) + int(absX / 2)
	}
	var maxHexDist int = 0

	for _, cmd = range commands {
		cmd = strings.TrimSpace(cmd)
		//val, exists := vectors[cmd]
		_, exists := vectors[cmd]
		// Collect and count all of the unique commands
		if exists {
			vectors[cmd] = vectors[cmd] + 1
		} else {
			vectors[cmd] = 1
		}
		// Part 2: Calculate the max distance on the hex grid:
		switch cmd {
		case "n": ydir += 2
		case "s": ydir -= 2
		case "ne":
			ydir++
			xdir++
		case "se":
			ydir--
			xdir++
		case "nw":
			ydir++
			xdir--
		case "sw":
			ydir--
			xdir--
		}
		hexDist := hexDistance(xdir, ydir)
		if hexDist > maxHexDist {
			maxHexDist = hexDist
		}
	}

	simplifyOppositeVectors(vectors)
	simplifyVectors(vectors)

	fmt.Printf("Vectors: %v\n", vectors)
	fmt.Printf("s:%v\n", vectors)

	var steps int = 0
	for _, v := range vectors {
		steps += v
	}

	fmt.Printf("Steps: %d\n", steps)
	fmt.Printf("Max dist: %d\n", maxHexDist)
}

func main() {
	var filename string = "input-day11.txt"
	var commands []string
	var data []byte

	data, _ = ioutil.ReadFile(filename)
	commands = strings.Split(string(data), ",")
	solve(commands)
}
