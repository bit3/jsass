#!/bin/bash

set -euo pipefail

if [[ -z "$(which sassc)" ]]; then
    echo "sassc not found, please install sassc first"
    exit 1
fi

if [[ -z "$(which sass-convert)" ]]; then
    echo "sass-convert not found, please install sass-convert first"
    exit 1
fi

cd `dirname $0`;
cd ../src/test/resources;

# convert input file
echo "convert scss input to sass input"
sass-convert scss/input.scss sass/input.sass
sass-convert scss/inc/mixins.scss sass/inc/mixins.sass
#sass-convert scss/src/include.scss sass/src/include.sass
#sass-convert scss/test/functions.scss sass/test/functions.sass

# scss/nested/output-with-map.css
echo "compile scss/nested/output-with-map.css"
sassc -t nested -I scss/inc -I scss/src -I scss/test -m scss/input.scss scss/nested/output-with-map.css

# scss/nested/output-without-map.css
echo "compile scss/nested/output-without-map.css"
sassc -t nested -I scss/inc -I scss/src -I scss/test scss/input.scss scss/nested/output-without-map.css

# scss/compressed/output-with-map.css
echo "compile scss/compressed/output-with-map.css"
sassc -t compressed -I scss/inc -I scss/src -I scss/test -m scss/input.scss scss/compressed/output-with-map.css

# scss/compressed/output-without-map.css
echo "compile scss/compressed/output-without-map.css"
sassc -t compressed -I scss/inc -I scss/src -I scss/test scss/input.scss scss/compressed/output-without-map.css

# sass/nested/output-with-map.css
echo "compile sass/nested/output-with-map.css"
sassc -t nested -I sass/inc -I scss/src -I scss/test -m sass/input.sass sass/nested/output-with-map.css

# sass/nested/output-without-map.css
echo "compile sass/nested/output-without-map.css"
sassc -t nested -I sass/inc -I scss/src -I scss/test sass/input.sass sass/nested/output-without-map.css

# sass/compressed/output-with-map.css
echo "compile sass/compressed/output-with-map.css"
sassc -t compressed -I sass/inc -I scss/src -I scss/test -m sass/input.sass sass/compressed/output-with-map.css

# sass/compressed/output-without-map.css
echo "compile sass/compressed/output-without-map.css"
sassc -t compressed -I sass/inc -I scss/src -I scss/test sass/input.sass sass/compressed/output-without-map.css

find -name '*.css.map' -delete
