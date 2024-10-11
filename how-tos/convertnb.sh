#!/bin/bash
source .venv/bin/activate

# Loop through all Jupyter notebooks in the current directory
for notebook in *.ipynb; do
    # Convert the notebook to markdown
    jupyter nbconvert --to markdown "$notebook" --output-dir=src/site/markdown
done

echo "Conversion complete!"
