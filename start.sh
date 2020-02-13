#!/bin/bash
echo "Iniciando ..."
sudo sysctl -w vm.max_map_count=262144
echo "Deseja copiar arquivos csv? (S/N)"
read isCopiar

if [ "$isCopiar" = "S" ] || [ "$isCopiar" = "s" ]
then
	cp -rf /home/sergio/Downloads/files/v1 /home/sergio/workspace/code/java/scd-batch/src/main/resources/input/
	cp -rf /home/sergio/Downloads/files/v2 /home/sergio/workspace/code/java/scd-batch/src/main/resources/input/
fi

mvn clean install
sudo docker-compose up
echo "Fim"


