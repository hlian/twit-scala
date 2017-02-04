#!/bin/bash

# the scala cucumber plugin doesn't have a hook to kill the browser at the end
# of all tests. this helps prevent a bunch of instances from being created, and
# should be called at the beginning of each cucumber run

killall chromedriver
ps -e | grep 'test-type=webdriver' | grep -i 'chrome' | awk '{ print $1 }' | xargs kill -9
