package core

type BaseClass interface {
	Name() *StringInstance
	Super() BaseClass
	Class() *Type
	Props() *Properties
	Generics() BaseListInstance
}

type ClassInstance struct {
	name  *StringInstance
	super BaseClass
	*Instance
	generics BaseListInstance
}

func (class *ClassInstance) Name() *StringInstance {
	return class.name
}

func (class *ClassInstance) Super() BaseClass {
	return class.super
}

func (class *ClassInstance) Generics() BaseListInstance {
	return class.generics
}

func NewClass(name string, super BaseClass, generics BaseListInstance) *ClassInstance {
	_name := NewStringInstance(name)
	_inst := NewInstance(SimpleType(Class))
	return &ClassInstance{_name, super, _inst, generics}
}
