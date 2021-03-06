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

func Find(inst BaseInstance, name string) (BaseInstance, bool) {
	if v, ok := inst.Props().Find(name); ok {
		return v, true
	}

	if v, ok := classLookup(inst.Class().Class(), name); ok {
		return v.(*UnboundMethodInstance).Bind(inst), true
	}
	return nil, false
}

func Get(inst BaseInstance, name string) BaseInstance {
	v, ok := Find(inst, name)
	if !ok {
		panic("Could not find attribute '" + name + "'")
	}
	return v
}

func classLookup(class BaseClass, name string) (BaseInstance, bool) {
	if v, ok := class.InstanceProps().Find(name); ok {
		return v, true
	}
	if class == class.Super() {
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
