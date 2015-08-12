package core

type BaseInstance interface {
	Props() *Properties
	Class() *Type
}

type LazyProperties struct {
	props *Properties
}

func NewLazyProperties() *LazyProperties {
	return &LazyProperties{}
}

func (lazy *LazyProperties) Props() *Properties {
	if lazy.props == nil {
		lazy.props = NewProperties()
	}
	return lazy.props
}

type Instance struct {
	*LazyProperties
	class *Type
}

func (inst *Instance) Class() *Type {
	return inst.class
}

func Invoke(inst BaseInstance, level string, name string, values ...BaseInstance) BaseInstance {
	prop := inst.Props().Get(level, name)
	return prop.(*UnboundMethodInstance).Invoke(inst, values...)
}

func NewInstance(class *Type) *Instance {
	return &Instance{NewLazyProperties(), class}
}
