package core

type Properties struct {
	values map[string]map[string]Instance
}

func NewProperties() *Properties {
	m := make(map[string]map[string]Instance)
	m["public"] = make(map[string]Instance)
	m["protected"] = make(map[string]Instance)
	m["private"] = make(map[string]Instance)
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

func (props *Properties) Get(level string, key string) Instance {
	return props.values[level][key]
}

func (props *Properties) Set(level string, key string, value Instance) {
	props.values[level][key] = value
}

func (props *Properties) SetAll(level string, m map[string]Instance) {
	thisMap := props.values[level]
	for k, v := range m {
		thisMap[k] = v
	}
}

func (props *Properties) Delete(level string, key string) {
	delete(props.values[level], key)
}
