package core

type BaseInstance interface {
	Props() *Properties
	Class() *Type
}

type Instance struct {
	props *LazyProperties
	class *LazyType
}

func (inst *Instance) Props() *Properties {
	return inst.props.Props()
}

func (inst *Instance) Class() *Type {
	return inst.class.Class()
}

func Invoke(inst BaseInstance, level string, name string, values ...BaseInstance) BaseInstance {
	levelHash := HashString(level)
	nameHash := HashString(name)
	visibilityMap := inst.Props().Map().Values()[levelHash].Value.(*MapInstance)
	prop := visibilityMap.Values()[nameHash].Value
	return prop.(*UnboundMethodInstance).Invoke(inst, values...)
}

func NewInstance(classGenerator func() *Type) *Instance {
	return &Instance{NewLazyProperties(), NewLazyType(classGenerator)}
}
