package core

var Object *UserClass

func init() {
	Object = NewUserClass("Object", nil, NewProperties(), NewProperties())
}
