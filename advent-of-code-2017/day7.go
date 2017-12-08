package main

import (
	"fmt"
	"io/ioutil"
	//"reflect"
	"regexp"
	"strconv"
	"strings"
)

type Node struct {
	Name string
	Weight int
	Parent *Node
	// Useful for pre processing the tree before building
	// the actual tree structure
	ChildNames []string
	Children []*Node
}

func (node *Node) AddChild(child *Node) {
	node.Children = append(node.Children, child)
	child.Parent = node
}

func buildTreeTable(lines []string) map[string]*Node {
	var table map[string]*Node  // Store nodes here for quick lookups
	var re *regexp.Regexp  // Regexp for parsing node name and weight
	var reResults []string

	table = make(map[string]*Node)
	re = regexp.MustCompile("^([a-z]+)[[:space:]]+[(]([[:digit:]]+)[)].*$")

	for _, line := range lines {
		var node Node
		var name string
		var weight int
		var parts []string
		var childNames []string

		line = strings.TrimSpace(line)
		if len(line) == 0 {
			// Skip empty lines
			continue
		}

		parts = strings.Split(line, "->")

		if len(parts) == 2 {
			childNames = strings.Split(parts[1], ", ")
			for i, cn := range childNames {
				childNames[i] = strings.TrimSpace(cn)
			}
		}
		reResults = re.FindStringSubmatch(parts[0])
		name = strings.TrimSpace(reResults[1])
		weight, _ = strconv.Atoi(reResults[2])

		// Create node, put it in the table with its weight and note who its
		// children are:
		node = Node{
			Name: name,
			Weight: weight,
			ChildNames: childNames,
			Children: make([]*Node, 0),
		}
		table[name] = &node
	}
	return table
}

func buildTree(treeTable map[string]*Node) {
	for _, node := range treeTable {
		for _, childName := range node.ChildNames {
			var childNode *Node
			childNode = treeTable[childName]
			node.AddChild(childNode)
		}
	}
}

func findTreeRoot(treeTable map[string]*Node) *Node {
	for _, node := range treeTable {
		if node.Parent == nil {
			// This is the root!
			return node
		}
	}
	return nil
}

func solve(lines []string) {
	var treeTable map[string]*Node
	var root *Node

	fmt.Println("Building initial tree table...")
	treeTable = buildTreeTable(lines)
	fmt.Println("Completing the tree table...")
	buildTree(treeTable)

	fmt.Println("Finding the root node of the tree...")
	root = findTreeRoot(treeTable)
	fmt.Printf("Part 1 solution: %s\n", root.Name)

}


func main() {
	var data []byte
	var lines []string

	//data, _ = ioutil.ReadFile("test-input-day7.txt")
	data, _ = ioutil.ReadFile("input-day7.txt")
	lines = strings.Split(string(data), "\n")
	solve(lines)
}
