package main

import "elco/core"

func main() {
	core.Scope.Set("b", core.NewIntInstance(10))
	core.Scope.Set("plus", core.Get(core.Scope.Get("b"), "plus"))
	core.Invoke(core.Scope.Get("println"), core.Invoke(core.Scope.Get("plus"), core.NewIntInstance(20)))
}
