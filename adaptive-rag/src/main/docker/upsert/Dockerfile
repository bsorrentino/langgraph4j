FROM python:latest

COPY requirements.txt /tmp

RUN pip install --no-cache-dir --user -r /tmp/requirements.txt

WORKDIR /workspace

COPY index.py /workspace

CMD ["python", "index.py"]
