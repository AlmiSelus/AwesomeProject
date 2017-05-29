#!/usr/bin/env bash

if [ "$TRAVIS_BRANCH" == "$1" ] ; then
    mvn test -B || exit 1
fi

exit 0