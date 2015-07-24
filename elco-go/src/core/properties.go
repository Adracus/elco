package core

type Properties struct {
	public    map[string]Instance
	protected map[string]Instance
	private   map[string]Instance
}

func NewProperties() *Properties {
	return &Properties{
		make(map[string]Instance),
		make(map[string]Instance),
		make(map[string]Instance)}
}
