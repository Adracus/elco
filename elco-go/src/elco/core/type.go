package core

type Type struct {
	class  BaseClass
	mixins []BaseClass
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
