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

	Define(Class.Class(), "public", "create", func(class BaseClass, args ...BaseInstance) BaseInstance {
		inst := NewInstance(func() *Type {
			return SimpleType(class)
		})

		newArgs := []BaseInstance{inst}
		newArgs = append(newArgs, args...)

		return GetAndInvoke(class, "initialize", newArgs...)
	})

	Define(Class.Class(), "public", "initialize", func(class BaseClass, inst BaseInstance, args ...BaseInstance) BaseInstance {
		return inst
	})
}
