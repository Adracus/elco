package core

type IntInstance struct {
	value int
	*LazyProperties
}

func (int *IntInstance) Class() *Type {
	return intType
}

func (this *IntInstance) Plus(that *IntInstance) *IntInstance {
	return NewIntInstance(this.value + that.value)
}

func (int *IntInstance) HashCode() *IntInstance {
	return int
}

var intType = SimpleType(Int)
var Int *UserClass

func init() {
	Int = NewUserClass("Int", Class, NewProperties(), NewProperties())
}

func NewIntInstance(value int) *IntInstance {
	return &IntInstance{value, NewDefaultLazyProperties()}
}
