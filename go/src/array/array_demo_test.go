package array_test

import "testing"

func TestArrayDemo(t *testing.T) {
	arr := [...]int{1, 2, 3, 4}
	for i := 0; i < len(arr); i++ {
		t.Log(arr[i])
	}

	for e_, e := range arr {
		t.Log(e_, e)
	}

	b := []int{1, 2, 3}
	b = append(b, 4)
	x := make([]int, 3, 5)
	t.Log(len(x), cap(x))
}
