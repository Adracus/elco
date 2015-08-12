package core

var Object *ClassInstance

func init() {
	Object = NewClass("Object", nil, El)
}
