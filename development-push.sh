#!/usr/bin/env bash

if [ "$TRAVIS_BRANCH" == "$1" ] ; then
    mvn clean wro4j:run || exit 1
    chmod 777 merge.sh
    ./merge.sh $GIT_USER $GIT_PASS development master || exit 1
fi

exit 0