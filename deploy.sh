#!/bin/sh



# get comment
comment="$1"

sbt fullOptJS

if [ "$comment" == "" ]; then
comment="push form CI"
echo "no comment specified to deploy, using default : $comment"
fi

projectName="scalajs-offline"

ghPagesPath="/Users/chandrasekharkode/Desktop/Kode/Programming/scalaprojects/chandu0101.github.io"

projectPath=${ghPagesPath}/${projectName}

mkdir -p ${projectPath}/js

cp index.html ${projectPath}

cp offline.js ${projectPath}

cp  js/scalajs-offline-opt.js ${projectPath}/js/

cp  js/scalajs-offline-jsdeps.js ${projectPath}/js/

cp  js/serviceworker-cache-polyfill.js ${projectPath}/js/

cd ${ghPagesPath}

git add ${projectName}

git commit -m "$comment"

git push