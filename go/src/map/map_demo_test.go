package _map

import "testing"

func TestMapMaking(t *testing.T) {
	m := map[string]int{"one": 1, "two": 2}
	m1 := map[string]int{}
	t.Log(len(m), len(m1))
	m1["one"] = 1
	mx := make(map[string]int, 10)
	t.Log(len(mx))
}

func TestAccessNotExists(t *testing.T) {
	m1 := map[int]int{}
	t.Log(m1[2])
	m1[2] = 1
	if v, ok := m1[2]; ok {
		t.Log(v)
	} else {
		t.Log("m1[3] not exists")
	}
}

func TestMapForEach(t *testing.T) {
	m := map[string]int{"one": 1, "two": 2}
	for k, v := range m {
		t.Logf("key:%s, value:%d", k, v)
	}
}
