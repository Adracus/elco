package core

type Properties struct {
	m *MapInstance
	*Instance
}

func (props *Properties) Map() *MapInstance {
	return props.m
}

func (props *Properties) innerMap(level string) *MapInstance {
	m, ok := props.Map().Get(level)
	if !ok {
		panic("Illegal level access '" + level + "'")
	}
	return m.(*MapInstance)
}

func (props *Properties) Put(level, name string, value BaseInstance) {
	inner := props.innerMap(level)
	inner.Put(name, value)
}

func (props *Properties) Find(name string) (BaseInstance, bool) {
	if res, ok := props.Get("public", name); ok {
		return res, true
	}
	if res, ok := props.Get("protected", name); ok {
		return res, true
	}
	if res, ok := props.Get("private", name); ok {
		return res, true
	}
	return nil, false
}

func (props *Properties) Get(level, name string) (BaseInstance, bool) {
	inner := props.innerMap(level)
	return inner.Get(name)
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
	m.Put("public", NewMapInstance())
	m.Put("protected", NewMapInstance())
	m.Put("private", NewMapInstance())
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
