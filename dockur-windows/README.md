## 🛠️ How to Run It (Interactive, One Command)

Use `docker compose run` to get the interactive shell **immediately** (like `docker run -it`):

```bash
docker compose run windows
# OR
docker compose run --rm windows
```

> - 📁 The `./windows:/storage` volume will persist windows VM data in a local `windows/` directory inside your current folder.
> - You can omit `--rm` if you want to keep the container after stopping.


- `--rm`: Automatically removes the container on exit (like your `docker run --rm`).
- This launches the windows VM and gives you an attached terminal.
