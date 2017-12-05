package main

import (
	"bufio"
	"fmt"
	"os"
	"strconv"
)

func solve(filename string) {
	var file *os.File
	var scanner *bufio.Scanner
	var data []int = make([]int, 0)
	var steps int

	file, _ = os.Open(filename)
	defer file.Close()

	scanner = bufio.NewScanner(file)
	scanner.Split(bufio.ScanLines)

	for scanner.Scan() {
		var x int
		x, _ = strconv.Atoi(scanner.Text())
		data = append(data, x)
	}

	steps = getStepsToExit(data)
	fmt.Printf("Steps: %d\n", steps)
}

func getStepsToExit(data []int) int {
	var i int = 0
	var n int = len(data)
	var steps int = 0

	for {
		// add current value at data[i] to i
		if i < 0 || i >= n {
			// We've exited the bounds of the array:
			break
		}
		// Keep track of the starting location:
		var prevI int = i
		var offset int
		// Read the offset value from the current location in the array:
		offset = data[i]
		// Add the offset to the location:
		i = i + offset
		// Increment the value at the starting location:

		// Part1:
		//data[prevI] = data[prevI] + 1
		// Part2:
		if offset >= 3 {
			data[prevI] = data[prevI] - 1
		} else {
			data[prevI] = data[prevI] + 1
		}
		// /Part2:
		steps++
	}
	return steps
}



func main() {
	var filename string
	if len(os.Args) != 2 {
		fmt.Println("Error: Specify a file name for the input")
		os.Exit(1)
	}

	filename = os.Args[1]
	solve(filename)
	//readLine(filename)

	/*
	file, err := os.Open(filename)
	defer file.Close()

	if err != nil {
		fmt.Printf("Error: %s\n", err.Error())
		os.Exit(1)
	}
	*/

	//solve(*file)
}
