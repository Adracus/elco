package core

type Properties struct {
	m *MapInstance
	*Instance
}

func (props *Properties) Map() *MapInstance {
	return props.m
}

var PropertyClass = NewClass("Properties", Object, El)
var propertyType = SimpleType(PropertyClass)

func NewProperties() *Properties {
	m := NewMapInstance()
	Invoke(m, "public", "put", NewMapInstance())
	Invoke(m, "protected", "put", NewMapInstance())
	Invoke(m, "private", "put", NewMapInstance())
	return &Properties{m, NewInstance(func() *Type {
		return propertyType
	})}
}
