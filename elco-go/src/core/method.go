package core

import "reflect"

var Method = NewUserClass("Method", nil, NewProperties(), NewProperties())

type MethodInstance struct {
	props *Properties
	fn    interface{}
}

func (method *MethodInstance) Class() *Type {
	return SimpleType(Method)
}

func (method *MethodInstance) Props() *Properties {
	return nil
}

func isFunc(fn interface{}) bool {
	return reflect.TypeOf(fn).Kind() == reflect.Func
}

func NewMethodInstance(fn interface{}) *MethodInstance {
	if !isFunc(fn) {
		panic("Illegal argument: fn is no function")
	}
	return &MethodInstance{Method.Props(), fn}
}
