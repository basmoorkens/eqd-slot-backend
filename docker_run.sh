docker stop eqd-slots-backend
docker rm eqd-slots-backend
docker run -v /var/log/eqd:/var/log --name="eqd-slots-backend" -p 8080:8080 -d eqd-slots
