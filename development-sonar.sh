#!/usr/bin/env bash

if [ "$TRAVIS_BRANCH" == "$1" ] ; then
    mvn package -Pci -B || exit 1
    sonar-scanner || exit 1
fi

exit 0