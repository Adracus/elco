package core

var Map = NewClass("Map", Object, El)
var mapType = SimpleType(Map)

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
	return &MapInstance{make(map[int]*MapEntry), NewInstance(func() *Type {
		return mapType
	})}
}

func init() {

}
