package core

import "fmt"

var Print *MethodInstance = NewMethodInstance(print)

func print(values ...BaseInstance) BaseInstance {
	for _, val := range values {
		fmt.Println(val.ToString().value)
	}
	return nil
}
