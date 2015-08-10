package core

type IntInstance struct {
	value int
	props *Properties
}

func (int *IntInstance) Class() *Type {
	return intType
}

func (int *IntInstance) HashCode() *IntInstance {
	return int
}

func (int *IntInstance) Props() *Properties {
	return int.props
}

var intType = SimpleType(Int)
var Int *UserClass

func init() {
	Int = NewUserClass("Int", Class, NewProperties(), NewProperties())
}

func NewIntInstance(value int) *IntInstance {
	return &IntInstance{value, NewProperties()}
}
