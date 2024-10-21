#!/bin/bash

hosts=("xhgrid1" "xhgrid2" "xhgrid3" "xhgrid4" "xhgrid5" "xhgrid6" "xhgrid7" "xhgrid8" "xhgrid9" "xhgrid10" "xhgrid11" "xhgrid12" "xhgrid13" "xhgrid14" "xhgrid15")


for host in "${hosts[@]}"; do
  scp -o StrictHostKeyChecking=no -r . swarch@xhgrid2:~/Callback
  swarch
  ssh swarch@xhgrid2
  swarch
  cd Callback
  chhmod +x gradlew
  ./gradlew build
  cd ./client/build/libs
  mkdir client
  unzip client.jar -d ./client
  rm client.jar
  cd Client
  "vim"
  zip
  jar cvf Client.jar
  rm -r Client
  java -jar Client.jar
  !1000
  
done