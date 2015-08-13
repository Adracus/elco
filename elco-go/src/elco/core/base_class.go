package core

type BaseClass interface {
	Name() *StringInstance
	Super() BaseClass
	Class() *Type
	Props() *Properties
	Generics() BaseListInstance
}

type ClassInstance struct {
	*LazyStringInstance
	*Instance
	*LazySuper
	generics BaseListInstance
}

type LazySuper struct {
	lazy *Lazy
}

func (lazy *LazySuper) Super() BaseClass {
	return lazy.lazy.Value().(BaseClass)
}

func NewLazySuper(gen func() BaseClass) *LazySuper {
	return &LazySuper{NewLazy(gen)}
}

func (class *ClassInstance) Name() *StringInstance {
	return class.String()
}

func (class *ClassInstance) Generics() BaseListInstance {
	return class.generics
}

func NewClass(name string, super func() BaseClass, generics BaseListInstance) *ClassInstance {
	_name := NewLazyString(name)
	_inst := NewInstance(func() *Type {
		return SimpleType(Class)
	})
	_super := NewLazySuper(super)
	return &ClassInstance{_name, _inst, _super, generics}
}

type LazyClass struct {
	lazy *Lazy
}

func Define(class BaseClass, level, name string, fn interface{}) {
	class.Props().Put(level, name, NewUnboundMethodInstance(fn))
}

func NewLazyClass(gen func() BaseClass) *LazyClass {
	return &LazyClass{NewLazy(gen)}
}

func (lc *LazyClass) Class() BaseClass {
	return lc.lazy.Value().(BaseClass)
}
