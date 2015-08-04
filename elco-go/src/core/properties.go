package core

type Properties struct {
	values map[string]map[string]BaseInstance
}

func NewProperties() *Properties {
	m := make(map[string]map[string]BaseInstance)
	m["public"] = make(map[string]BaseInstance)
	m["protected"] = make(map[string]BaseInstance)
	m["private"] = make(map[string]BaseInstance)
	return &Properties{m}
}

func (this *Properties) Merge(that *Properties) {
	for level, otherMap := range that.values {
		thisMap := this.values[level]
		for k, v := range otherMap {
			thisMap[k] = v
		}
	}
}

func (props *Properties) Inheritable() *Properties {
	newProps := NewProperties()
	newProps.SetAll("public", props.values["public"])
	newProps.SetAll("protected", props.values["protected"])
	return newProps
}

func (props *Properties) Get(level string, key string) BaseInstance {
	return props.values[level][key]
}

func (props *Properties) Set(level string, key string, value BaseInstance) {
	props.values[level][key] = value
}

func (props *Properties) SetAll(level string, m map[string]BaseInstance) {
	thisMap := props.values[level]
	for k, v := range m {
		thisMap[k] = v
	}
}

func (props *Properties) Delete(level string, key string) {
	delete(props.values[level], key)
}
