package core

var Method = NewUserClass("Method", nil, NewProperties(), NewProperties())

type MethodInstance struct {
	props *Properties
	fn    *Function
}

func (method *MethodInstance) Apply(args ...BaseInstance) BaseInstance {
	return method.fn.Apply(args...)
}

var methodClass = SimpleType(Method)

func (method *MethodInstance) Class() *Type {
	return methodClass
}

func (method *MethodInstance) Props() *Properties {
	return method.props
}

func (method *MethodInstance) HashCode() *IntInstance {
	return NewIntInstance(0) // TODO: Implement hashcode
}

func NewMethodInstance(fn func(...BaseInstance) BaseInstance) *MethodInstance {
	return &MethodInstance{Method.Props(), &Function{fn}}
}

var UnboundMethod = NewUserClass("UnboundMethod", nil, NewProperties(), NewProperties())

type UnboundMethodInstance struct {
	props *Properties
	fn    *UnboundFunction
}

var unboundMethodClass = SimpleType(UnboundMethod)

func (method *UnboundMethodInstance) Class() *Type {
	return unboundMethodClass
}

func (method *UnboundMethodInstance) Props() *Properties {
	return method.props
}

func (method *UnboundMethodInstance) HashCode() *IntInstance {
	return NewIntInstance(0) // TODO: Implement hashcode
}

func NewUnboundMethodInstance(fn func(BaseInstance, ...BaseInstance) BaseInstance) *UnboundMethodInstance {
	return &UnboundMethodInstance{UnboundMethod.Props(), &UnboundFunction{fn}}
}
