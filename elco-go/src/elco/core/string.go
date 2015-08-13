package core

import "math"

type StringInstance struct {
	value string
	*Instance
}

type LazyStringInstance struct {
	value interface{}
}

func NewLazyString(value string) *LazyStringInstance {
	return &LazyStringInstance{value}
}

func (lazy *LazyStringInstance) String() *StringInstance {
	_string, ok := lazy.value.(*StringInstance)
	if !ok {
		inst := NewStringInstance(lazy.value.(string))
		lazy.value = inst
		return inst
	}
	return _string
}

var stringType *LazyType
var String *LazyClass

func init() {
	stringType = NewLazyType(func() *Type {
		return SimpleType(String.Class())
	})
	String = NewLazyClass(func() BaseClass {
		return NewClass("String", func() BaseClass {
			return Object.Class()
		}, El)
	})
}

func (str *StringInstance) HashCode() int {
	return HashString(str.value)
}

func NewStringInstance(value string) *StringInstance {
	return &StringInstance{value, NewInstance(func() *Type {
		return stringType.Class()
	})}
}

func HashString(s string) int {
	acc := 0
	for i, c := range s {
		acc += int(math.Pow(float64(c), float64(i)))
	}
	return acc
}
