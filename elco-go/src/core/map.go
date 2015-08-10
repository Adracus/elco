package core

var Map *UserClass = NewUserClass("Map", Class, NewProperties(), NewProperties())
var mapType = SimpleType(Map)

const defaultSize = 16

type MapEntry struct { // TODO: Improve shitty linear map implementation
	Key   BaseInstance
	Value BaseInstance
}

type MapInstance struct {
	values []*MapEntry
	props  *Properties
}

func NewDefaultMapInstance() *MapInstance {
	return &MapInstance{make([]*MapEntry, defaultSize), NewProperties()}
}

func (m *MapInstance) Class() *Type {
	return mapType
}

func (m *MapInstance) HashCode() *IntInstance {
	return NewIntInstance(0)
}

func (m *MapInstance) Props() *Properties {
	return m.props
}

func (m *MapInstance) ForEach(f func(*MapEntry)) {
	for _, elem := range m.values {
		if elem != nil {
			f(elem)
		}
	}
}

func (m *MapInstance) Put(key, value BaseInstance) {
	for i, elem := range m.values {
		if elem == nil {
			m.values[i] = &MapEntry{key, value}
			return
		} else if elem.Key.HashCode() == key.HashCode() {
			return
		}
	}
	panic("Map full")
}

func (m *MapInstance) Remove(key BaseInstance) {
	for i, elem := range m.values {
		if elem.Key.HashCode() == key.HashCode() {
			m.values[i] = nil
			return
		}
	}
}

func (m *MapInstance) Get(key BaseInstance) BaseInstance {
	for _, elem := range m.values {
		if elem != nil && elem.Key.HashCode() == key.HashCode() {
			return elem.Value
		}
	}
	return nil
}
