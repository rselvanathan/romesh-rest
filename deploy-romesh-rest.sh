docker pull rselvanathan/romesh-rest:latest
isImageRunning=$(docker inspect -f {{.State.Running}} romesh-rest 2> /dev/null)
if [ "$isImageRunning" = "true" ]; then
	echo "Removing romesh-rest container"
	docker stop romesh-rest
	docker rm romesh-rest
fi
value=$(docker images -q --filter "dangling=true")
if [ "$value" = "" ]; then
	echo "No Dangling Images"
else
	echo "Removing Dangling Images"
 	docker images -q --filter "dangling=true" | xargs docker rmi
fi
docker run -d -m 180m --name romesh-rest \
-e VIRTUAL_HOST=api.romeshselvan.com \
-e LETSENCRYPT_HOST=api.romeshselvan.com \
-e LETSENCRYPT_EMAIL= \
-e JWTSECRET= \
-e AWS_ACCESS_KEY_ID= \
-e AWS_SECRET_ACCESS_KEY= \
-e AWS_EMAIL_SNS_TOPIC= \
-e APP_TYPE=ROMCHARM \
-e PLAY_SECRET= \
-it rselvanathan/romesh-rest:latest