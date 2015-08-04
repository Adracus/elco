package core

type BaseInstance interface {
	Props() *Properties
	Class() *Type
}

type Instance struct {
	props *Properties
	class *Type
}

func (inst *Instance) Props() *Properties {
	return inst.props
}

func (inst *Instance) Class() *Type {
	return inst.class
}

func NewInstance(class *Type, props *Properties) *Instance {
	return &Instance{props, class}
}
