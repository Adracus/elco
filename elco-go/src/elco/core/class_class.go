package core

type ClassClass struct {
	class *Type
	super BaseClass
	*LazyProperties
	instanceProps *LazyProperties
}

var name = NewStringInstance("Class")

func (c *ClassClass) Super() BaseClass {
	return c.super
}

func (c *ClassClass) ToString() *StringInstance {
	return name
}

func (c *ClassClass) Name() *StringInstance {
	return name
}

func (c *ClassClass) Class() *Type {
	return c.class
}

func (c *ClassClass) HashCode() *IntInstance {
	return c.Name().HashCode()
}

func (c *ClassClass) InstanceProps() *Properties {
	return c.instanceProps.Props()
}

var Class *ClassClass

func Subclass(class BaseInstance, name ...BaseInstance) BaseInstance {
	_class := class.(BaseClass)
	_name := (name[0]).(*StringInstance)
	return &UserClass{
		name:          _name,
		class:         SimpleType(Class),
		super:         _class,
		props:         _class.Props().Inheritable(),
		instanceProps: NewProperties(),
	}
}

func init() {
	Class = &ClassClass{nil, nil, NewDefaultLazyProperties(), NewDefaultLazyProperties()}

	subclassMethod := NewUnboundMethodInstance(Subclass)

	instProps := Class.InstanceProps()
	instProps.Set("public", "create", subclassMethod)
	instProps.Set("public", "subclass", subclassMethod)
}
