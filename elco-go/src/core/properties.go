package core

type Properties struct {
	values *MapInstance
}

func NewProperties() *Properties {
	m := NewDefaultMapInstance()
	m.Put(NewStringInstance("public"), NewDefaultMapInstance())
	m.Put(NewStringInstance("protected"), NewDefaultMapInstance())
	m.Put(NewStringInstance("private"), NewDefaultMapInstance())
	return &Properties{m}
}

func (this *Properties) Merge(that *Properties) {
	that.values.ForEach(func(m *MapEntry) {
		this.values.Put(m.Key, m.Value)
	})
}

func (props *Properties) Inheritable() *Properties {
	newProps := NewProperties()
	newProps.SetAll("public", props.values.Get(NewStringInstance("public")).(*MapInstance))
	newProps.SetAll("private", props.values.Get(NewStringInstance("private")).(*MapInstance))
	return newProps
}

func (props *Properties) Get(level string, key string) BaseInstance {
	return props.values.Get(NewStringInstance(level)).(*MapInstance).Get(NewStringInstance(key))
}

func (props *Properties) Set(level string, key string, value BaseInstance) {
	props.values.Get(NewStringInstance(level)).(*MapInstance).Put(NewStringInstance(key), value)
}

func (props *Properties) SetAll(level string, m *MapInstance) {
	thisMap := props.values.Get(NewStringInstance(level)).(*MapInstance)
	m.ForEach(func(e *MapEntry) {
		thisMap.Put(e.Key, e.Value)
	})
}

func (props *Properties) Delete(level string, key string) {
	props.values.Get(NewStringInstance(level)).(*MapInstance).Remove(NewStringInstance(key))
}
