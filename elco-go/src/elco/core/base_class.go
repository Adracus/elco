package core

type BaseClass interface {
	Name() *StringInstance
	Super() BaseClass
	Class() *Type
	Props() *Properties
	Generics() BaseListInstance
}

type ClassInstance struct {
	*LazyStringInstance
	super BaseClass
	*Instance
	generics BaseListInstance
}

func Define(class BaseClass, name string, fn interface{}) *UnboundMethodInstance {
	m := class.Props().Map().Values()
	hash := HashString(name)
	key := NewStringInstance(name)
	value := NewUnboundMethodInstance(fn)
	m[hash] = &MapEntry{key, value}
	return value
}

func (class *ClassInstance) Name() *StringInstance {
	return class.String()
}

func (class *ClassInstance) Super() BaseClass {
	return class.super
}

func (class *ClassInstance) Generics() BaseListInstance {
	return class.generics
}

func NewClass(name string, super BaseClass, generics BaseListInstance) *ClassInstance {
	_name := NewLazyString(name)
	_inst := NewInstance(func() *Type {
		return SimpleType(Class)
	})
	return &ClassInstance{_name, super, _inst, generics}
}
