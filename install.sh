#!/bin/bash
set -x
DEST='C:/Program Files/sonarqube-4.5.1/extensions/plugins'
SRC=target
PLUGIN='sonar-mscover-plugin*.jar'
rm "$DEST"/$PLUGIN
cp $SRC/$PLUGIN "$DEST"