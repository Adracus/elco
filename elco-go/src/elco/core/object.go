package core

var Object *LazyClass
var objectType *LazyType

func init() {
	Object = NewLazyClass(func() BaseClass {
		return NewClass("Object", func() BaseClass {
			return Object.Class()
		}, El)
	})
	objectType = NewLazyType(func() *Type {
		return SimpleType(Object.Class())
	})
}
