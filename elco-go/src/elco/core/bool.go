package core

type BoolInstance interface {
	Value() bool
}

type falseInstance struct {
	*Instance
}

type trueInstance struct {
	*Instance
}

var True = &trueInstance{NewInstance(typeFn)}
var False = &falseInstance{NewInstance(typeFn)}

func (f *falseInstance) Value() bool {
	return false
}

func (f *trueInstance) Value() bool {
	return true
}

var boolType = NewLazyType(func() *Type {
	return SimpleType(Bool.Class())
})
var typeFn = func() *Type {
	return boolType.Class()
}

var Bool = NewLazyClass(func() BaseClass {
	return NewClass("Bool", func() BaseClass {
		return Object.Class()
	}, El)
})

func ToBool(value bool) BoolInstance {
	if value {
		return True
	}
	return False
}
