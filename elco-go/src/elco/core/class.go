package core

var Class *LazyClass
var classType *LazyType

func init() {
	Class = NewLazyClass(func() BaseClass {
		return NewClass("Class", func() BaseClass {
			return Object.Class()
		}, El)
	})
	classType = NewLazyType(func() *Type {
		return SimpleType(Class.Class())
	})
}
