package core

var Map *UserClass = NewUserClass("Map", Class, NewProperties(), NewProperties())
var mapType = SimpleType(Map)

type MapEntry struct {
	Key   BaseInstance
	Value BaseInstance
}

type MapInstance struct {
	values map[int]*MapEntry
	*LazyProperties
}

func NewDefaultMapInstance() *MapInstance {
	return &MapInstance{make(map[int]*MapEntry), NewDefaultLazyProperties()}
}

func (m *MapInstance) ToString() *StringInstance {
	return NewStringInstance("Map")
}

func (m *MapInstance) Class() *Type {
	return mapType
}

func (m *MapInstance) HashCode() *IntInstance {
	return NewIntInstance(0)
}

func (m *MapInstance) ForEach(f func(*MapEntry)) {
	for _, elem := range m.values {
		if elem != nil {
			f(elem)
		}
	}
}

func (m *MapInstance) Put(key, value BaseInstance) {
	hashCode := key.HashCode().value
	m.values[hashCode] = &MapEntry{key, value}
}

func (m *MapInstance) Remove(key BaseInstance) {
	hashCode := key.HashCode().value
	delete(m.values, hashCode)
}

func (m *MapInstance) Get(key BaseInstance) BaseInstance {
	hashCode := key.HashCode().value
	return m.values[hashCode].Value
}
