# How to

- Step 14 of adding new languages says you need to redraw accuracy plots
- But I could not find the gradle task or anything to draw the plots
- I had to go digging in past PRs to find the scripts and then ran them myself
- I already had uv and python installed
- If not, install them yourself https://docs.astral.sh/uv/. The docs are self explanatory
- I initialized a python project with `uv init` from the project root
- I added the packages needed with `uv add matplotlib seaborn numpy pandas`
- Then in the commandline I ran the script with `uv run python-scripts/draw_accuracy_plots.py`
- I don't know why the scripts and the gradle task were removed but I'm just adding it back because I think it's nice to have them
- Afterwards I deleted all the stuff created by uv and left the python scripts
