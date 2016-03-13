#!/bin/bash

set -euo pipefail

if [[ -d .gradle ]]; then
  rm -rf .gradle
fi

gradle clean test
