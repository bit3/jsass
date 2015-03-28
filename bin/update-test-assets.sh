#!/bin/bash

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
sass-convert scss/compile.scss sass/compile.sass

# scss/nested/output-with-map.css
echo "compile scss/nested/output-with-map.css"
sassc -t nested -m scss/compile.scss scss/nested/output-with-map.css

# scss/nested/output-without-map.css
echo "compile scss/nested/output-without-map.css"
sassc -t nested scss/compile.scss scss/nested/output-without-map.css

# scss/compressed/output-with-map.css
echo "compile scss/compressed/output-with-map.css"
sassc -t compressed -m scss/compile.scss scss/compressed/output-with-map.css

# scss/compressed/output-without-map.css
echo "compile scss/compressed/output-without-map.css"
sassc -t compressed scss/compile.scss scss/compressed/output-without-map.css

# sass/nested/output-with-map.css
echo "compile sass/nested/output-with-map.css"
sassc -t nested -m sass/compile.sass sass/nested/output-with-map.css

# sass/nested/output-without-map.css
echo "compile sass/nested/output-without-map.css"
sassc -t nested sass/compile.sass sass/nested/output-without-map.css

# sass/compressed/output-with-map.css
echo "compile sass/compressed/output-with-map.css"
sassc -t compressed -m sass/compile.sass sass/compressed/output-with-map.css

# sass/compressed/output-without-map.css
echo "compile sass/compressed/output-without-map.css"
sassc -t compressed sass/compile.sass sass/compressed/output-without-map.css
