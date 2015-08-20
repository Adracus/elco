package core

var Unit *LazyClass
var unitType *LazyType
var UnitInstance BaseInstance

func InitUnit() {
	Unit = NewLazyClass(func() BaseClass {
		return NewClass("Unit", func() BaseClass {
			return Object.Class()
		}, El)
	})

	unitType = NewLazyType(func() *Type {
		return SimpleType(Unit.Class())
	})

	UnitInstance = GetAndInvoke(Unit.Class(), "create")
}
