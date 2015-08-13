package core

type Properties struct {
	m *MapInstance
	*Instance
}

func (props *Properties) Map() *MapInstance {
	return props.m
}

var PropertyClass = NewClass("Properties", func() BaseClass {
	return Object
}, El)
var propertyType = NewLazyType(func() *Type {
	return SimpleType(PropertyClass)
})

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
