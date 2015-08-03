package core

import "reflect"

var Method = NewUserClass("Method", NewProperties(), NewProperties())

type MethodInstance struct {
	class *Type
	props *Properties
	fn    *interface{}
}

func isFunc(fn interface{}) bool {
	return reflect.TypeOf(fn).Kind() == reflect.Func
}

func NewMethodInstance(fn *interface{}) *MethodInstance {
	if !isFunc(fn) {
		panic("Illegal argument: fn is no function")
	}
	return &MethodInstance{SimpleType(Method), Method.Props(), fn}
}
