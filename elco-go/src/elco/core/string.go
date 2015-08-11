package core

import "math"

type StringInstance struct {
	value string
	*LazyProperties
}

func (str *StringInstance) HashCode() *IntInstance {
	acc := 0
	for i, c := range str.value {
		acc += int(math.Pow(float64(c), float64(i)))
	}
	return NewIntInstance(acc)
}

func (str *StringInstance) ToString() *StringInstance {
	return str
}

func (str *StringInstance) Class() *Type {
	return stringType
}

var stringType = SimpleType(String)
var String *UserClass

func init() {
	String = NewUserClass("String", Class, NewProperties(), NewProperties())
}

func NewStringInstance(value string) *StringInstance {
	return &StringInstance{value, NewDefaultLazyProperties()}
}
