# Portainer Deployment with Docker Compose

This project provides a simple way to deploy [Portainer CE](https://www.portainer.io/) using Docker Compose.  
Portainer is a lightweight management UI that allows you to easily manage your Docker environments (hosts or Swarm clusters).

---

## 📦 Requirements

- Docker 
- Docker Compose 

```bash
sudo pacman -S docker docker-compose
```

---

## 🚀 Quick Start

1. **Clone this repository:**

```bash
cd Portainer
```
2. **Run The Composer:**

```bash
docker compose up -d
```

---

🔐 Access Portainer

[**`https://localhost:9443`**](https://localhost:9443)

📁 Volumes Used

- `portainer_data`: Persistent volume to store Portainer's data.

- `/var/run/docker.sock`: Allows Portainer to communicate with the Docker daemon.

---

🛑 Stop and Remove

- To stop and remove Portainer:

```bash
docker compose down

```

- To remove everything including the volume:

```bash
docker compose down -v

```

---

### Add Podman environments

```bash
sudo systemctl enable --now podman.socket

sudo podman volume create portainer

sudo podman run -d \
-p 9001:9001 \
--name portainer_agent \
--restart=always \
--privileged \
-v /run/podman/podman.sock:/var/run/docker.sock \
-v /var/lib/containers/storage/volumes:/var/lib/docker/volumes \
-v /:/host \
docker.io/portainer/agent:2.27.8

```
