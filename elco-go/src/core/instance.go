package core

type Instance interface {
	Props() *Properties
	Class() *Type
}
