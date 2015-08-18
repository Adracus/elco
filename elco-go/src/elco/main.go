package main

import "elco/core"

func main() {
	i1 := core.NewIntInstance(10)
	i2 := core.NewIntInstance(20)
	i3 := core.GetAndInvoke(i1, "plus", i2)

	core.Invoke(core.Println, i3)
	core.Invoke(core.Println, i3)
}
