package main

import "elco/core"

func main() {
	i1 := core.NewIntInstance(10)
	i2 := core.NewIntInstance(20)

	plus := i1.Class().Class().InstanceProps().Get("public", "plus").(*core.UnboundMethodInstance).Fn().(func(*core.IntInstance, *core.IntInstance) *core.IntInstance)
	i3 := plus(i1, i2)

	core.Print.Fn().(func(...core.BaseInstance))(i3)
}
