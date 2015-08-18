package main

import "elco/core"
import "fmt"

func main() {
	i1 := core.NewIntInstance(10)
	i2 := core.NewIntInstance(20)
	plus := core.Get(i1, "plus")

	i3 := plus.(*core.MethodInstance).Invoke(i2)

	fmt.Println(i3)
}
