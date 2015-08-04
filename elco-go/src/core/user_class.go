package core

type UserClass struct {
	name          string
	class         *Type
	super         BaseClass
	props         *Properties
	instanceProps *Properties
}

func (class UserClass) Name() string {
	return class.name
}

func (class UserClass) Super() BaseClass {
	return class.super
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
	super BaseClass,
	props *Properties,
	instanceProps *Properties) *UserClass {

	return &UserClass{name, SimpleType(Class), super, props, instanceProps}
}
