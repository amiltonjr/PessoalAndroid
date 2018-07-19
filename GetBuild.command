#! /bin/bash
current_path="$(dirname "$BASH_SOURCE")"
cd $current_path
cp -R ./app/release/app-release.apk ./1.0.0/app.apk
