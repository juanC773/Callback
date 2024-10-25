#!/bin/bash

hosts=("hgrid1" "hgrid2" "hgrid3" "hgrid4" "hgrid5" "hgrid6" "hgrid7" "hgrid8" "hgrid9" "hgrid10" "hgrid11" "hgrid12" "hgrid13" "hgrid14" "hgrid15")

for host in "${hosts[@]}"; do
  scp -o StrictHostKeyChecking=no -r . "swarch@x$host:~/Callback"
  swarch
  ssh "swarch@x$host"
  swarch
  cd Callback
  chhmod +x gradlew
  ./gradlew build
  cd ./client/build/libs
  mkdir client
  unzip client.jar -d ./client
  rm client.jar
  cd client
  sed -i 's/localhost/'$host'/g' client.config
  jar cvf client
  rm -r client
  java -jar Client.jar
  1000
  
done

for host in "${hosts[@]}"; do
  ssh "swarch@x$host" 
  swarch 
  rm -r Callback
done