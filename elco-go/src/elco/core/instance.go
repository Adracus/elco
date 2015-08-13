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
	*LazyType
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
