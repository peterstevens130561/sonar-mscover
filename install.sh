#!/bin/bash
set -x
DEST='C:/Program Files/sonarqube-5.5/extensions/plugins'
SRC=target
PLUGIN='sonar-mscover-plugin*.jar'
rm "$DEST"/$PLUGIN
cp $SRC/$PLUGIN "$DEST"