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

func GetAndInvoke(inst BaseInstance, name string, values ...BaseInstance) BaseInstance {
	return Invoke(Get(inst, name), values...)
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

func Invoke(inst BaseInstance, values ...BaseInstance) BaseInstance {
	method, ok := inst.(*MethodInstance)
	if !ok {
		panic("Cannot invoke instance " + GetAndInvoke(inst, "toString").(*StringInstance).Value())
	}
	return method.Invoke(values...)
}

func NewInstance(classGenerator func() *Type) *Instance {
	return &Instance{NewLazyProperties(), NewLazyType(classGenerator)}
}
