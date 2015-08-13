package core

import "strconv"

type IntInstance struct {
	value int
	*Instance
}

func (int *IntInstance) Class() *Type {
	return intType
}

func Plus(this *IntInstance, that *IntInstance) *IntInstance {
	return NewIntInstance(this.value + that.value)
}

func (int *IntInstance) HashCode() *IntInstance {
	return int
}

func (int *IntInstance) ToString() *StringInstance {
	return NewStringInstance(strconv.Itoa(int.value))
}

var intType = SimpleType(Int)
var Int = NewClass("Int", Object, El)

func NewIntInstance(value int) *IntInstance {
	return &IntInstance{value, NewInstance(func() *Type {
		return intType
	})}
}
