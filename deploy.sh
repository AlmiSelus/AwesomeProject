#!/bin/bash
rev=$(git rev-parse --short HEAD)
git config user.name "JanuszCebulaJ"
git config user.password "OnionJava"
git add .
git reset .travis.yml
git commit -m "Committed automatically by Travis at ${rev}"
git push origin HEAD:$1