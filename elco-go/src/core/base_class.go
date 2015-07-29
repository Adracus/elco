package core

import "reflect"

type BaseClass interface {
	name() string
	class() *Type
	properties() *Properties
	instanceProperties() *Properties
}

type UserClass struct {
	name               string
	class              *Type
	properties         *Properties
	instanceProperties *Properties
}

var Method = UserClass{
	name:               "Method",
	properties:         nil,
	instanceProperties: nil,
}

type MethodInstance struct {
	_properties *Properties
	_fn         *interface{}
}

func isFunc(fn *interface{}) bool {
	return reflect.TypeOf(fn).Kind() == reflect.Func
}

func NewMethodInstance(fn *interface{}) *MethodInstance {
	if !isFunc(fn) {
		panic("No valid function")
	}
	return &MethodInstance{Method.instanceProperties, fn}
}
