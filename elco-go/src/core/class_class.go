package core

type ClassClass struct {
	class         *Type
	super         BaseClass
	props         *Properties
	instanceProps *Properties
}

var name = NewStringInstance("Class")

func (c *ClassClass) Super() BaseClass {
	return c.super
}

func (c *ClassClass) Name() *StringInstance {
	return name
}

func (c *ClassClass) Class() *Type {
	return c.class
}

func (c *ClassClass) Props() *Properties {
	return c.props
}

func (c *ClassClass) InstanceProps() *Properties {
	return c.instanceProps
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
	Class = &ClassClass{
		props:         nil,
		instanceProps: nil,
	}

	subclassMethod := NewUnboundMethodInstance(Subclass)

	Class.InstanceProps().SetAll("public", map[string]BaseInstance{
		"create":   subclassMethod,
		"subclass": subclassMethod,
	})
}
