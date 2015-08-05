package core

type BaseFunction interface {
	Apply(...BaseInstance) BaseInstance
}

type UnboundFunction struct {
	fn func(BaseInstance, ...BaseInstance) BaseInstance
}

func (unbound *UnboundFunction) Apply(args ...BaseInstance) BaseInstance {
	return unbound.fn(args[0], args[1:]...)
}

type Function struct {
	fn func(...BaseInstance) BaseInstance
}

func (function *Function) Apply(args ...BaseInstance) BaseInstance {
	return function.fn(args...)
}

func (cf *UnboundFunction) Curry(b BaseInstance) *Function {
	return &Function{func(args ...BaseInstance) BaseInstance {
		return cf.fn(b, args...)
	}}
}
