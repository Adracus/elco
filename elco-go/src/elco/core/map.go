package core

var Map *LazyClass
var mapType *LazyType

func init() {
	Map = NewLazyClass(func() BaseClass {
		return NewClass("Map", func() BaseClass {
			return Object
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

func NewMapInstance() *MapInstance {
	return &MapInstance{make(map[int]*MapEntry), NewInstance(mapType.Class)}
}

func init() {

}
