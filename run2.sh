#!/bin/bash

if [ $1 -eq 1 ]; then 
		java -cp .:ASM_Test.jar:ASM/lib/json-20160212.jar main.DistinctItems ${@:2} 
elif [[ $1 -eq 2 ]]; then
	java -cp .:ASM_Test.jar:ASM/lib/json-20160212.jar main.Moments ${@:2} 
fi