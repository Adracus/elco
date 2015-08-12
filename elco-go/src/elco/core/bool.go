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

var True = &trueInstance{NewInstance(SimpleType(Bool))}
var False = &falseInstance{NewInstance(SimpleType(Bool))}

func (f *falseInstance) Value() bool {
	return false
}

func (f *trueInstance) Value() bool {
	return true
}

var Bool = NewClass("Bool", Object, El)
