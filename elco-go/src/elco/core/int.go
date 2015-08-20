package core

import "strconv"

type IntInstance struct {
	value int
	*Instance
}

func Plus(this *IntInstance, that *IntInstance) *IntInstance {
	return NewIntInstance(this.value + that.value)
}

func (i *IntInstance) HashCode() *IntInstance {
	return i
}

func (int *IntInstance) ToString() *StringInstance {
	return NewStringInstance(strconv.Itoa(int.value))
}

var intType *LazyType
var Int *LazyClass

func init() {
	intType = NewLazyType(func() *Type {
		return SimpleType(Int.Class())
	})
	Int = NewLazyClass(func() BaseClass {
		return NewClass("Int", func() BaseClass {
			return Object.Class()
		}, El)
	})

	Define(Int.Class(), "public", "hashCode", func(i *IntInstance) *IntInstance {
		return i
	})
	Define(Int.Class(), "public", "toString", func(i *IntInstance) *StringInstance {
		return i.ToString()
	})
	Define(Int.Class(), "public", "plus", func(a *IntInstance, b *IntInstance) *IntInstance {
		return NewIntInstance(a.value + b.value)
	})
	Define(Int.Class(), "public", "equals", func(a *IntInstance, b *IntInstance) BoolInstance {
		return ToBool(a.value == b.value)
	})
}

func NewIntInstance(value int) *IntInstance {
	return &IntInstance{value, NewInstance(func() *Type {
		return intType.Class()
	})}
}
