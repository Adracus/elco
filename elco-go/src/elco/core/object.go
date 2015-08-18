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

	Define(Object.Class(), "public", "toString", func(inst BaseInstance) BaseInstance {
		return inst.Class().Class().Name()
	})

	Define(Object.Class(), "public", "respondsTo", func(inst BaseInstance, name *StringInstance) BoolInstance {
		_, ok := Find(inst, name.Value())
		return ToBool(ok)
	})
}
