package core

type BaseInstance interface {
	Props() *Properties
	Class() *Type
}

type Instance struct {
	props *LazyProperties
	class *LazyType
}

func (inst *Instance) Props() *Properties {
	return inst.props.Props()
}

func (inst *Instance) Class() *Type {
	return inst.class.Class()
}

func Call(inst BaseInstance, name string, values ...BaseInstance) BaseInstance {
	callable, found := getMatching(inst, name)
	if !found {
		panic("Could not find " + name)
	}
	return apply(callable, inst, values...)
}

func Get(inst BaseInstance, name string) BaseInstance {
	v, ok := inst.Props().Find(name)
	if ok {
		return v
	}
	v, ok = classLookup(inst.Class().Class(), name)
	if ok {
		return v.(*UnboundMethodInstance).Bind(inst)
	}
	return nil
}

func apply(method BaseInstance, on BaseInstance, values ...BaseInstance) BaseInstance {
	unbound, ok := method.(*UnboundMethodInstance)
	if ok {
		return unbound.Invoke(on, values...)
	}
	return method.(*MethodInstance).Invoke(values...)
}

func getMatching(inst BaseInstance, name string) (BaseInstance, bool) {
	v, ok := inst.Props().Find(name)
	if ok {
		return v, true
	}
	v, ok = classLookup(inst.Class().Class(), name)
	return v, ok
}

func classLookup(class BaseClass, name string) (BaseInstance, bool) {
	v, ok := class.InstanceProps().Find(name)
	if ok {
		return v, true
	}
	if nil == class.Super() {
		return nil, false
	}
	return classLookup(class.Super(), name)
}

func Invoke(inst BaseInstance, level string, name string, values ...BaseInstance) BaseInstance {
	method, ok := inst.Class().Class().InstanceProps().Get(level, name)
	if !ok {
		panic("Instance does not contain method " + name + " at level " + level)
	}
	return method.(*UnboundMethodInstance).Invoke(inst, values...)
}

func NewInstance(classGenerator func() *Type) *Instance {
	return &Instance{NewLazyProperties(), NewLazyType(classGenerator)}
}
