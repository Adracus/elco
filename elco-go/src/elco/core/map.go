package core

var Map *LazyClass
var mapType *LazyType

func init() {
	Map = NewLazyClass(func() BaseClass {
		return NewClass("Map", func() BaseClass {
			return Object.Class()
		}, El)
	})
	mapType = NewLazyType(func() *Type {
		return SimpleType(Map.Class())
	})
}

type MapEntry struct {
	Key   BaseInstance
	Value BaseInstance
}

type MapInstance struct {
	values map[int]*MapEntry
	*Instance
}

func (m *MapInstance) Values() map[int]*MapEntry {
	return m.values
}

func (m *MapInstance) Put(key string, value BaseInstance) {
	keyString := NewStringInstance(key)
	m.values[keyString.HashCode()] = &MapEntry{keyString, value}
}

func (m *MapInstance) Get(key string) (BaseInstance, bool) {
	var result BaseInstance
	entry, ok := m.values[HashString(key)]
	if !ok {
		return result, false
	}
	return entry.Value, true
}

func NewMapInstance() *MapInstance {
	return &MapInstance{make(map[int]*MapEntry), NewInstance(mapType.Class)}
}
