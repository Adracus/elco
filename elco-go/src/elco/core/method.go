package core

import "reflect"

var Method = NewClass("Method", func() BaseClass {
	return Object
}, El)

type MethodInstance struct {
	*Instance
	fn interface{}
}

var methodType = NewLazyType(func() *Type {
	return SimpleType(Method)
})

func (method *MethodInstance) Fn() interface{} {
	return method.fn
}

func NewMethodInstance(fn interface{}) *MethodInstance {
	return &MethodInstance{NewInstance(func() *Type {
		return methodType.Class()
	}), fn}
}

var UnboundMethod = NewClass("UnboundMethod", func() BaseClass {
	return Object
}, El)

type UnboundMethodInstance struct {
	*Instance
	fn interface{}
}

var unboundMethodType = NewLazyType(func() *Type {
	return SimpleType(UnboundMethod)
})

func (method *UnboundMethodInstance) Fn() interface{} {
	return method.fn
}

func NewUnboundMethodInstance(fn interface{}) *UnboundMethodInstance {
	return &UnboundMethodInstance{NewInstance(func() *Type {
		return unboundMethodType.Class()
	}), fn}
}

func (method *UnboundMethodInstance) Invoke(inst BaseInstance, values ...BaseInstance) BaseInstance {
	params := toIn(inst, values)
	return reflect.ValueOf(method.fn).Call(params)[0].Interface().(BaseInstance)
}

func toIn(inst BaseInstance, args []BaseInstance) []reflect.Value {
	vs := make([]reflect.Value, len(args)+1)
	vs[0] = reflect.ValueOf(inst)
	for i := range args {
		vs[i+1] = reflect.ValueOf(args[i])
	}
	return vs
}
