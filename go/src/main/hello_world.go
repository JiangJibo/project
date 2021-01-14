package main

import "fmt"

const (
	MONDAY = iota + 1
	TUESDAY
	THURSDAY
)

var y int

func main() {

	main1()
}

func switchTest() {
	switch a := 1 == 1; a {
	case true:
		break
	case false:
		break
	}

	switch {
	case 1 > 2 && 2 == 3:
		fmt.Print("")
	case true:
		fmt.Print("")
	}

	switch x := 1; x {
	case 1, 2:
	case 4, 5, 6:
	}
}

func main1() {
	var i = 3
	for i < 20 {
		fmt.Println(i)
		i++
	}
	fmt.Print("Hello World")
	if a := 1 == 1; a {
		fmt.Print(a)
	}
	x := 1
	y = 1
	fmt.Print(x, y)

	for {
		if 2 > 1 {
			break
		}
	}

	var a = [3]int{1, 2, 3}
	a[0] = 1

	b := [3]int{1, 2, 3}
	fmt.Print(len(b))
}
