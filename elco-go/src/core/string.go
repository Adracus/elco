package core

type StringInstance struct {
	value string
	props *Properties
}

func (str *StringInstance) Class() *Type {
	return stringType
}

func (str *StringInstance) Props() *Properties {
	return str.props
}

var stringType = SimpleType(String)
var String *UserClass

func init() {
}

func NewStringInstance(value string) *StringInstance {
	return &StringInstance{value, NewProperties()}
}
