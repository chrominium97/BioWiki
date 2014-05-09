#!/bin/sh

adb shell su -c "echo .dump | sqlite3 /data/data/kr.kdev.dg1s.biowiki/databases/wordpress"
