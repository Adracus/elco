package core

type ClassClass struct {
	name          string
	class         *Type
	props         *Properties
	instanceProps *Properties
}

func (c ClassClass) Name() string {
	return c.name
}

func (c ClassClass) Class() *Type {
	return c.class
}

func (c ClassClass) Props() *Properties {
	return c.props
}

func (c ClassClass) InstanceProps() *Properties {
	return c.instanceProps
}

var Class *ClassClass

func init() {
	Class = &ClassClass{
		name:          "Class",
		props:         nil,
		instanceProps: nil,
	}
}
