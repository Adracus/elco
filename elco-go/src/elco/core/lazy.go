package core

import "reflect"

type Lazy struct {
	value interface{}
}

type generator struct {
	fn interface{}
}

func (lazy *Lazy) Value() interface{} {
	g, ok := lazy.value.(*generator)
	if ok {
		lazy.value = reflect.ValueOf(g).Call(make([]reflect.Value, 0))[0].Interface()
	}
	return lazy.value
}

func NewLazy(gen interface{}) *Lazy {
	return &Lazy{&generator{gen}}
}
