package core

var name = NewStringInstance("Class")

var Class *ClassInstance

func init() {
	Class = &ClassInstance{
		NewStringInstance("Class"),
		nil,
		nil,
		nil,
	}

	Class.class = SimpleType(Class)
	Class.super = Object
	Class.generics = El
}
