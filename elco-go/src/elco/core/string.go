package core

type StringInstance struct {
	value string
	*Instance
}

var stringType = SimpleType(String)
var String = NewClass("String", Object, El)

func NewStringInstance(value string) *StringInstance {
	return &StringInstance{value, NewInstance(stringType)}
}
