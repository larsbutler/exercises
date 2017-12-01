package main

import (
	"fmt"
	"io/ioutil"
	"os"
)

type Offset func(i int, n int) int


func solveFromBytes(data []byte, offset Offset) int {
	var nums []int = make([]int, len(data) - 1)  // chop off the newline char at the EOF
	var n int = len(nums)

	// Convert chars to nums, populate the array
	for i := 0; i < n; i++ {
		nums[i] = int(data[i]) - 48 // convert from ascii code to int in the range [0,9]
	}
	return solve(nums, offset)
	return 0
}


func solve(nums []int, offset Offset) int {
	var sum int = 0
	var n int = len(nums)
	var cur int
	var next int

	//var offset int = 1
	// 1122 -> sum of 3 (1+2) because the first digit(1) matches the second digit and the third digit (2) matches the fourth digit
	// 1111 -> 4 because digit (all 1) matches the next
	// 1234 -> 0 because no digit matches the next
	// 91212129 -> 9 because the only digit that matches the next one is the last digit, 9.
	for i := 0; i < n; i++ {
		cur = nums[i]
		nextIdx := (i + offset(i, n)) % n
		next = nums[nextIdx]
		if cur == next {
			sum += cur
		}
	}
	return sum
}

func main() {
	var filename string
	var err error
	var data []byte
	var solution int
	var plus1 Offset = func(_i int, _n int) int {
		return 1
	}
	var half Offset = func(_i int, n int) int {
		return n / 2
	}

	// Make sure a filename was actually given:
	if len(os.Args) != 2 {
		fmt.Println("Error: Specify a file name for the input")
		os.Exit(1)
	}
	filename = os.Args[1]
	// Check if the `filename` actually exists:
	_, err = os.Stat(filename)
	if os.IsNotExist(err) {
		fmt.Printf("%s: No such file or directory\n", filename)
		os.Exit(1)
	}

	data, err = ioutil.ReadFile(filename)
	if err == nil {
		// Part 1:
		solution = solveFromBytes(data, plus1)
		fmt.Printf("Solution, part 1: %d\n", solution)
		// Part 2:
		solution = solveFromBytes(data, half)
		fmt.Printf("Solution, part 2: %d\n", solution)
	} else {
		fmt.Printf("error: %s\n", err.Error())
	}

	fmt.Println("Part 1 test cases:")
	fmt.Printf("%d == %d\n", 3, solve([]int{1,1,2,2}, plus1))
	fmt.Printf("%d == %d\n", 4, solve([]int{1,1,1,1}, plus1))
	fmt.Printf("%d == %d\n", 0, solve([]int{1,2,3,4}, plus1))
	fmt.Printf("%d == %d\n", 9, solve([]int{9,1,2,1,2,1,2,9}, plus1))
	fmt.Println("Part 2 test cases:")
	fmt.Printf("%d == %d\n", 6, solve([]int{1,2,1,2}, half))
	fmt.Printf("%d == %d\n", 0, solve([]int{1,2,2,1}, half))
	fmt.Printf("%d == %d\n", 4, solve([]int{1,2,3,4,2,5}, half))
	fmt.Printf("%d == %d\n", 12, solve([]int{1,2,3,1,2,3}, half))
	fmt.Printf("%d == %d\n", 4, solve([]int{1,2,1,3,1,4,1,5}, half))
}
