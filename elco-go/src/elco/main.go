package main

import "elco/core"

func main() {
	i1 := core.NewIntInstance(10)
	i2 := core.NewIntInstance(20)

	i3 := i1.Plus(i2)
	core.Print.Apply(i3)
}
