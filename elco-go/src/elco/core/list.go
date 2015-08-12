package core

var List = NewClass("List", Object, El)
var listType = SimpleType(List)
var El = &EmptyListInstance{NewLazyProperties()}

type BaseListInstance interface {
	Head() BaseInstance
	Tail() BaseListInstance
	IsEl() BoolInstance
}

type EmptyListInstance struct {
	*LazyProperties
}

func (list *EmptyListInstance) IsEl() BoolInstance {
	return True
}

func (list *EmptyListInstance) Head() BaseInstance {
	panic("Access empty list")
}

func (list *EmptyListInstance) Tail() BaseListInstance {
	panic("Access empty list")
}

type ListInstance struct {
	head BaseInstance
	tail BaseListInstance
	*Instance
}

func NewListInstance(elements ...BaseInstance) BaseListInstance {
	l := len(elements)
	if 0 == l {
		return El
	}
	var current BaseListInstance = El
	for i := l - 1; i >= 0; i-- {
		current = &ListInstance{elements[i], current, NewInstance(listType)}
	}
	return current
}

func (list *ListInstance) IsEl() BoolInstance {
	return False
}

func (list *ListInstance) Head() BaseInstance {
	return list.head
}

func (list *ListInstance) Tail() BaseListInstance {
	return list.tail
}
