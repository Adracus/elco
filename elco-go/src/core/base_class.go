package core

type BaseClass interface {
	Name() string
	Class() *Type
	Props() *Properties
	InstanceProps() *Properties
}

type UserClass struct {
	name          string
	class         *Type
	props         *Properties
	instanceProps *Properties
}

func (class UserClass) Name() string {
	return class.name
}

func (class UserClass) Class() *Type {
	return class.class
}

func (class UserClass) Props() *Properties {
	return class.props
}

func (class UserClass) InstanceProps() *Properties {
	return class.instanceProps
}

func NewUserClass(
	name string,
	props *Properties,
	instanceProps *Properties) *UserClass {

	return &UserClass{name, SimpleType(Class), props, instanceProps}
}
