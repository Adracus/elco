package core

import "fmt"

var Print = NewMethodInstance(func(args ...BaseInstance) BaseInstance {
	fmt.Print(toStrings(args...)...)
	return UnitInstance
})

var Println = NewMethodInstance(func(args ...BaseInstance) BaseInstance {
	fmt.Println(toStrings(args...)...)
	return UnitInstance
})

func toStrings(args ...BaseInstance) []interface{} {
	strings := make([]interface{}, len(args))
	for i, elem := range args {
		strings[i] = GetAndInvoke(elem, "toString").(*StringInstance).Value()
	}
	return strings
}
