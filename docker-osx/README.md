### Initial setup

- Install Dependencies

```bash
sudo pacman -S qemu libvirt dnsmasq virt-manager bridge-utils flex bison iptables-nft edk2-ovmf

```

- Then, enable libvirt and load the KVM kernel module

```bash
sudo systemctl enable --now libvirtd
sudo systemctl enable --now virtlogd

echo 1 | sudo tee /sys/module/kvm/parameters/ignore_msrs

sudo modprobe kvm
```


### Run Catalina

```bash
cd catalina 
docker compose run docker-osx
```
---

#### Fix Display Authorization

```bash
xhost +local:
export DISPLAY=:0
```