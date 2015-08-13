package core

import "strconv"

type IntInstance struct {
	value int
	*Instance
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

var intType = NewLazyType(func() *Type {
	return SimpleType(Int.Class())
})
var Int = NewLazyClass(func() BaseClass {
	return NewClass("Int", func() BaseClass {
		return Object
	}, El)
})

func NewIntInstance(value int) *IntInstance {
	return &IntInstance{value, NewInstance(func() *Type {
		return intType.Class()
	})}
}
