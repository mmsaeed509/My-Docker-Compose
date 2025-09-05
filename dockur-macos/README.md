## 🛠️ How to Run It (Interactive, One Command)

Use `docker compose run` to get the interactive shell **immediately** (like `docker run -it`):

```bash
docker compose run macos
# OR
docker compose run --rm macos
```

> - 📁 The `./macos:/storage` volume will persist macOS VM data in a local `macos/` directory inside your current folder.
> - You can omit `--rm` if you want to keep the container after stopping.


- `--rm`: Automatically removes the container on exit (like your `docker run --rm`).
- This launches the macOS VM and gives you an attached terminal.
