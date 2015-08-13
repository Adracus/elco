package main

import "elco/core"
import "fmt"

func main() {
	i1 := core.NewIntInstance(10)
	i2 := core.NewIntInstance(20)
	i3 := core.Invoke(i1, "public", "plus", i2)

	fmt.Println(i3)
}
