package core

type BaseInstance interface {
	Props() *Properties
	Class() *Type
	HashCode() *IntInstance
}

type LazyProperties struct {
	props     *Properties
	generator func() *Properties
}

func NewDefaultLazyProperties() *LazyProperties {
	return NewLazyProperties(nil)
}

func NewLazyProperties(generator func() *Properties) *LazyProperties {
	return &LazyProperties{nil, generator}
}

func (lazy *LazyProperties) Props() *Properties {
	if lazy.props == nil {
		if lazy.generator != nil {
			lazy.props = lazy.generator()
		} else {
			lazy.props = NewProperties()
		}
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

func NewInstance(class *Type, props func() *Properties) *Instance {
	return &Instance{NewLazyProperties(props), class}
}
