#!/bin/bash
set -x
DEST='C:/users/stevpet/sonarqube-5.1.2/extensions/plugins'
SRC=target
PLUGIN='sonar-mscover-plugin*.jar'
rm "$DEST"/$PLUGIN
cp $SRC/$PLUGIN "$DEST"