package core

var Unit *LazyClass
var unitType *LazyType
var UnitInstance BaseInstance

func init() {
	Unit = NewLazyClass(func() BaseClass {
		return NewClass("Unit", func() BaseClass {
			return Object.Class()
		}, El)
	})
	unitType = NewLazyType(func() *Type {
		return SimpleType(Object.Class())
	})

	UnitInstance = GetAndInvoke(Unit.Class(), "create")
}
