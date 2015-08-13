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

var boolType = SimpleType(Bool)
var typeFn = func() *Type {
	return boolType
}

var Bool = NewClass("Bool", Object, El)
