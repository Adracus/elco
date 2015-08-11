package core

var List = NewUserClass("List", Object, NewProperties(), NewProperties())
var listType = SimpleType(List)
var El = &EmptyListInstance{NewDefaultLazyProperties()}

type BaseListInstance interface {
	Head() BaseInstance
	Tail() BaseListInstance
}

type EmptyListInstance struct {
	*LazyProperties
}

func (list *EmptyListInstance) Head() BaseInstance {
	panic("Access empty list")
}

func (list *EmptyListInstance) Tail() BaseListInstance {
	panic("Access empty list")
}

func (m *EmptyListInstance) ToString() *StringInstance {
	return NewStringInstance("List")
}

func (m *EmptyListInstance) Class() *Type {
	return listType
}

func (m *EmptyListInstance) HashCode() *IntInstance {
	return NewIntInstance(0)
}

type ListInstance struct {
	head BaseInstance
	tail BaseListInstance
	*LazyProperties
}

func NewListInstance(elements ...BaseInstance) BaseListInstance {
	l := len(elements)
	if 0 == l {
		return El
	}
	var current BaseListInstance = El
	for i := l - 1; i >= 0; i-- {
		current = &ListInstance{elements[i], current, NewDefaultLazyProperties()}
	}
	return current
}

func (list *ListInstance) Head() BaseInstance {
	return list.head
}

func (list *ListInstance) Tail() BaseListInstance {
	return list.tail
}

func (m *ListInstance) ToString() *StringInstance {
	return NewStringInstance("List")
}

func (m *ListInstance) Class() *Type {
	return listType
}

func (m *ListInstance) HashCode() *IntInstance {
	return NewIntInstance(0)
}
