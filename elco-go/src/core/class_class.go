package core

type ClassClass struct {
	_name               string
	_class              *Type
	_properties         *Properties
	_instanceProperties *Properties
}

func (c ClassClass) name() string {
	return c._name
}

func (c ClassClass) class() *Type {
	return c._class
}

func (c ClassClass) properties() *Properties {
	return c._properties
}

func (c ClassClass) instanceProperties() *Properties {
	return c._instanceProperties
}

var Class ClassClass

func init() {
	Class = ClassClass{
		_name:               "Class",
		_properties:         nil,
		_instanceProperties: nil,
	}
}
