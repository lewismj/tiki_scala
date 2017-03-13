#!/bin/bash

sbt_cmd="sbt ++$TRAVIS_SCALA_VERSION"
coverage="(export SCOVERAGEON=true; $sbt_cmd coverage tests/test coverageReport && bash <(curl -s https://codecov.io/bash) )"
all="$sbt_cmd clean compile" # && $sbt_cmd tests/test"

run_cmd="$coverage && $all"
eval $run_cmd
