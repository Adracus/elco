package core

type BaseClass interface {
	Name() string
	Class() *Type
	Super() BaseClass
	Props() *Properties
	InstanceProps() *Properties
}

func GetProp(class BaseClass, level, key string) {
	props := class.Props()
	props.Get(level, key)
}

func AddProp(class BaseClass, level, key string, inst Instance) {
	props := class.Props()
	props.Set(level, key, inst)
}

func DeleteProp(class BaseClass, level, key string) {
	props := class.Props()
	props.Delete(level, key)
}

func GetInstanceProp(class BaseClass, level, key string) {
	props := class.InstanceProps()
	props.Get(level, key)
}

func AddInstanceProp(class BaseClass, level, key string, inst Instance) {
	props := class.InstanceProps()
	props.Set(level, key, inst)
}

func DeleteInstanceProp(class BaseClass, level, key string) {
	props := class.InstanceProps()
	props.Delete(level, key)
}
