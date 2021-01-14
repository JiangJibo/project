package _map

import "testing"

func TestMapWithFunValue(t *testing.T) {
	m := map[int]func(op int) int{}
	m[1] = func(op int) int { return op }
	m[2] = func(op int) int { return op * op }
	m[3] = func(op int) int { return op * op * op }
	t.Log(m[1](2), m[2](2), m[3](2))
}

func TestMapForSet(t *testing.T) {
	mset := map[int]bool{}
	mset[0] = true
	mset[1] = false
	if mset[0] {
		t.Logf("exists 1")
	} else {
		t.Logf("1 not exists")
	}
	delete(mset, 0)
	if mset[0] {
		t.Logf("exists 1")
	} else {
		t.Logf("1 not exists")
	}
}
