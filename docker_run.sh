sudo docker stop eqd-slots-backend
sudo docker rm eqd-slots-backend
sudo docker run --name="eqd-slots-backend" -p 8080:8080 -d eqd-slots

