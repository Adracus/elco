package core

type Type struct {
	class  BaseClass
	mixins []BaseClass
}

type LazyType struct {
	typeGenerator func() *Type
	t             *Type
}

func NewLazyType(typeGenerator func() *Type) *LazyType {
	return &LazyType{typeGenerator, nil}
}

func (l *LazyType) Class() *Type {
	if l.t == nil {
		l.t = l.typeGenerator()
	}
	return l.t
}

func SimpleType(class BaseClass) *Type {
	return &Type{class, nil}
}

func (t *Type) Class() BaseClass {
	return t.class
}

type GenericType struct {
	assignableTo BaseClass
}

func NewGenericType(assignableTo BaseClass) *GenericType {
	return &GenericType{assignableTo}
}
