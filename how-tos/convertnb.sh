#!/bin/bash

# Activate the virtual environment
source .venv/bin/activate

# Set the notebook argument, defaulting to all .ipynb files if not provided
NB="${1:-*.ipynb}"

# Convert the specified notebook(s) to markdown
jupyter nbconvert --to markdown $NB --output-dir=src/site/markdown

echo "Conversion complete!"