package main

import (
	"errors"
	"fmt"
	"io/ioutil"
)

// Stack for holding single bytes
type Stack struct {
	size int
	stack []byte
}

func NewStack() Stack {
	return Stack{
		size: 0,
		stack: make([]byte, 0),
	}
}

func (s *Stack) Size() int {
	return s.size
}

func (s *Stack) AsArray() []byte {
	return s.stack
}

func (s *Stack) Pop() (byte, error) {
	if s.size == 0 {
		return ' ', errors.New("Stack is empty")
	} else {
		var val byte = s.stack[s.size - 1]
		// Pop off the last value:
		s.stack = s.stack[0:len(s.stack) - 1]
		s.size--;
		return val, nil
	}
}

func (s *Stack) Peek() (byte, error) {
	if s.size == 0 {
		return ' ', errors.New("Stack is empty")
	} else {
		return s.stack[s.size - 1], nil
	}
}

func (s *Stack) Push(b byte) {
	s.stack = append(s.stack, b)
	s.size++
}

func solve(data []byte) {

	var stack Stack = NewStack()
	var top byte
	var err error
	var depth int = 0
	var score int = 0
	var garbageCount int = 0

	var garbageMode bool = false

	// Ignore character after !

	for _, char := range data {
		top, err = stack.Peek()

		if err == nil && top == '!' {
			// Ignore any character after !
			// Pop the ! off the stack and continue to the next char
			stack.Pop()
			continue
		}

		switch char {
		case '!':
			stack.Push(char)
			continue
		case '<':
			if garbageMode {
				garbageCount++
			} else {
				garbageMode = true
			}
		case '>':
			if garbageMode {
				garbageMode = false
			}
		case '{':
			if garbageMode {
				garbageCount++
			} else {
				stack.Push(char)
				depth++
			}
		case '}':
			if garbageMode {
				garbageCount++
			} else if err == nil && top == '{' {
				score += depth
				stack.Pop()
				depth--
			}
		default:
			if garbageMode {
				// Ignore any other random character in the garbage
				garbageCount++
				continue
			}
		}
	}
	fmt.Printf("Score: %d\n", score)
	fmt.Printf("Garbage count: %d\n", garbageCount)
}

func main() {
	var filename string = "input-day9.txt"
	var data []byte

	data, _ = ioutil.ReadFile(filename)
	solve(data)
}
