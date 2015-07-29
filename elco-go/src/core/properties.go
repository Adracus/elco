package core

type Properties struct {
	public    *map[string]Instance
	protected *map[string]Instance
	private   *map[string]Instance
}

func NewProperties() *Properties {
	public := make(map[string]Instance)
	protected := make(map[string]Instance)
	private := make(map[string]Instance)
	return &Properties{&public, &protected, &private}
}

func get(props *Properties, level string, key string) Instance {
	var m *map[string]Instance
	switch level {
	case "private":
		m = props.private
	case "protected":
		m = props.protected
	case "public":
		m = props.public
	default:
		panic("Invalid access level " + level)
	}
	return (*m)[key]
}
