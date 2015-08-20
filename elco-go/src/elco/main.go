package main

import "elco/core"

func main() {
	core.Scope.Set("b", core.NewIntInstance(10))
	core.Scope.Set("plus", core.Get(core.Scope.Get("b"), "plus"))
	core.Scope.Set("res", core.Invoke(core.Scope.Get("plus"), core.NewIntInstance(50)))
	if core.AsBool(core.Invoke(core.Get(core.Scope.Get("res"), "equals"), core.NewIntInstance(50))) {
		core.Invoke(core.Scope.Get("println"), core.Scope.Get("res"))
	}
}
