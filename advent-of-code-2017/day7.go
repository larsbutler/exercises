package main

import (
	"fmt"
	"io/ioutil"
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

func (node *Node) TreeWeight() int {
	var weight int = 0
	weight += node.Weight
	for _, child := range node.Children {
		weight += child.TreeWeight()
	}
	return weight
}

func (node *Node) IsLeaf() bool {
	return len(node.Children) == 0
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

// Turn the soft links from parent to child into hard bi-directional links.
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

func findUnbalancedProgram(root *Node) (minChild *Node, maxChild *Node) {
	var minWeight int
	var maxWeight int

	if root.IsLeaf() {
		minChild = nil
		maxChild = nil
		return
	}

	minWeight = root.Children[0].TreeWeight()
	maxWeight = minWeight
	minChild = root.Children[0]
	maxChild = minChild

	for i := 1; i < len(root.Children); i++ {
		var child *Node = root.Children[i]
		var childWeight int

		childWeight = child.TreeWeight()

		if childWeight <= minWeight {
			minWeight = childWeight
			minChild = child
		}
		if childWeight >= maxWeight {
			maxWeight = childWeight
			maxChild = child
		}
	}
	if minWeight != maxWeight {
		// The maxChild is the unabalanced subtree.
		// See if there's another node deeper down the tree that is unbalanced,
		// or if the max child is indeed the unbalanced node.
		if min, max := findUnbalancedProgram(maxChild); max != nil {
			minChild = min
			maxChild = max
			return
		} else {
			// There's nothing deeper; this is the one we're looking for.
			return
		}
	}
	minChild = nil
	maxChild = nil
	return
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

	var min *Node
	var max *Node
	fmt.Printf("Finding the unbalanced program...")
	min, max = findUnbalancedProgram(root)
	var diff int = max.TreeWeight() - min.TreeWeight()
	var solution int = max.Weight - diff
	fmt.Printf("Part 2 solution: %s needs to be lighter by %d .: %d\n", max.Name, diff, solution)
}


func main() {
	var data []byte
	var lines []string

	//data, _ = ioutil.ReadFile("test-input-day7.txt")
	data, _ = ioutil.ReadFile("input-day7.txt")
	lines = strings.Split(string(data), "\n")
	solve(lines)
}
