package core

type Properties struct {
	m *MapInstance
	*Instance
}

func (props *Properties) Map() *MapInstance {
	return props.m
}

func (props *Properties) Put(level, name string, value BaseInstance) {
	innerMap := props.Map().Get(level).(*MapInstance)
	innerMap.Put(name, value)
}

var PropertyClass *LazyClass
var propertyType *LazyType

func init() {
	PropertyClass = NewLazyClass(func() BaseClass {
		return NewClass("Properties", func() BaseClass {
			return Object.Class()
		}, El)
	})
	propertyType = NewLazyType(func() *Type {
		return SimpleType(PropertyClass.Class())
	})
}

func NewProperties() *Properties {
	m := NewMapInstance()
	Invoke(m, "public", "put", NewMapInstance())
	Invoke(m, "protected", "put", NewMapInstance())
	Invoke(m, "private", "put", NewMapInstance())
	return &Properties{m, NewInstance(func() *Type {
		return propertyType.Class()
	})}
}

type LazyProperties struct {
	value *Lazy
}

func NewLazyProperties() *LazyProperties {
	return &LazyProperties{NewLazy(func() *Properties {
		return NewProperties()
	})}
}

func (lazy *LazyProperties) Props() *Properties {
	return lazy.value.Value().(*Properties)
}
