#import "@romeo/romeostyle:0.0.1": *
#let colour-scheme = "bluegrey"
#show: schooldoc.with(
  author: ("Carlos Romeo Clemente Del Castillo III","Dale Cofulan Kerchaten"),
  section: "CC22-1H"+sub("A"),
  subject: "Introduction to Platform Technologies", 
  title: "Final Project: Analysis",
  code: "32PT-FP-AN",
  paper: "a4",
  math-font-scale: 1.125,
  colour-scheme: colour-scheme,
  heading-numbering: "1",
  flags: ("showsection",),
)

= Analysis

1. *Why did you choose the specific algorithms for each queue?*
  - We believe that combining the two scheduling algorithms (SJF and NPPS) would allow us to strike a balance between urgency and efficiency.

2. *What are the advantages and disadvantages you observed? (can be based on TAT and WT)*
  - Using NPPS and SJF made important tasks done first and short once finished quickly for a balanced workflow. There aren't much disadvantages to mention since there's barely any unfairness and starvation taking place.

3. *Did starvation or unfairness occur in any of the queues? If yes, explain why.*
  - Since our system only executes tasks one at a time, there is little unfairness that occurs, except in cases where a process is particularly lengthy. Starvation will only happen if a process with a long burst time is present.

4. *What challenges did you encounter while simulating multilevel scheduling?*
  - We ran into numerous problems whilst writing code to simulate multilevel scheduling. Most notably, while writing code to simulate the ticking of CPU time, the ticker went into integer overflow multiple times for reasons yet to be concluded, which was only fixed by entirely rewriting the code for simulating process execution.

5. *How does multilevel scheduling affect overall performance compared to using a single algorithm?*
  - A multilevel schedule makes mixed workloads faster by giving urgent tasks priority and finishing small tasks quickly.
#pagebreak()

= Screenshots
#show: align.with(center)
#block(height: 65%, breakable: false)[
  #stack(dir: ttb,
  image("image-2.png"),
  image("image-3.png"))]