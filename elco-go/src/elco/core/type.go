package core

type Type struct {
	class  BaseClass
	mixins []BaseClass
}

type LazyType struct {
	lazy *Lazy
}

func NewLazyType(typeGenerator func() *Type) *LazyType {
	return &LazyType{NewLazy(typeGenerator)}
}

func (l *LazyType) Class() *Type {
	return l.lazy.Value().(*Type)
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
