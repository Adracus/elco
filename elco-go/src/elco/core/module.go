package core

var Module *LazyClass
var moduleType *LazyType

func init() {
	Module = NewLazyClass(func() BaseClass {
		return NewClass("Module", func() BaseClass {
			return Object.Class()
		}, El)
	})

	moduleType = NewLazyType(func() *Type {
		return SimpleType(Module.Class())
	})

	StaticDefine(Module.Class(), "public", "initialize", func(inst BaseInstance) BaseInstance {
		inst.Props().Put("public", "subModules", NewMapInstance())
		inst.Props().Put("public", "values", NewMapInstance())

		return inst
	})

	Define(Module.Class(), "public", "get", func(module BaseInstance, name *StringInstance) BaseInstance {
		if val, ok := Get(module, "values").(*MapInstance).Get(name.Value()); ok {
			return val
		}
		panic("Could not find " + name.Value())
	})

	Define(Module.Class(), "public", "contains", func(module BaseInstance, name *StringInstance) BoolInstance {
		_, ok := Get(module, "values").(*MapInstance).Get(name.Value())
		return ToBool(ok)
	})

	Define(Module.Class(), "public", "set", func(module BaseInstance, name *StringInstance, value BaseInstance) BaseInstance {
		m := Get(module, "values").(*MapInstance)
		m.Put(name.Value(), value)

		return UnitInstance
	})
}
