package core

type ScopeNode struct {
	next     *ScopeNode
	previous *ScopeNode
	values   BaseInstance
}

type ScopeTree struct {
	top    *ScopeNode
	bottom *ScopeNode
}

var Scope *ScopeTree

func init() {
	InitUnit()
	Scope = newScopeTree()

	Scope.Set("Object", Object.Class())
	Scope.Set("Bool", Bool.Class())
	Scope.Set("Int", Int.Class())
	Scope.Set("Map", Map.Class())
	Scope.Set("println", Println)
	Scope.Set("print", Print)
}

func newScopeTree() *ScopeTree {
	entry := &ScopeNode{nil, nil, GetAndInvoke(Module.Class(), "create")}
	return &ScopeTree{entry, entry}
}

func (scope *ScopeTree) Get(name string) BaseInstance {
	current := scope.top
	nameInst := NewStringInstance(name)
	for current != nil {
		if GetAndInvoke(current.values, "contains", nameInst).(BoolInstance).Value() {
			return GetAndInvoke(current.values, "get", nameInst)
		}
		current = current.previous
	}
	panic("Scope does not contain value '" + name + "'")
}

func (scope *ScopeTree) Set(name string, inst BaseInstance) BaseInstance {
	current := scope.top
	nameInst := NewStringInstance(name)
	for current != nil {
		if GetAndInvoke(current.values, "contains", nameInst).(BoolInstance).Value() {
			GetAndInvoke(current.values, "set", nameInst, inst) // Override in found scope
			return inst
		}
		current = current.previous
	}
	GetAndInvoke(scope.top.values, "set", nameInst, inst) // Set in current scope
	return inst
}

func (scope *ScopeTree) Push() {
	entry := &ScopeNode{nil, nil, GetAndInvoke(Module.Class(), "create")}
	oldTop := scope.top
	oldTop.next = entry
	entry.previous = oldTop
	scope.top = entry
}

func (scope *ScopeTree) Pop() {
	oldTop := scope.top
	scope.top = oldTop.previous
	scope.top.next = nil
}
