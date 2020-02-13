#!/bin/bash
echo "Parando... docker-compose down "
sudo docker-compose down
echo "Deseja limpar as imagens? (S/N)"
read isLimpar

if [ "$isLimpar" = "S" ] || [ "$isLimpar" = "s" ]
then
	sudo docker rmi $(sudo docker images -q)
fi

echo "Fim"

