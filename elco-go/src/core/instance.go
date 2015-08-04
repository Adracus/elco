package core

type BaseInstance interface {
	Props() *Properties
	Class() *Type
}
