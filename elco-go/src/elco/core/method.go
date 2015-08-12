package core

import "reflect"

var Method = NewClass("Method", Object, El)

type MethodInstance struct {
	*Instance
	fn interface{}
}

var methodType = SimpleType(Method)

func (method *MethodInstance) Fn() interface{} {
	return method.fn
}

func NewMethodInstance(fn interface{}) *MethodInstance {
	return &MethodInstance{NewInstance(methodType), fn}
}

var UnboundMethod = NewClass("UnboundMethod", Object, El)

type UnboundMethodInstance struct {
	*Instance
	fn interface{}
}

var unboundMethodType = SimpleType(UnboundMethod)

func (method *UnboundMethodInstance) Fn() interface{} {
	return method.fn
}

func NewUnboundMethodInstance(fn interface{}) *UnboundMethodInstance {
	return &UnboundMethodInstance{NewInstance(unboundMethodType), fn}
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
