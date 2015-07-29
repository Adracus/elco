package core

type Type struct {
	class  *BaseClass
	mixins *[]*BaseClass
}

func SimpleType(class *BaseClass) *Type {
	return &Type{class, nil}
}
