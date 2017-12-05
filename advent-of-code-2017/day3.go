package main

import (
	"fmt"
)

type Matrix [][]int

func matrix_set_value(m Matrix, row int, col int, value int) {
	fmt.Printf("Setting value: row=%d, col=%d, value=%d\n", row, col, value)
	m[row][col] = value
	print_matrix(m)
}

func print_matrix(m Matrix) {
	/*
	fmt.Println("[")
	for i := 0; i < len(m); i++ {
		//fmt.Printf(" %+v\n", m[i])
		fmt.Printf("  [")
		for j := 0; j < len(m); j++ {
			fmt.Printf("%6d, ", m[i][j])
		}
		fmt.Println("  ]")
	}
	fmt.Println("]")
	*/
}

func max_of_ring(ring int) int {
	if ring == 1 {
		return 9
	} else {
		return ring*8 + max_of_ring(ring-1)
	}
}

func get_ring_of_target(ring int, target int) int {
	if max_of_ring(ring) >= target {
		return ring
	} else {
		return get_ring_of_target(ring+1, target)
	}
}

func width_of_ring(ring int) int {
	return ring * 2 + 1
}

func sum_neighbors(matrix Matrix, row int, col int) int {
	sum := 0
	// below
	sum += matrix[row + 1][col]
	// above
	sum += matrix[row - 1][col]
	// right
	sum += matrix[row][col + 1]
	// left
	sum += matrix[row][col - 1]

	// below left
	sum += matrix[row + 1][col - 1]
	// below right
	sum += matrix[row + 1][col + 1]
	// above left
	sum += matrix[row - 1][col - 1]
	// above right
	sum += matrix[row - 1][col + 1]
	return sum
}

func sum_until(matrix Matrix, center int, row int, col int, target int, length int) int {
	var sum int
	fmt.Printf("--> start: row=%d, col=%d\n", row, col)

	// move up "length"
	for i:= 0; i < length; i++ {
		fmt.Println("Moving up")
		row -= 1
		sum = sum_neighbors(matrix, row, col)
		matrix_set_value(matrix, row, col, sum)
		if sum > target {
			return sum
		}
		// TODO: set the sum in matrix
	}
	// move left "length"
	for i:= 0; i < length; i++ {
		fmt.Println("Moving left")
		col -= 1
		sum = sum_neighbors(matrix, row, col)
		matrix_set_value(matrix, row, col, sum)
		if sum > target {
			return sum
		}
	}
	// move down "length"
	for i:= 0; i < length; i++ {
		fmt.Println("Moving down")
		row += 1
		sum = sum_neighbors(matrix, row, col)
		matrix_set_value(matrix, row, col, sum)
		if sum > target {
			return sum
		}
	}
	// move right "length"
	for i:= 0; i < length; i++ {
		fmt.Println("Moving right")
		col += 1
		sum = sum_neighbors(matrix, row, col)
		matrix_set_value(matrix, row, col, sum)
		if sum > target {
			return sum
		}
	}
	fmt.Printf("-------> Recursing, row=%d, col=%d\n", row+1, col+1)
	return sum_until(matrix, center, row+1, col+1, target, length+2)
}

func main() {
	target := 312051
	fmt.Printf("target=%d\n", target)
	ring := get_ring_of_target(1, target)
	fmt.Printf("ring=%d\n", ring)
	ring_max := max_of_ring(ring)
	fmt.Printf("ring_max=%d\n", ring_max)
	ring_width := width_of_ring(ring)
	fmt.Printf("ring_width=%d\n", ring_width)
	square_size := ring_width + 2

	//target = 400
	//square_size = 9

	matrix := make(Matrix, square_size)
	for i := 0; i < square_size; i++ {
		matrix[i] = make([]int, square_size)
	}
	fmt.Printf("matrix dims: rows=%d, cols=%d\n", len(matrix), len(matrix[0]))
	center := square_size / 2
	fmt.Printf("center=%d\n", center)
	var row int
	var col int

	row = center
	col = center
	// IMPORTANT: set the center value to 1 to seed the calculation
	matrix[row][col] = 1
	row += 1 // move down
	col += 1 // move right
	length := 2
	print_matrix(matrix)
	result := sum_until(matrix, center, row, col, target, length)
	//fmt.Printf("matrix=%+v\n", matrix[center-3:center+3])
	// first guess 425763
	// second guess 312453
	fmt.Printf("result=%d\n", result)
}
