package core

type BoolInstance interface {
	Value() bool
}

type falseInstance struct {
	*LazyProperties
}

type trueInstance struct {
	*LazyProperties
}

var True = &trueInstance{NewDefaultLazyProperties()}
var False = &falseInstance{NewDefaultLazyProperties()}

func (t *trueInstance) Class() *Type {
	return boolType
}

func (t *trueInstance) HashCode() *IntInstance {
	return NewIntInstance(1)
}

func (t *trueInstance) ToString() *StringInstance {
	return NewStringInstance("True")
}

func (t *trueInstance) Value() bool {
	return true
}

func (t *falseInstance) Class() *Type {
	return boolType
}

func (t *falseInstance) HashCode() *IntInstance {
	return NewIntInstance(0)
}

func (t *falseInstance) ToString() *StringInstance {
	return NewStringInstance("False")
}

func (t *falseInstance) Value() bool {
	return false
}

var boolType = SimpleType(Bool)
var Bool *UserClass

func init() {
	Bool = NewUserClass("Bool", Class, NewProperties(), NewProperties())
}
